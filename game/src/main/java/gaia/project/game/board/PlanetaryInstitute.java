package gaia.project.game.board;

import gaia.project.game.model.PlayerEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlanetaryInstitute extends Rectangle {
  private static final int SIDE = 23;

  private final Hex hex;
  private final PlayerEnum player;

  public PlanetaryInstitute(Hex hex, Color color, PlayerEnum player) {
    super(SIDE * BoardUtils.getScaling(), SIDE * BoardUtils.getScaling(), color);
    this.hex = hex;
    this.player = player;
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
