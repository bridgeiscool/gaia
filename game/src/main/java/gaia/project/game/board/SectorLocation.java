package gaia.project.game.board;

import static gaia.project.game.board.BoardUtils.ROOT_3;

public enum SectorLocation {
  LOC0(160, 160),
  LOC1(160 + 15.0 * BoardUtils.hexSize(), 160 + ROOT_3 * BoardUtils.hexSize()),
  LOC2(160 + 30.0 * BoardUtils.hexSize(), 160 + 2.0 * ROOT_3 * BoardUtils.hexSize()),
  LOC3(160 - 9.0 * BoardUtils.hexSize(), 160 + 7.0 * ROOT_3 * BoardUtils.hexSize()),
  LOC4(160 + 6.0 * BoardUtils.hexSize(), 160 + 8.0 * ROOT_3 * BoardUtils.hexSize()),
  LOC5(160 + 21.0 * BoardUtils.hexSize(), 160 + 9.0 * ROOT_3 * BoardUtils.hexSize()),
  LOC6(160 + 36.0 * BoardUtils.hexSize(), 160 + 10.0 * ROOT_3 * BoardUtils.hexSize()),
  LOC7(160 - 3.0 * BoardUtils.hexSize(), 160 + 15.0 * ROOT_3 * BoardUtils.hexSize()),
  LOC8(160 + 12.0 * BoardUtils.hexSize(), 160 + 16.0 * ROOT_3 * BoardUtils.hexSize()),
  LOC9(160 + 27.0 * BoardUtils.hexSize(), 160 + 17.0 * ROOT_3 * BoardUtils.hexSize());

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
