package gaia.project.game.board;

import gaia.project.game.model.PlayerEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Mine extends Rectangle {
  public static final int BASE_LENGTH = 18;
  public static final int BASE_WIDTH = 14;
  private static final int POWER = 1;

  private final Hex hex;
  private final PlayerEnum player;

  public Mine(Hex hex, Color color, PlayerEnum player) {
    super(BASE_LENGTH * BoardUtils.getScaling(), BASE_WIDTH * BoardUtils.getScaling());
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
