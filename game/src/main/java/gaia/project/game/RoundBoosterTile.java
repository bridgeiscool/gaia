package gaia.project.game;

import java.util.Optional;

import javax.annotation.Nullable;

import gaia.project.game.model.Player;
import gaia.project.game.model.RoundBooster;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
  private Optional<Player> currentPlayer = Optional.empty();
  private final BoosterRectangle rectangle;
  @Nullable
  private EventHandler<? super MouseEvent> currentEventHandler;

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

  public void highlight(Player activePlayer, EventHandler<MouseEvent> topLevel) {
    if (!isTaken()) {
      rectangle.highlight();
      this.currentEventHandler = me -> {
        activePlayer.getRoundBooster().setValue(roundBooster);
        currentPlayer = Optional.of(activePlayer);
        getChildren().add(new Circle(10, activePlayer.getRace().getColor()));
        topLevel.handle(me);
      };
      this.setOnMouseClicked(currentEventHandler);
    }
  }

  public void clearHighlighting() {
    rectangle.setNormalBorder();
    if (currentEventHandler != null) {
      removeEventHandler(MouseEvent.MOUSE_CLICKED, currentEventHandler);
      currentEventHandler = null;
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