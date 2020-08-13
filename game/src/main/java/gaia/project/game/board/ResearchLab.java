package gaia.project.game.board;

import gaia.project.game.model.PlayerEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ResearchLab extends Circle {
  private static final int RADIUS = 10;

  private final Hex hex;
  private final PlayerEnum player;

  public ResearchLab(Hex hex, Color color, PlayerEnum player) {
    super(RADIUS, color);

    this.hex = hex;
    this.player = player;
    setStroke(Color.BLACK);
    setStrokeWidth(2.0);
  }

  public int getPower() {
    return 2;
  }

  public Hex getHex() {
    return hex;
  }

  public PlayerEnum getPlayer() {
    return player;
  }
}
