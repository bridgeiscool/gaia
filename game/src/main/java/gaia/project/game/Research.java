package gaia.project.game;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Research extends StackPane {
  private int count;
  private Label researchLabel;

  public Research(int count) {
    this.count = count;
    Ellipse ellipse = new Ellipse(14.0, 9.0);
    ellipse.setFill(Color.CYAN);
    ellipse.getStyleClass().add("blackBorder");
    researchLabel = new Label(String.valueOf(count));
    researchLabel.getStyleClass().add("blackLabel");

    getChildren().addAll(ellipse, researchLabel);
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
    researchLabel.setText(String.valueOf(count));
  }

}
