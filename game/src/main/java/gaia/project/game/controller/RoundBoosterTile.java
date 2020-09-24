package gaia.project.game.controller;

import java.util.Optional;

import javax.annotation.Nullable;

import gaia.project.game.board.BoardUtils;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.RoundBooster;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

public class RoundBoosterTile extends StackPane {
  private static final double BASE_WIDTH = 60.0;
  private static final double BASE_HEIGHT = 175.0;

  private static final double BASE_FONT_SIZE = 18.0;

  private RoundBooster roundBooster;
  private Optional<PlayerEnum> currentPlayer = Optional.empty();
  private final BoosterRectangle rectangle;
  @Nullable
  private SpecialAction action;

  public RoundBoosterTile(RoundBooster roundBooster) {
    this.roundBooster = roundBooster;
    getStyleClass().add("roundBooster");
    ObservableList<Node> children = getChildren();
    this.rectangle = new BoosterRectangle();
    children.add(rectangle);
    VBox vbox = new VBox(
        BASE_WIDTH * BoardUtils.getScaling(),
        roundBooster.isAction() ? getSpecialAction(roundBooster) : new Label(roundBooster.getTopText()),
        new Label(roundBooster.getBottomText()));
    vbox.setAlignment(Pos.CENTER);
    children.add(vbox);

    for (Node node : lookupAll(".label")) {
      ((Label) node).setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));
    }
  }

  private SpecialAction getSpecialAction(RoundBooster roundBooster) {
    this.action = new SpecialAction(roundBooster, roundBooster.getTopText(), new SimpleBooleanProperty(false));
    return action;
  }

  public boolean isTaken() {
    return currentPlayer.isPresent();
  }

  public void highlight(Player activePlayer, CallBack callBack) {
    if (!isTaken()) {
      rectangle.highlight();
      this.setOnMouseClicked(me -> {
        activePlayer.setRoundBooster(roundBooster);
        addToken(activePlayer);
        callBack.call();
      });
    }
  }

  public RoundBooster getRoundBooster() {
    return roundBooster;
  }

  public void clearHighlighting() {
    setOnMouseClicked(null);
    rectangle.setNormalBorder();
  }

  public void addToken(Player player) {
    currentPlayer = Optional.of(player.getPlayerEnum());
    getChildren().add(new Circle(10, player.getRace().getColor()));
    if (player.roundBoosterUsed() && action != null) {
      action.setTaken(true);
    }
  }

  public void clearToken(PlayerEnum activePlayer) {
    if (currentPlayer.isPresent() && currentPlayer.get() == activePlayer) {
      getChildren().remove(getChildren().size() - 1);
      currentPlayer = Optional.empty();
    }
  }

  public void highlightSpecialAction(Player activePlayer, CallBack callback) {
    if (currentPlayer.isPresent() && currentPlayer.get() == activePlayer.getPlayerEnum()) {
      rectangle.highlight();
      this.setOnMouseClicked(me -> {
        roundBooster.onAction(activePlayer);
        activePlayer.setRoundBoosterUsed();
        action.setTaken(true);
        callback.call();
      });
    }
  }

  public void clearAction() {
    if (action != null) {
      action.setTaken(false);
    }
  }

  private static class BoosterRectangle extends Rectangle {
    BoosterRectangle() {
      super(BASE_WIDTH * BoardUtils.getScaling(), BASE_HEIGHT * BoardUtils.getScaling(), Color.ANTIQUEWHITE);
      setNormalBorder();
    }

    void highlight() {
      setStroke(Color.LIME);
      setStrokeWidth(3);
      setStrokeType(StrokeType.INSIDE);
    }

    void setNormalBorder() {
      setStroke(Color.BLACK);
      setStrokeWidth(1);
      setStrokeType(StrokeType.INSIDE);
    }
  }
}
