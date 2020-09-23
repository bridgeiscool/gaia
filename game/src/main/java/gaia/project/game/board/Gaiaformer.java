package gaia.project.game.board;

import gaia.project.game.model.PlayerEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Gaiaformer extends Polygon {
  private static final int BASE_HEX_SIZE = 9;
  private static final double ROOT_3 = Math.sqrt(3.0);
  private final Hex hex;
  private final PlayerEnum player;

  public Gaiaformer(Hex hex, Color color, PlayerEnum player) {
    super(
        // TOP LEFT
        -1.0 * BASE_HEX_SIZE * BoardUtils.getScaling(),
        -1.0 * BASE_HEX_SIZE * BoardUtils.getScaling() * ROOT_3,
        // TOP RIGHT
        BASE_HEX_SIZE * BoardUtils.getScaling(),
        -1.0 * BASE_HEX_SIZE * BoardUtils.getScaling() * ROOT_3,
        // RIGHT
        BASE_HEX_SIZE * BoardUtils.getScaling() * 2,
        0.0,
        // BOTTOM RIGHT,
        BASE_HEX_SIZE * BoardUtils.getScaling(),
        BASE_HEX_SIZE * BoardUtils.getScaling() * ROOT_3,
        // BOTTOM LEFT
        -1.0 * BASE_HEX_SIZE * BoardUtils.getScaling(),
        BASE_HEX_SIZE * BoardUtils.getScaling() * ROOT_3,
        // LEFT
        -2.0 * BASE_HEX_SIZE * BoardUtils.getScaling(),
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
