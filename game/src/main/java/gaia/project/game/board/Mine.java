package gaia.project.game.board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Mine extends Rectangle {
  public static final int LENGTH = 20;
  public static final int WIDTH = 15;

  private final Hex hex;

  public Mine(Hex hex, Color color) {
    super(LENGTH, WIDTH);
    this.hex = hex;
    setFill(color);
    setStroke(Color.BLACK);
    setStrokeWidth(2.0);
  }
}
