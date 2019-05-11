package gaia.project.game;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ore extends StackPane {
  private int count;
  private final Label oreLabel;

  public Ore(int count) {
    this.count = count;
    Rectangle rectangle = new Rectangle(18.0, 18.0);
    rectangle.setFill(Color.WHITE);
    rectangle.getStyleClass().add("blackBorder");
    oreLabel = new Label(String.valueOf(count));
    oreLabel.getStyleClass().add("blackLabel");

    getChildren().addAll(rectangle, oreLabel);
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
    oreLabel.setText(String.valueOf(count));
  }

}
