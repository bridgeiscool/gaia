package gaia.project.game;

import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.util.converter.NumberStringConverter;

public class Research extends StackPane {
  private Label researchLabel;

  public Research(Property<Number> bindTo) {
    Ellipse ellipse = new Ellipse(14.0, 9.0);
    ellipse.setFill(Color.CYAN);
    ellipse.getStyleClass().add("blackBorder");
    researchLabel = new Label();
    researchLabel.textProperty().bindBidirectional(bindTo, new NumberStringConverter());
    researchLabel.getStyleClass().add("blackLabel");

    getChildren().addAll(ellipse, researchLabel);
  }
}
