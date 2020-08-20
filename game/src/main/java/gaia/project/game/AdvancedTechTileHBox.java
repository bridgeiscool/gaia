package gaia.project.game;

import java.util.Map.Entry;

import gaia.project.game.model.AdvancedTechTile;
import gaia.project.game.model.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class AdvancedTechTileHBox extends HBox {
  private static final String NORMAL = "advTechTile";
  private static final String HIGHLIGHTED = "advTechTileHighlighted";
  private final AdvancedTechTile techTile;
  private boolean taken;

  public AdvancedTechTileHBox(Entry<AdvancedTechTile, Boolean> entry) {
    this.techTile = entry.getKey();
    this.taken = entry.getValue();
    if (!taken) {
      if (techTile.isAction()) {
        Action action = new Action(20.0, techTile.display(), "specialAction");
        action.setTaken(false);
        getChildren().add(action);
      } else {
        Label label = new Label(techTile.display());
        label.setTextFill(Color.WHITE);
        getChildren().add(label);
      }
    }
    setPrefHeight(60);
    setPrefWidth(96);
    setAlignment(Pos.CENTER);
    getStyleClass().add(NORMAL);
  }

  boolean isTaken() {
    return taken;
  }

  void setTaken() {
    this.taken = true;
    getChildren().clear();
  }

  public void highlight(Player activePlayer, CallBack callBack) {
    getStyleClass().clear();
    getStyleClass().add(HIGHLIGHTED);
    this.setOnMouseClicked(me -> {
      activePlayer.getAdvTechTiles().add(techTile);
      techTile.updatePlayer(activePlayer);
      activePlayer.exhaustFederationTile();
      callBack.call();
    });
  }

  public void clearHighlighting() {
    setOnMouseClicked(null);
    getStyleClass().clear();
    getStyleClass().add(NORMAL);
  }
}
