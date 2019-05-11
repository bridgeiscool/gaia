package gaia.project.game;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Credits extends StackPane {
  private int count;
  private Label creditLabel;

  public Credits(int count) {
    this.count = count;
    Ellipse ellipse = new Ellipse(13.0, 9.0);
    ellipse.setFill(Color.YELLOW);
    ellipse.getStyleClass().add("blackBorder");
    creditLabel = new Label(String.valueOf(count));
    creditLabel.getStyleClass().add("blackLabel");

    getChildren().addAll(ellipse, creditLabel);
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
    creditLabel.setText(String.valueOf(count));
  }

}
