package gaia.project.game.board;

public final class BoardUtils {
  private BoardUtils() {}

  // Hex side length / 2
  static final double HEX_SIZE = 20;
  // Longer non-hypotenuse side
  static final double ROOT_3 = Math.sqrt(3.0);

  static final double TWO_ROOT_3 = 2.0 * ROOT_3;

  static final double PLANET_RADIUS = 24;
}
