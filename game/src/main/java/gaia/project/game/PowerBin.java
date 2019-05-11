package gaia.project.game;

import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.util.converter.NumberStringConverter;

public class PowerBin extends StackPane {
  private final Label valueLabel;

  public static PowerBin purple(Property<Number> bindTo) {
    return new PowerBin(GuiUtils.POWERBINX, GuiUtils.POWERBINY, Color.PURPLE, bindTo);
  }

  public static PowerBin green(Property<Number> bindTo) {
    return new PowerBin(GuiUtils.POWERBINX, GuiUtils.POWERBINY, Color.GREEN, bindTo);
  }

  private PowerBin(double radiusX, double radiusY, Color fill, Property<Number> bindTo) {
    Ellipse ellipse = new Ellipse(radiusX, radiusY);
    ellipse.setFill(fill);
    valueLabel = new Label();
    valueLabel.textProperty().bindBidirectional(bindTo, new NumberStringConverter());

    getChildren().addAll(ellipse, valueLabel);
  }
}
