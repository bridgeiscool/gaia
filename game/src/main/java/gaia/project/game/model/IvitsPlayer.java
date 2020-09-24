package gaia.project.game.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import gaia.project.game.board.EmptyHex;
import gaia.project.game.board.GameBoard;
import gaia.project.game.board.Hex;
import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.PlanetaryInstitute;
import gaia.project.game.board.Satellite;
import gaia.project.game.board.SpaceStation;
import gaia.project.game.controller.PlanetType;
import javafx.beans.property.SimpleBooleanProperty;

public final class IvitsPlayer extends Player {
  public final Set<String> spaceStations = new HashSet<>();

  public static IvitsPlayer createNew(PlayerEnum playerEnum) {
    IvitsPlayer p = new IvitsPlayer();
    p.fromRace(Race.IVITS, playerEnum);

    return p;
  }

  public static IvitsPlayer empty() {
    return new IvitsPlayer();
  }

  private IvitsPlayer() {}

  @Override
  public void buildSetupMine(HexWithPlanet hex, GameBoard gameBoard) {
    PlanetaryInstitute pi = new PlanetaryInstitute(hex, getRace().getColor(), getPlayerEnum());
    hex.addPi(pi);
    getPi().add(hex.getHexId());

    // Update income
    getRace().getPiIncome().addTo(getCurrentIncome());

    // Add action
    getSpecialActions().put(PlayerBoardAction.SPACE_STATION, new SimpleBooleanProperty(false));

    getBuiltOn().add(PlanetType.RED);
  }

  public Set<String> getSpaceStations() {
    return spaceStations;
  }

  public void buildSpaceStation(EmptyHex hex, GameBoard gameBoard) {
    SpaceStation ss = new SpaceStation(hex, getPlayerEnum());
    spaceStations.add(hex.getHexId());
    hex.addSpaceStation(ss);

    recheckAllBuildingsInFederations(gameBoard);
  }

  @Override
  public Set<String> allBuildingLocations() {
    return Sets.union(super.allBuildingLocations(), spaceStations);
  }

  @Override
  public boolean canBuildSatellite() {
    return getQic().get() > 0;
  }

  @Override
  public void addSatellite(EmptyHex emptyHex) {
    Preconditions.checkArgument(getQic().get() > 0);
    getSatellites().add(emptyHex.getHexId());
    Satellite satellite = new Satellite(emptyHex, getRace().getColor(), getPlayerEnum());
    emptyHex.addSatelliteUI(satellite);

    Util.minus(getQic(), 1);
  }

  @Override
  public boolean couldFederate() {
    return Sets.union(Sets.union(getMines(), getLostPlanet()), spaceStations).size()
        + 2 * Sets.union(getTradingPosts(), getResearchLabs()).size()
        + getBigBuildingPower().intValue()
            * Sets.union(Sets.union(getPi(), getQa()), getKa()).size() >= nextFederationPower();
  }

  @Override
  public int getPower(Hex hex) {
    return getPower(hex.getHexId());
  }

  private int getPower(String hexId) {
    if (Sets.union(Sets.union(getMines(), getLostPlanet()), spaceStations).contains(hexId)) {
      return 1;
    }

    if (Sets.union(getTradingPosts(), getResearchLabs()).contains(hexId)) {
      return 2;
    }

    if (Sets.union(Sets.union(getPi(), getQa()), getKa()).contains(hexId)) {
      return getBigBuildingPower().intValue();
    }

    return 0;
  }

  public int totalFederationPower() {
    return Iterables.getOnlyElement(getFederations()).stream().collect(Collectors.summingInt(this::getPower));
  }

  public int nextFederationPower() {
    // Can gain an additional federation tile if terraforming is maxed...
    return 7 * (getFederationTiles().size() + (getTerraformingLevel().get() == 5 ? 0 : 1));
  }

  @Override
  protected void writeExtraContent(JsonWriter json) throws IOException {
    JsonUtil.writeCoordsCollection(json, JsonUtil.SPACE_STATIONS, spaceStations);
  }

  @Override
  protected void handleAdditionalContent(String name, JsonReader json) throws IOException {
    Preconditions.checkArgument(JsonUtil.SPACE_STATIONS.contentEquals(name));
    JsonUtil.readCoordsArray(spaceStations, json);
  }
}
