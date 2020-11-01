package gaia.project.game.board;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import gaia.project.game.controller.PlanetType;
import javafx.scene.Parent;

public final class Sector1 {
  // Top down and left to right looking at sector right side up
  private static final ImmutableList<PlanetType> PLANETS = ImmutableList.<PlanetType> builder()
      .add(
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.BROWN,
          PlanetType.BLUE,
          PlanetType.YELLOW,
          PlanetType.NONE,
          PlanetType.TRANSDIM,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.ORANGE,
          PlanetType.RED)
      .build();

  public static Sector getInstance(Parent parent, Entry<SectorLocation, Rot> entry) {
    return new Sector(parent, entry.getKey(), PLANETS, 1, entry.getValue());
  }
}
