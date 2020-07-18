package gaia.project.game;

import com.google.common.collect.ImmutableList;

import javafx.scene.Parent;

public final class Sector2 {
  // Top down and left to right looking at sector right side up
  private static final ImmutableList<PlanetType> PLANETS = ImmutableList.<PlanetType> builder()
      .add(
          PlanetType.GRAY,
          PlanetType.ORANGE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.WHITE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.YELLOW,
          PlanetType.BROWN,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.RED,
          PlanetType.TRANSDIM,
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