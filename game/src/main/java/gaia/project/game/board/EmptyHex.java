package gaia.project.game.board;

import gaia.project.game.model.Coords;

public class EmptyHex extends Hex {
  public EmptyHex(Coords coords, int sectorId) {
    super(coords, sectorId);
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  @Override
  public Planet getPlanet() {
    return null;
  }
}
