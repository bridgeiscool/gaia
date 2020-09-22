package gaia.project.game.board;

public final class BoardUtils {
  private BoardUtils() {}

  // These sizings worked for Mike's Mac and will need to be scaled for other window sizes

  // Hex side length / 2
  private static final double HEX_SIZE = 18;
  private static final double PLANET_RADIUS = 24;

  // Longer non-hypotenuse side
  public static final double ROOT_3 = Math.sqrt(3.0);

  public static final double TWO_ROOT_3 = 2.0 * ROOT_3;

  public static double hexSize() {
    return HEX_SIZE;
  }

  public static double planetRadius() {
    return PLANET_RADIUS;
  }
}
