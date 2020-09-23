package gaia.project.game.board;

import gaia.project.game.model.PlayerEnum;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SpaceStation extends StackPane {
  private static final int SIDE = 20;
  private static final int INNER_SIDE = 5;

  private final Hex hex;
  private final PlayerEnum player;

  public SpaceStation(Hex hex, PlayerEnum player) {
    this.hex = hex;
    this.player = player;

    Rectangle rectangle = new Rectangle(SIDE * BoardUtils.getScaling(), SIDE * BoardUtils.getScaling(), Color.SILVER);
    rectangle.setStroke(Color.BLACK);
    rectangle.setStrokeWidth(1.0);

    getChildren().add(rectangle);

    Rectangle inner =
        new Rectangle(INNER_SIDE * BoardUtils.getScaling(), INNER_SIDE * BoardUtils.getScaling(), Color.RED);
    inner.setStroke(Color.BLACK);
    inner.setStrokeWidth(1.0);

    getChildren().add(inner);
  }

  public Hex getHex() {
    return hex;
  }

  public PlayerEnum getPlayer() {
    return player;
  }
}
