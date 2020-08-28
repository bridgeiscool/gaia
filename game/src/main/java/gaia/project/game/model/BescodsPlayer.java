package gaia.project.game.model;

import java.util.TreeSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import gaia.project.game.PlanetType;
import gaia.project.game.board.Hex;
import javafx.beans.property.SimpleBooleanProperty;

public class BescodsPlayer extends Player {
  private static final long serialVersionUID = 2663152509950065941L;

  public BescodsPlayer(PlayerEnum playerEnum) {
    super(Race.BESCODS, playerEnum);

    getSpecialActions().put(PlayerBoardAction.BUMP_LOWEST_TECH, new SimpleBooleanProperty(false));
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
}
