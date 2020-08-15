package gaia.project.game.board;

import gaia.project.game.model.PlayerEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Gaiaformer extends Polygon {
  private static final int HEX_SIZE = 9;
  private static final double ROOT_3 = Math.sqrt(3.0);
  private final Hex hex;
  private final PlayerEnum player;

  public Gaiaformer(Hex hex, Color color, PlayerEnum player) {
    super(
        // TOP LEFT
        -1.0 * HEX_SIZE,
        -1.0 * HEX_SIZE * ROOT_3,
        // TOP RIGHT
        HEX_SIZE,
        -1.0 * HEX_SIZE * ROOT_3,
        // RIGHT
        HEX_SIZE * 2,
        0.0,
        // BOTTOM RIGHT,
        HEX_SIZE,
        HEX_SIZE * ROOT_3,
        // BOTTOM LEFT
        -1.0 * HEX_SIZE,
        HEX_SIZE * ROOT_3,
        // LEFT
        -2.0 * HEX_SIZE,
        0.0);
    this.hex = hex;
    this.player = player;
    setStroke(Color.BLACK);
    setFill(color);
    setStrokeWidth(2.0);
  }

  public Hex getHex() {
    return hex;
  }

  public PlayerEnum getPlayer() {
    return player;
  }
}
