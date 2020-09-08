package gaia.project.game.board;

import com.google.common.collect.ImmutableList;

import gaia.project.game.controller.PlanetType;
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

  public static Sector getInstance(Parent parent, SectorLocation location) {
    return new Sector(parent, location, PLANETS, 10);
  }
}
