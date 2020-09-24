package gaia.project.game.model;

import java.io.IOException;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import gaia.project.game.board.GameBoard;
import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.Mine;
import gaia.project.game.controller.PlanetType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public final class LantidsPlayer extends Player {
  private final ObservableSet<String> leechMines = FXCollections.observableSet();

  public static LantidsPlayer createNew(PlayerEnum playerEnum) {
    LantidsPlayer p = new LantidsPlayer();
    p.fromRace(Race.LANTIDS, playerEnum);
    return p;
  }

  public static LantidsPlayer empty() {
    return new LantidsPlayer();
  }

  private LantidsPlayer() {}

  @Override
  protected void raceSpecificListeners() {
    leechMines.addListener((SetChangeListener<String>) change -> {
      if (!getPi().isEmpty()) {
        Util.plus(getResearch(), 2);
      }
    });
  }

  public ObservableSet<String> getLeechMines() {
    return leechMines;
  }

  public void buildLeechMine(HexWithPlanet hex, GameBoard gameBoard) {
    Mine mine = new Mine(hex, getRace().getColor(), getPlayerEnum());
    getMines().add(hex.getHexId());
    leechMines.add(hex.getHexId());
    hex.addLeechMine(mine);

    // Update income
    if (getMines().size() != 3) {
      Util.plus(getCurrentIncome().getOreIncome(), 1);
    }

    // Update planet counts
    PlanetType planetType = hex.getPlanet().getPlanetType();
    if (planetType == PlanetType.GAIA) {
      // Add to trigger the building bonuses, then immediately remove so it doesn't get counted in other measures
      Util.plus(getGaiaPlanets(), 1);
      Util.minus(getGaiaPlanets(), 1);
    }
    Util.plus(getTotalBuildings(), 1);
    getSectors().add(hex.getSectorId());

    // Pay mine cost
    Util.minus(getOre(), 1);
    Util.minus(getCredits(), 2);

    for (Set<String> federation : getFederations()) {
      if (federation.stream().anyMatch(c -> hex.isWithinRangeOf(gameBoard.hexWithId(c), 1))) {
        federation.add(hex.getHexId());
      }
    }

    for (String sat : getSatellites()) {
      if (hex.isWithinRangeOf(gameBoard.hexWithId(sat), 1)) {
        // Just add to the first possible fed for now. Shouldn't matter
        // TODO: Check and add logic to check which fed later
        getFederations().iterator().next().add(hex.getHexId());
      }
    }

    // We might have added something that causes more additions to an existing federation
    recheckAllBuildingsInFederations(gameBoard);
  }

  @Override
  protected void writeExtraContent(JsonWriter json) throws IOException {
    JsonUtil.writeCoordsCollection(json, JsonUtil.LEECH_MINES, leechMines);
  }

  @Override
  protected void handleAdditionalContent(String name, JsonReader json) throws IOException {
    Preconditions.checkArgument(JsonUtil.LEECH_MINES.contentEquals(name));
    JsonUtil.readCoordsArray(leechMines, json);
  }
}
