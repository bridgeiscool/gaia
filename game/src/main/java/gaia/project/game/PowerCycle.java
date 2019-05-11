package gaia.project.game;

import javafx.beans.property.Property;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PowerCycle extends BorderPane {
  private PowerBin bin1;
  private PowerBin bin2;
  private PowerBin bin3;
  private PowerBin gaiaBin;

  public PowerCycle(
      Property<Number> gaia,
      Property<Number> bin1,
      Property<Number> bin2,
      Property<Number> bin3) {
    this.gaiaBin = PowerBin.green(gaia);
    this.bin1 = PowerBin.purple(bin1);
    this.bin2 = PowerBin.purple(bin2);
    this.bin3 = PowerBin.purple(bin3);

    setLeft(gaiaBin);
    VBox vbox = new VBox(this.bin2, this.bin1);
    vbox.setSpacing(5);
    setCenter(vbox);
    setRight(this.bin3);
  }
}
