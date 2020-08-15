package gaia.project.game;

import gaia.project.game.model.FederationTile;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;

public class FederationTokenPane extends StackPane {
  private static final double HEIGHT = 64;
  private static final double WIDTH = 60;

  public FederationTokenPane(FederationTile federationTile, double scaling) {
    getStyleClass().add("fedToken");
    ObservableList<Node> children = getChildren();
    children.add(new Shape(scaling, federationTile.isFlippable() ? "greenFedToken" : "grayFedToken"));
    children.add(new Label(federationTile.getText()));
  }

  private static class Shape extends Polygon {
    Shape(double scaling, String styleClass) {
      super(
          // TOP LEFT
          0.0,
          0.0,
          // TOP RIGHT
          WIDTH * scaling,
          0.0,
          // RIGHT BOTTOM
          WIDTH * scaling,
          0.9 * HEIGHT * scaling,
          // BOTTOM
          WIDTH * scaling / 2.0,
          HEIGHT * scaling,
          // LEFT BOTTOM
          0.0,
          0.9 * HEIGHT * scaling);
      getStyleClass().add(styleClass);
    }
  }
}
