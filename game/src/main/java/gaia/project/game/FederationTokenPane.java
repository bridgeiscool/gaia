package gaia.project.game;

import java.util.function.Consumer;

import gaia.project.game.model.FederationTile;
import gaia.project.game.model.Player;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

  public static FederationTokenPane regular(FederationTile federationTile) {
    return new FederationTokenPane(
        federationTile,
        Size.REGULAR,
        new SimpleBooleanProperty(federationTile.isFlippable()));
  }

  public static FederationTokenPane mini(FederationTile federationTile, BooleanProperty flippable) {
    return new FederationTokenPane(federationTile, Size.MINI, flippable);
  }

  public FederationTokenPane(FederationTile federationTile, Size size, BooleanProperty flippable) {
    this.federationTile = federationTile;
    getStyleClass().add(size.styleClass);
    this.shape = new Shape(size.scaling, flippable.get() ? "greenFedToken" : "grayFedToken");
    flippable.addListener((o, oldValue, newValue) -> flip());
    ObservableList<Node> children = getChildren();
    children.add(shape);
    children.add(new Label(federationTile.getText()));
  }

  public FederationTile getFederationTile() {
    return federationTile;
  }

  public void flip() {
    shape.getStyleClass().clear();
    shape.getStyleClass().add("grayFedToken");
  }

  public void highlight(Player activePlayer, Consumer<FederationTile> callback) {
    shape.getStyleClass().clear();
    shape.getStyleClass().add(federationTile.isFlippable() ? "greenFedTokenHighlighted" : "grayFedTokenHighlighted");
    this.setOnMouseClicked(me -> {
      activePlayer.addFederationTile(federationTile);
      callback.accept(federationTile);
    });
  }

  public void highlightForCopy(Player activePlayer, CallBack callback) {
    shape.getStyleClass().clear();
    shape.getStyleClass().add(federationTile.isFlippable() ? "greenFedTokenHighlighted" : "grayFedTokenHighlighted");
    this.setOnMouseClicked(me -> {
      federationTile.updatePlayer(activePlayer);
      callback.call();
    });
  }

  public void clearHighlighting() {
    shape.getStyleClass().clear();
    shape.getStyleClass().add(federationTile.isFlippable() ? "greenFedToken" : "grayFedToken");
    setOnMouseClicked(null);
  }

  private enum Size {
    REGULAR(1.0, "fedToken"), MINI(0.7, "miniFedToken");

    private final double scaling;
    private final String styleClass;

    Size(double scaling, String styleClass) {
      this.scaling = scaling;
      this.styleClass = styleClass;
    }

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
