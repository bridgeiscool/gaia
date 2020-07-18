package gaia.project.game.board;

import com.google.common.collect.ImmutableList;

import gaia.project.game.PlanetType;
import javafx.scene.Parent;

public final class Sector4 {
  // Top down and left to right looking at sector right side up
  private static final ImmutableList<PlanetType> PLANETS = ImmutableList.<PlanetType> builder()
      .add(
          PlanetType.GRAY,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.RED,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.WHITE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.ORANGE,
          PlanetType.BROWN,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.BLUE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE)
      .build();
  private static Sector instance;

  public static Sector getInstance(Parent parent, SectorLocation location) {
    if (instance == null) {
      instance = new Sector(parent, location, PLANETS);
    }

    return instance;
  }
}
