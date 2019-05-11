package gaia.project.game;

import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.converter.NumberStringConverter;

public class Ore extends StackPane {
  private final Label oreLabel;

  public Ore(Property<Number> bindTo) {
    Rectangle rectangle = new Rectangle(18.0, 18.0);
    rectangle.setFill(Color.WHITE);
    rectangle.getStyleClass().add("blackBorder");
    oreLabel = new Label();
    oreLabel.textProperty().bindBidirectional(bindTo, new NumberStringConverter());
    oreLabel.getStyleClass().add("blackLabel");

    getChildren().addAll(rectangle, oreLabel);
  }
}
