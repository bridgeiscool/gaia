package gaia.project.game.board;

import gaia.project.game.model.PlayerEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Satellite extends Rectangle {
  private static final int SIDE = 10;

  private final Hex hex;
  private final PlayerEnum player;

  public Satellite(Hex hex, Color color, PlayerEnum player) {
    super(SIDE, SIDE);
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
}
