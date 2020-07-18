package gaia.project.game.board;

import com.google.common.collect.ImmutableList;

import gaia.project.game.PlanetType;
import javafx.scene.Parent;

public final class Sector8 {
  // Top down and left to right looking at sector right side up
  private static final ImmutableList<PlanetType> PLANETS = ImmutableList.<PlanetType> builder()
      .add(
          PlanetType.BLUE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.WHITE,
          PlanetType.TRANSDIM,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.ORANGE,
          PlanetType.GRAY,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.TRANSDIM,
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
