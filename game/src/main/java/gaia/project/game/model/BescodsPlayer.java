package gaia.project.game.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import gaia.project.game.board.Academy;
import gaia.project.game.board.Hex;
import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.PlanetaryInstitute;
import gaia.project.game.controller.PlanetType;
import javafx.beans.property.SimpleBooleanProperty;

public final class BescodsPlayer extends Player {
  private final Set<Coords> grayPlanets = new HashSet<>();

  public static BescodsPlayer createNew(PlayerEnum playerEnum) {
    BescodsPlayer p = new BescodsPlayer();
    p.fromRace(Race.BESCODS, playerEnum);
    p.getSpecialActions().put(PlayerBoardAction.BUMP_LOWEST_TECH, new SimpleBooleanProperty(false));
    return p;
  }

  public static BescodsPlayer empty() {
    return new BescodsPlayer();
  }

  private BescodsPlayer() {}

  @Override
  public void buildMine(HexWithPlanet hex) {
    super.buildMine(hex);
    if (hex.getPlanet().getPlanetType() == PlanetType.GRAY) {
      grayPlanets.add(hex.getCoords());
    }
  }

  @Override
  public void buildSetupMine(HexWithPlanet hex) {
    super.buildSetupMine(hex);
    grayPlanets.add(hex.getCoords());
  }

  @Override
  public int getPower(Hex hex) {
    if (getLostPlanet().contains(hex.getCoords())) {
      return 1;
    }

    if (getMines().contains(hex.getCoords())) {
      return !getPi().isEmpty() && hex.getPlanet().getPlanetType() == PlanetType.GRAY ? 2 : 1;
    }

    if (Sets.union(getTradingPosts(), getResearchLabs()).contains(hex.getCoords())) {
      return !getPi().isEmpty() && hex.getPlanet().getPlanetType() == PlanetType.GRAY ? 3 : 2;
    }

    if (Sets.union(Sets.union(getPi(), getQa()), getKa()).contains(hex.getCoords())) {
      return !getPi().isEmpty() && hex.getPlanet().getPlanetType() == PlanetType.GRAY
          ? getBigBuildingPower().get() + 1
          : getBigBuildingPower().get();
    }

    throw new IllegalStateException("No power!");
  }

  @Override
  protected int getExcessBuildingPower() {
    int totalPower = 0;

    for (Coords coords : Sets.union(getMines(), getLostPlanet())) {
      if (!inFederation(coords)) {
        totalPower += extraPower(coords) ? 2 : 1;
      }
    }

    for (Coords coords : Sets.union(getTradingPosts(), getResearchLabs())) {
      if (!inFederation(coords)) {
        totalPower += extraPower(coords) ? 3 : 2;
      }
    }

    for (Coords coords : Sets.union(Sets.union(getPi(), getQa()), getKa())) {
      if (!inFederation(coords)) {
        totalPower += extraPower(coords) ? getBigBuildingPower().intValue() + 1 : getBigBuildingPower().intValue();
      }
    }

    return totalPower;
  }

  @Override
  public void buildPI(HexWithPlanet hex) {
    PlanetaryInstitute pi = new PlanetaryInstitute(hex, Race.BESCODS.getColor(), getPlayerEnum());
    hex.switchBuildingUI(pi);
    getPi().add(hex.getCoords());
    getResearchLabs().remove(hex.getCoords());

    Util.minus(getOre(), 4);
    Util.minus(getCredits(), 6);

    // Update income
    Race.BESCODS.getRlIncome().get(getResearchLabs().size()).removeFrom(getCurrentIncome());
    Race.BESCODS.getPiIncome().addTo(getCurrentIncome());
  }

  public void buildAcademy(HexWithPlanet hex, boolean ka) {
    Academy academy = new Academy(hex, Race.BESCODS.getColor(), getPlayerEnum());
    hex.switchBuildingUI(academy);
    if (ka) {
      getKa().add(hex.getCoords());
    } else {
      getQa().add(hex.getCoords());
    }
    getTradingPosts().remove(hex.getCoords());

    Util.minus(getOre(), 6);
    Util.minus(getCredits(), 6);

    // Update Income
    Race.BESCODS.getTpIncome().get(getTradingPosts().size()).removeFrom(getCurrentIncome());
    if (ka) {
      Util.plus(getCurrentIncome().getResearchIncome(), getRace().getKaIncome());
    } else {
      getSpecialActions().put(Race.BESCODS.getQaAction(), new SimpleBooleanProperty(false));
    }
  }

  private boolean extraPower(Coords coords) {
    if (grayPlanets.contains(coords) && !getPi().isEmpty()) {
      return true;
    }

    return false;
  }

  public int lowestTechLevel() {
    return new TreeSet<Integer>(
        ImmutableSet.of(
            getTerraformingLevel().get(),
            getNavLevel().get(),
            getAiLevel().get(),
            getGaiaformingLevel().get(),
            getEconLevel().get(),
            getKnowledgeLevel().get())).first();
  }

  @Override
  protected void writeExtraContent(JsonWriter json) throws IOException {
    JsonUtil.writeCoordsCollection(json, JsonUtil.GRAY_PLANETS, grayPlanets);
  }

  @Override
  protected void handleAdditionalContent(String name, JsonReader json) throws IOException {
    Preconditions.checkArgument(JsonUtil.GRAY_PLANETS.contentEquals(name));
    JsonUtil.readCoordsArray(grayPlanets, json);
  }
}
