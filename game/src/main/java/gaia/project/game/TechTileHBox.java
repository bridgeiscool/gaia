package gaia.project.game;

import gaia.project.game.model.TechTile;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class TechTileHBox extends HBox {
  private final TechTile techTile;

  public TechTileHBox(TechTile techTile) {
    this.techTile = techTile;
    Label label = new Label(techTile.display());
    label.setTextFill(Color.WHITE);
    getChildren().add(label);
    setPrefHeight(72);
    setPrefWidth(108);
    setAlignment(Pos.CENTER);
    getStyleClass().add("techTile");
  }
}
