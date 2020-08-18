package gaia.project.game;

import java.util.function.Consumer;

import gaia.project.game.model.Player;
import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;

public class Action extends StackPane {
  private static final double ROOT_2 = Math.sqrt(2.0);
  private static final String TAKEN_ACTION = "takenAction";
  private static final String QIC_HIGHLIGHTED = "qicActionHighlighted";
  private static final String POWER_HIGHLIGHTED = "powerActionHighlighted";

  private final Octagon octagon;
  private final String originalStyle;
  private boolean taken;

  public Action(
      @NamedArg("sideLength") double sideLength,
      @NamedArg("text") String text,
      @NamedArg("octagonStyle") String octagonStyle) {
    ObservableList<Node> children = getChildren();
    this.octagon = new Octagon(sideLength);
    this.originalStyle = octagonStyle;
    children.add(octagon);
    Label label = new Label(text);
    label.getStyleClass().add("actionLabel");
    children.add(label);
  }

  public boolean isTaken() {
    return taken;
  }

  public void setTaken(boolean taken) {
    this.taken = taken;
    octagon.getStyleClass().clear();
    octagon.getStyleClass().add(taken ? TAKEN_ACTION : originalStyle);
  }

  public void tryHighlight(Player activePlayer, Consumer<Player> toExecute, CallBack callBack) {
    if (!taken) {
      octagon.getStyleClass().clear();
      octagon.getStyleClass().add(originalStyle.contains("qic") ? QIC_HIGHLIGHTED : POWER_HIGHLIGHTED);
      this.setOnMouseClicked(me -> {
        toExecute.accept(activePlayer);
        callBack.call();
      });
    }
  }

  public void clearHighlighting() {
    if (!taken) {
      octagon.getStyleClass().clear();
      octagon.getStyleClass().add(originalStyle);
      this.setOnMouseClicked(null);
    }
  }

  private static class Octagon extends Polygon {
    Octagon(double sideLength) {
      super(
          // TOP LEFT
          0.0,
          0.0,
          // TOP RIGHT
          sideLength,
          0.0,
          // RIGHT TOP
          sideLength + sideLength / ROOT_2,
          sideLength / ROOT_2,
          // RIGHT BOTTOM
          sideLength + sideLength / ROOT_2,
          sideLength + sideLength / ROOT_2,
          // BOTTOM RIGHT
          sideLength,
          sideLength + 2.0 * sideLength / ROOT_2,
          // BOTTOM LEFT
          0.0,
          sideLength + 2.0 * sideLength / ROOT_2,
          // LEFT BOTTOM
          -1.0 * sideLength / ROOT_2,
          sideLength + sideLength / ROOT_2,
          // LEFT TOP
          -1.0 * sideLength / ROOT_2,
          sideLength / ROOT_2);
    }
  }
}
