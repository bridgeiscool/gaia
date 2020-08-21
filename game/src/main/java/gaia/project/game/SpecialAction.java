package gaia.project.game;

import java.io.Serializable;
import java.util.function.Consumer;

import gaia.project.game.model.Player;
import gaia.project.game.model.UpdatePlayer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;

public class SpecialAction extends StackPane {
  private static final int SIDE_LENGTH = 20;
  private static final double ROOT_2 = Math.sqrt(2.0);
  private static final String NORMAL = "specialAction";
  private static final String HIGHLIGHTED = "specialActionHighlighted";
  private static final String TAKEN_ACTION = "takenAction";

  private final Octagon octagon;
  private boolean taken;
  private final UpdatePlayer specialAction;

  public SpecialAction(UpdatePlayer specialAction, String display) {
    this(specialAction, display, false);
  }

  public SpecialAction(UpdatePlayer specialAction, String display, boolean taken) {
    ObservableList<Node> children = getChildren();
    this.octagon = new Octagon();
    this.specialAction = specialAction;
    setTaken(taken);
    children.add(octagon);
    Label label = new Label(display);
    label.getStyleClass().add("actionLabel");
    children.add(label);
  }

  public boolean isTaken() {
    return taken;
  }

  public void setTaken(boolean taken) {
    this.taken = taken;
    octagon.getStyleClass().clear();
    octagon.getStyleClass().add(taken ? TAKEN_ACTION : NORMAL);
  }

  public void tryHighlight(Player activePlayer, Consumer<Serializable> callBack) {
    if (!taken) {
      octagon.getStyleClass().clear();
      octagon.getStyleClass().add(HIGHLIGHTED);
      this.setOnMouseClicked(me -> {
        setTaken(true);
        activePlayer.takeSpecialAction(specialAction);
        callBack.accept(specialAction);
      });
    }
  }

  public void clearHighlighting() {
    if (!taken) {
      octagon.getStyleClass().clear();
      octagon.getStyleClass().add(NORMAL);
      this.setOnMouseClicked(null);
    }
  }

  private static class Octagon extends Polygon {
    Octagon() {
      super(
          // TOP LEFT
          0.0,
          0.0,
          // TOP RIGHT
          SIDE_LENGTH,
          0.0,
          // RIGHT TOP
          SIDE_LENGTH + SIDE_LENGTH / ROOT_2,
          SIDE_LENGTH / ROOT_2,
          // RIGHT BOTTOM
          SIDE_LENGTH + SIDE_LENGTH / ROOT_2,
          SIDE_LENGTH + SIDE_LENGTH / ROOT_2,
          // BOTTOM RIGHT
          SIDE_LENGTH,
          SIDE_LENGTH + 2.0 * SIDE_LENGTH / ROOT_2,
          // BOTTOM LEFT
          0.0,
          SIDE_LENGTH + 2.0 * SIDE_LENGTH / ROOT_2,
          // LEFT BOTTOM
          -1.0 * SIDE_LENGTH / ROOT_2,
          SIDE_LENGTH + SIDE_LENGTH / ROOT_2,
          // LEFT TOP
          -1.0 * SIDE_LENGTH / ROOT_2,
          SIDE_LENGTH / ROOT_2);
    }
  }
}