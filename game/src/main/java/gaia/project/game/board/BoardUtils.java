package gaia.project.game.board;

public final class BoardUtils {
  private BoardUtils() {}

  private static double scaling = 1.0;

  // These sizings were created for the following resolution and must be scaled accordingly
  private static final int BASE_WIDTH = 1792;
  private static final int BASE_HEIGHT = 1120;

  // Hex side length / 2
  private static final double HEX_SIZE = 18;
  private static final double PLANET_RADIUS = 24;

  // Longer non-hypotenuse side
  public static final double ROOT_3 = Math.sqrt(3.0);

  public static final double TWO_ROOT_3 = 2.0 * ROOT_3;

  public static double hexSize() {
    return scaling * HEX_SIZE;
  }

  public static double planetRadius() {
    return scaling * PLANET_RADIUS;
  }

  public static void setScaling(double screenWidth, double screenHeight) {
    double widthRatio = screenWidth / BASE_WIDTH;
    double heightRatio = screenHeight / BASE_HEIGHT;

    // Pick the smaller one
    scaling = widthRatio < heightRatio ? widthRatio : heightRatio;
  }

  public static double getScaling() {
    return scaling;
  }
}
