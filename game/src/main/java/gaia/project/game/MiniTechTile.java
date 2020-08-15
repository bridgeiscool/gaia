package gaia.project.game;

import gaia.project.game.model.Player;
import gaia.project.game.model.TechTile;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MiniTechTile extends HBox {
  private static final String NORMAL = "techTile";
  private static final String HIGHLIGHTED = "techTileHighlighted";
  private final TechTile techTile;

  public MiniTechTile(TechTile techTile) {
    this.techTile = techTile;
    Label label = new Label(techTile.display());
    label.setTextFill(Color.WHITE);
    label.setFont(Font.font(10));
    getChildren().add(label);
    setPrefHeight(36);
    setPrefWidth(54);
    setAlignment(Pos.CENTER);
    getStyleClass().add(NORMAL);
  }

  public void highlight(Player activePlayer, CallBack callBack) {
    if (techTile.isAction()) {
      getStyleClass().clear();
      getStyleClass().add(HIGHLIGHTED);
      this.setOnMouseClicked(me -> {
        techTile.onAction(activePlayer);
        callBack.call();
      });
    }
  }

  public void clearHighlighting() {
    setOnMouseClicked(null);
    getStyleClass().clear();
    getStyleClass().add(NORMAL);
  }
}
