package gaia.project.game;

import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.util.converter.NumberStringConverter;

public class Credits extends StackPane {
  private Label creditLabel;

  public Credits(Property<Number> bindTo) {
    Ellipse ellipse = new Ellipse(13.0, 9.0);
    ellipse.setFill(Color.YELLOW);
    ellipse.getStyleClass().add("blackBorder");
    creditLabel = new Label();
    creditLabel.textProperty().bindBidirectional(bindTo, new NumberStringConverter());
    creditLabel.getStyleClass().add("blackLabel");

    getChildren().addAll(ellipse, creditLabel);
  }
}
