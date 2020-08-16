package gaia.project.game;

import java.util.Optional;

import gaia.project.game.model.Player;
import gaia.project.game.model.TechTile;
import gaia.project.game.model.UpdatePlayer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class TechTileHBox extends HBox {
  private static final String NORMAL = "techTile";
  private static final String HIGHLIGHTED = "techTileHighlighted";
  private final TechTile techTile;

  public TechTileHBox(TechTile techTile) {
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
    setPrefHeight(72);
    setPrefWidth(108);
    setAlignment(Pos.CENTER);
    getStyleClass().add("techTile");
  }

  public void highlight(Player activePlayer, CallBack callBack, Optional<UpdatePlayer> techBump) {
    if (!activePlayer.getTechTiles().contains(techTile)) {
      getStyleClass().clear();
      getStyleClass().add(HIGHLIGHTED);
      this.setOnMouseClicked(me -> {
        techTile.addTo(activePlayer);
        activePlayer.getTechTiles().add(techTile);
        if (techBump.isPresent()) {
          techBump.get().updatePlayer(activePlayer);
        }
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
