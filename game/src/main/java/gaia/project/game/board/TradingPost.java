package gaia.project.game.board;

import gaia.project.game.model.PlayerEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class TradingPost extends Polygon {
  private static final int POWER = 2;

  private final Hex hex;
  private final PlayerEnum player;

  public TradingPost(Hex hex, Color color, PlayerEnum player) {
    super(
        // LEFT BOTTOM
        0.0,
        0.0,
        // LEFT TOP
        0.0,
        -12.0 * BoardUtils.getScaling(),
        // POINT
        5.0 * BoardUtils.getScaling(),
        -20.0 * BoardUtils.getScaling(),
        // TOP MIDDLE
        10.0 * BoardUtils.getScaling(),
        -12.0 * BoardUtils.getScaling(),
        // RIGHT TOP
        20.0 * BoardUtils.getScaling(),
        -12.0 * BoardUtils.getScaling(),
        // RIGHT BOTTOM
        20.0 * BoardUtils.getScaling(),
        0.0);
    this.hex = hex;
    this.player = player;
    setFill(color);
    setStroke(Color.BLACK);
    setStrokeWidth(2.0);
  }

  public Hex getHex() {
    return hex;
  }

  public PlayerEnum getPlayer() {
    return player;
  }

  public int getPower() {
    return POWER;
  }
}
