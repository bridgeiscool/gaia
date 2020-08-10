package gaia.project.game.board;

import gaia.project.game.model.PlayerEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Mine extends Rectangle {
  public static final int LENGTH = 20;
  public static final int WIDTH = 15;
  private static final int POWER = 1;

  private final Hex hex;
  private final PlayerEnum player;

  public Mine(Hex hex, Color color, PlayerEnum player) {
    super(LENGTH, WIDTH);
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
