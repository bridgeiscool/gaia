package gaia.project.game;

import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.converter.NumberStringConverter;

public class Qic extends StackPane {
  private final Label qicLabel;

  public Qic(Property<Number> bindTo) {
    Rectangle rectangle = new Rectangle(18.0, 18.0);
    rectangle.setFill(Color.GREEN);
    rectangle.getStyleClass().add("blackBorder");
    qicLabel = new Label();
    qicLabel.textProperty().bindBidirectional(bindTo, new NumberStringConverter());
    qicLabel.getStyleClass().add("blackLabel");

    getChildren().addAll(rectangle, qicLabel);
  }
}
