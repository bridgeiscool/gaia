package gaia.project.game.board;

import com.google.common.collect.ImmutableList;

import gaia.project.game.PlanetType;
import javafx.scene.Parent;

public final class Sector3 {
  // Top down and left to right looking at sector right side up
  private static final ImmutableList<PlanetType> PLANETS = ImmutableList.<PlanetType> builder()
      .add(
          PlanetType.TRANSDIM,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.GAIA,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.GRAY,
          PlanetType.NONE,
          PlanetType.WHITE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.BLUE,
          PlanetType.NONE,
          PlanetType.YELLOW)
      .build();
  private static Sector instance;

  public static Sector getInstance(Parent parent, SectorLocation location) {
    if (instance == null) {
      instance = new Sector(parent, location, PLANETS);
    }

    return instance;
  }
}
