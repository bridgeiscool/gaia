package gaia.project.game.board;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import gaia.project.game.controller.PlanetType;
import javafx.scene.Parent;

public final class Sector7 {
  // Top down and left to right looking at sector right side up
  private static final ImmutableList<PlanetType> PLANETS = ImmutableList.<PlanetType> builder()
      .add(
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.BROWN,
          PlanetType.TRANSDIM,
          PlanetType.RED,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.GAIA,
          PlanetType.GAIA,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.GRAY)
      .build();

  public static Sector getInstance(Parent parent, Entry<SectorLocation, Rot> entry) {
    return new Sector(parent, entry.getKey(), PLANETS, 7, entry.getValue());
  }
}
