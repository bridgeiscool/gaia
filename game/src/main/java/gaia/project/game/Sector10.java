package gaia.project.game;

import com.google.common.collect.ImmutableList;

import javafx.scene.Parent;

public final class Sector10 {
  // Top down and left to right looking at sector right side up
  private static final ImmutableList<PlanetType> PLANETS = ImmutableList.<PlanetType> builder()
      .add(
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.TRANSDIM,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.TRANSDIM,
          PlanetType.YELLOW,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.GAIA,
          PlanetType.BLUE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.RED,
          PlanetType.NONE,
          PlanetType.NONE)
      .build();
  private static Sector instance;

  public static Sector getInstance(Parent parent, double centerX, double centerY) {
    if (instance == null) {
      instance = new Sector(parent, 500, 500, centerX, centerY, PLANETS);
    }

    return instance;
  }
}
