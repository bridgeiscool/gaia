package gaia.project.game.controller;

import java.util.function.Consumer;

import gaia.project.game.board.BoardUtils;
import gaia.project.game.model.FederationTile;
import gaia.project.game.model.Player;
import gaia.project.game.model.Player.FedToken;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

public class FederationTokenPane extends StackPane {
  private static final double HEIGHT = 54;
  private static final double WIDTH = 52;
  private static final double BASE_FONT_SIZE = 18;
  private final FederationTile federationTile;

  private final Shape shape;

  public static FederationTokenPane regular(FederationTile federationTile) {
    return new FederationTokenPane(
        federationTile,
        Size.REGULAR,
        new SimpleBooleanProperty(federationTile.isFlippable()));
  }

  public static FederationTokenPane techTrack(FederationTile federationTile) {
    return new FederationTokenPane(federationTile, Size.TECH, new SimpleBooleanProperty(federationTile.isFlippable()));
  }

  public static FederationTokenPane mini(FedToken token) {
    return new FederationTokenPane(token.getFederationTile(), Size.MINI, token.getFlippable());
  }

  public FederationTokenPane(FederationTile federationTile, Size size, BooleanProperty flippable) {
    this.federationTile = federationTile;
    this.shape = new Shape(size.scaling * BoardUtils.getScaling(), flippable.get() ? "greenFedToken" : "grayFedToken");
    flippable.addListener((o, oldValue, newValue) -> flip());
    ObservableList<Node> children = getChildren();
    children.add(shape);
    Label label = new Label(federationTile.getText());
    label.setTextFill(Color.WHITE);
    label.setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));
    children.add(label);
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
    REGULAR(1.0), TECH(0.8), MINI(0.7);

    private final double scaling;

    Size(double scaling) {
      this.scaling = scaling;
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
