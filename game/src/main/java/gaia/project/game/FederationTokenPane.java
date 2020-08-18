package gaia.project.game;

import java.util.function.Consumer;

import gaia.project.game.model.FederationTile;
import gaia.project.game.model.Player;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;

public class FederationTokenPane extends StackPane {
  private static final double HEIGHT = 64;
  private static final double WIDTH = 60;
  private final FederationTile federationTile;

  private final Shape shape;

  public FederationTokenPane(FederationTile federationTile, double scaling) {
    this.federationTile = federationTile;
    getStyleClass().add("fedToken");
    ObservableList<Node> children = getChildren();
    this.shape = new Shape(scaling, federationTile.isFlippable() ? "greenFedToken" : "grayFedToken");
    children.add(shape);
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

  public void highlight(Player activePlayer, Consumer<FederationTile> callback) {
    shape.getStyleClass().clear();
    shape.getStyleClass().add(federationTile.isFlippable() ? "greenFedTokenHighlighted" : "grayFedTokenHighlighted");
    this.setOnMouseClicked(me -> {
      activePlayer.addFederationTile(federationTile);
      callback.accept(federationTile);
    });
  }

  public void clearHighlighting() {
    shape.getStyleClass().clear();
    shape.getStyleClass().add(federationTile.isFlippable() ? "greenFedToken" : "grayFedToken");
    setOnMouseClicked(null);
  }

}
