package gaia.project.game;

import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Action extends StackPane {
  private static final double ROOT_2 = Math.sqrt(2.0);

  public Action(
      @NamedArg("sideLength") double sideLength,
      @NamedArg("text") String text,
      @NamedArg("octagonStyle") String octagonStyle) {
    ObservableList<Node> children = getChildren();
    children.add(new Octagon(sideLength, octagonStyle));
    Label label = new Label(text);
    label.setTextFill(Color.WHITE);
    children.add(label);
  }

  private static class Octagon extends Polygon {
    Octagon(double sideLength, String octagonStyle) {
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
      this.getStyleClass().add(octagonStyle);
    }
  }
}
