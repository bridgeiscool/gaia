package gaia.project.game;

import java.util.Optional;

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
  private static final double WIDTH = 62.0;
  private static final double HEIGHT = 175.0;

  private RoundBooster roundBooster;
  private Optional<PlayerEnum> currentPlayer = Optional.empty();
  private final BoosterRectangle rectangle;

  public RoundBoosterTile(RoundBooster roundBooster) {
    this.roundBooster = roundBooster;
    getStyleClass().add("roundBooster");
    ObservableList<Node> children = getChildren();
    this.rectangle = new BoosterRectangle();
    children.add(rectangle);
    VBox vbox = new VBox(60.0, new Label(roundBooster.getTopText()), new Label(roundBooster.getBottomText()));
    vbox.setAlignment(Pos.CENTER);
    children.add(vbox);
  }

  public boolean isTaken() {
    return currentPlayer.isPresent();
  }

  public void highlight(Player activePlayer, CallBack callBack) {
    if (!isTaken()) {
      rectangle.highlight();
      this.setOnMouseClicked(me -> {
        activePlayer.setRoundBooster(roundBooster);
        currentPlayer = Optional.of(activePlayer.getPlayerEnum());
        getChildren().add(new Circle(10, activePlayer.getRace().getColor()));
        callBack.call();
      });
    }
  }

  public void clearHighlighting() {
    setOnMouseClicked(null);
    rectangle.setNormalBorder();
  }

  public void clearToken(PlayerEnum activePlayer) {
    if (currentPlayer.isPresent() && currentPlayer.get() == activePlayer) {
      getChildren().remove(getChildren().size() - 1);
      currentPlayer = Optional.empty();
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
