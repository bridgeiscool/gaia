package gaia.project.game;

import static gaia.project.game.GuiUtils.ROOT_3;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class GaiaformerSlot extends Polygon {
  private static final double HEX_SIZE = 10;

  private boolean hasGaiaformer;
  private Color raceColor;

  public GaiaformerSlot(boolean hasGaiaformer, Color raceColor) {
    super(// TOP LEFT
        -1.0 * HEX_SIZE,
        -1.0 * HEX_SIZE * ROOT_3,
        // TOP RIGHT
        HEX_SIZE,
        -1.0 * HEX_SIZE * ROOT_3,
        // RIGHT
        HEX_SIZE * 2,
        0,
        // BOTTOM_RIGHT
        HEX_SIZE,
        HEX_SIZE * ROOT_3,
        // BOTTOM_LEFT
        -1.0 * HEX_SIZE,
        HEX_SIZE * ROOT_3,
        // LEFT
        -1.0 * HEX_SIZE * 2,
        0);

    if (hasGaiaformer) {
      setFill(raceColor);
      getStyleClass().clear();
      getStyleClass().add("blackBorder");
    } else {
      setFill(null);
      getStyleClass().clear();
      getStyleClass().add("dashedBorder");
    }
  }
}
