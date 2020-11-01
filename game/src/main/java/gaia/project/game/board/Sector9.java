package gaia.project.game.board;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import gaia.project.game.controller.PlanetType;
import javafx.scene.Parent;

public final class Sector9 {
  // Top down and left to right looking at sector right side up
  private static final ImmutableList<PlanetType> PLANETS = ImmutableList.<PlanetType> builder()
      .add(
          PlanetType.NONE,
          PlanetType.ORANGE,
          PlanetType.TRANSDIM,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.WHITE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.GRAY,
          PlanetType.GAIA,
          PlanetType.BROWN,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE,
          PlanetType.NONE)
      .build();

  public static Sector getInstance(Parent parent, Entry<SectorLocation, Rot> entry) {
    return new Sector(parent, entry.getKey(), PLANETS, 9, entry.getValue());
  }
}
