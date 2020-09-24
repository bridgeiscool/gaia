package gaia.project.game.board;

import gaia.project.game.model.PlayerEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Academy extends Polygon {
  private static final int SIDE_LENGTH = 9;
  private static final double ROOT_2 = Math.sqrt(2.0);
  private final Hex hex;
  private final PlayerEnum player;

  public Academy(Hex hex, Color color, PlayerEnum player) {
    super(
        // TOP LEFT
        0.0,
        0.0,
        // TOP RIGHT
        SIDE_LENGTH * BoardUtils.getScaling(),
        0.0,
        // RIGHT TOP
        SIDE_LENGTH * BoardUtils.getScaling() + SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        // EXTENDED RIGHT TOP
        2.0 * SIDE_LENGTH * BoardUtils.getScaling() + SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        // EXTENDED RIGHT BOTTOM
        2.0 * SIDE_LENGTH * BoardUtils.getScaling() + SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        SIDE_LENGTH * BoardUtils.getScaling() + SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        // RIGHT BOTTOM
        SIDE_LENGTH * BoardUtils.getScaling() + SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        SIDE_LENGTH * BoardUtils.getScaling() + SIDE_LENGTH / ROOT_2,
        // BOTTOM RIGHT
        SIDE_LENGTH * BoardUtils.getScaling(),
        SIDE_LENGTH * BoardUtils.getScaling() + 2.0 * SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        // BOTTOM LEFT
        0.0,
        SIDE_LENGTH * BoardUtils.getScaling() + 2.0 * SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        // LEFT BOTTOM
        -1.0 * SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        SIDE_LENGTH * BoardUtils.getScaling() + SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        // LEFT TOP
        -1.0 * SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2,
        SIDE_LENGTH * BoardUtils.getScaling() / ROOT_2);
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
