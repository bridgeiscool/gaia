package gaia.project.game;

import gaia.project.game.model.RoundBooster;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RoundBoosterTile extends StackPane {
  private static final double WIDTH = 50.0;
  private static final double HEIGHT = 180.0;

  private RoundBooster roundBooster;

  public RoundBoosterTile(RoundBooster roundBooster) {
    this.roundBooster = roundBooster;
    getStyleClass().add("roundBooster");
    ObservableList<Node> children = getChildren();
    children.add(new BoosterRectangle());
    VBox vbox = new VBox(60.0, new Label(roundBooster.getTopText()), new Label(roundBooster.getBottomText()));
    vbox.setAlignment(Pos.CENTER);
    children.add(vbox);
  }

  private static class BoosterRectangle extends Rectangle {
    BoosterRectangle() {
      super(WIDTH, HEIGHT, Color.ANTIQUEWHITE);
    }
  }
}
