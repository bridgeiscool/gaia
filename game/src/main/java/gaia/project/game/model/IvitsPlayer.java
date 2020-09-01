package gaia.project.game.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import gaia.project.game.board.EmptyHex;
import gaia.project.game.board.Hex;
import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.PlanetaryInstitute;
import gaia.project.game.board.Satellite;
import gaia.project.game.board.SpaceStation;
import javafx.beans.property.SimpleBooleanProperty;

public class IvitsPlayer extends Player {
  private static final long serialVersionUID = -4915515584020163311L;
  public final Set<Coords> spaceStations = new HashSet<>();

  public IvitsPlayer(PlayerEnum playerEnum) {
    super(Race.IVITS, playerEnum);
  }

  @Override
  public void buildSetupMine(HexWithPlanet hex) {
    PlanetaryInstitute pi = new PlanetaryInstitute(hex, getRace().getColor(), getPlayerEnum());
    hex.addPi(pi);
    getPi().add(hex.getCoords());

    // Update income
    getRace().getPiIncome().addTo(getCurrentIncome());

    // Add action
    getSpecialActions().put(PlayerBoardAction.SPACE_STATION, new SimpleBooleanProperty(false));
  }

  public Set<Coords> getSpaceStations() {
    return spaceStations;
  }

  public void buildSpaceStation(EmptyHex hex) {
    SpaceStation ss = new SpaceStation(hex, getPlayerEnum());
    spaceStations.add(hex.getCoords());
    hex.addSpaceStation(ss);

    recheckAllBuildingsInFederations();
  }

  @Override
  public Set<Coords> allBuildingLocations() {
    return Sets.union(super.allBuildingLocations(), spaceStations);
  }

  @Override
  public boolean canBuildSatellite() {
    return getQic().get() > 0;
  }

  @Override
  public void addSatellite(EmptyHex emptyHex) {
    Preconditions.checkArgument(getQic().get() > 0);
    getSatellites().add(emptyHex.getCoords());
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
    return getPower(hex.getCoords());
  }

  private int getPower(Coords coords) {
    if (Sets.union(Sets.union(getMines(), getLostPlanet()), spaceStations).contains(coords)) {
      return 1;
    }

    if (Sets.union(getTradingPosts(), getResearchLabs()).contains(coords)) {
      return 2;
    }

    if (Sets.union(Sets.union(getPi(), getQa()), getKa()).contains(coords)) {
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
}