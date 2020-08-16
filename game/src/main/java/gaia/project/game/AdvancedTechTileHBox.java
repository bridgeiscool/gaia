package gaia.project.game;

import gaia.project.game.model.AdvancedTechTile;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class AdvancedTechTileHBox extends HBox {
  private final AdvancedTechTile techTile;

  public AdvancedTechTileHBox(AdvancedTechTile techTile) {
    this.techTile = techTile;
    if (techTile.isAction()) {
      Action action = new Action(20.0, techTile.display(), "specialAction");
      action.setTaken(false);
      getChildren().add(action);
    } else {
      Label label = new Label(techTile.display());
      label.setTextFill(Color.WHITE);
      getChildren().add(label);
    }
    setPrefHeight(60);
    setPrefWidth(96);
    setAlignment(Pos.CENTER);
    getStyleClass().add("advTechTile");
  }

}
