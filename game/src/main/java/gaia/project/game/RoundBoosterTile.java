package gaia.project.game;

import java.util.Optional;

import javax.annotation.Nullable;

import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.RoundBooster;
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

public class RoundBoosterTile extends StackPane {
  private static final double WIDTH = 60.0;
  private static final double HEIGHT = 175.0;

  private RoundBooster roundBooster;
  private Optional<PlayerEnum> currentPlayer = Optional.empty();
  private final BoosterRectangle rectangle;
  @Nullable
  private Action action;

  public RoundBoosterTile(RoundBooster roundBooster) {
    this.roundBooster = roundBooster;
    getStyleClass().add("roundBooster");
    ObservableList<Node> children = getChildren();
    this.rectangle = new BoosterRectangle();
    children.add(rectangle);
    VBox vbox = new VBox(
        60.0,
        roundBooster.isAction() ? getSpecialAction(roundBooster) : new Label(roundBooster.getTopText()),
        new Label(roundBooster.getBottomText()));
    vbox.setAlignment(Pos.CENTER);
    children.add(vbox);
  }

  private Action getSpecialAction(RoundBooster roundBooster) {
    this.action = new Action(20.0, roundBooster.getTopText(), "specialAction");
    action.setTaken(false);
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
      super(WIDTH, HEIGHT, Color.ANTIQUEWHITE);
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
