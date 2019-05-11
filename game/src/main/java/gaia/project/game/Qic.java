package gaia.project.game;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Qic extends StackPane {
  private int count;
  private final Label qicLabel;

  public Qic(int count) {
    this.count = count;
    Rectangle rectangle = new Rectangle(18.0, 18.0);
    rectangle.setFill(Color.GREEN);
    rectangle.getStyleClass().add("blackBorder");
    qicLabel = new Label(String.valueOf(count));
    qicLabel.getStyleClass().add("blackLabel");

    getChildren().addAll(rectangle, qicLabel);
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
    qicLabel.setText(String.valueOf(count));
  }

}
