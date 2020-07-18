package gaia.project.game.board;

import static gaia.project.game.board.BoardUtils.HEX_SIZE;
import static gaia.project.game.board.BoardUtils.ROOT_3;

public enum SectorLocation {
  LOC0(160, 160),
  LOC1(160 + 15.0 * HEX_SIZE, 160 + ROOT_3 * HEX_SIZE),
  LOC2(160 + 30.0 * HEX_SIZE, 160 + 2.0 * ROOT_3 * HEX_SIZE),
  LOC3(160 - 9.0 * HEX_SIZE, 160 + 7.0 * ROOT_3 * HEX_SIZE),
  LOC4(160 + 6.0 * HEX_SIZE, 160 + 8.0 * ROOT_3 * HEX_SIZE),
  LOC5(160 + 21.0 * HEX_SIZE, 160 + 9.0 * ROOT_3 * HEX_SIZE),
  LOC6(160 + 36.0 * HEX_SIZE, 160 + 10.0 * ROOT_3 * HEX_SIZE),
  LOC7(160 - 3.0 * HEX_SIZE, 160 + 15.0 * ROOT_3 * HEX_SIZE),
  LOC8(160 + 12.0 * HEX_SIZE, 160 + 16.0 * ROOT_3 * HEX_SIZE),
  LOC9(160 + 27.0 * HEX_SIZE, 160 + 17.0 * ROOT_3 * HEX_SIZE);

  private final double centerX;
  private final double centerY;

  private SectorLocation(double centerX, double centerY) {
    this.centerX = centerX;
    this.centerY = centerY;
  }

  public double getCenterX() {
    return centerX;
  }

  public double getCenterY() {
    return centerY;
  }
}
