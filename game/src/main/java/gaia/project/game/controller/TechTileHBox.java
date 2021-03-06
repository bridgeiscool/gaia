package gaia.project.game.controller;

import java.util.Optional;

import gaia.project.game.board.BoardUtils;
import gaia.project.game.model.Player;
import gaia.project.game.model.TechTile;
import gaia.project.game.model.UpdatePlayer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TechTileHBox extends HBox {
  private static final double BASE_WIDTH = 108;
  private static final double BASE_HEIGHT = 72;
  private static final double BASE_FONT_SIZE = 18;
  private static final String NORMAL = "techTile";
  private static final String HIGHLIGHTED = "techTileHighlighted";
  private final TechTile techTile;

  public TechTileHBox(TechTile techTile) {
    this.techTile = techTile;
    if (techTile.isAction()) {
      SpecialAction action = new SpecialAction(techTile, techTile.display(), new SimpleBooleanProperty(false));
      getChildren().add(action);
    } else {
      Label label = new Label(techTile.display());
      label.setTextFill(Color.WHITE);
      getChildren().add(label);
    }
    setPrefHeight(BASE_HEIGHT * BoardUtils.getScaling());
    setPrefWidth(BASE_WIDTH * BoardUtils.getScaling());
    setAlignment(Pos.CENTER);
    getStyleClass().add("techTile");

    for (Node node : lookupAll(".label")) {
      ((Label) node).setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));
    }
  }

  public void highlight(Player activePlayer, CallBack callBack, Optional<UpdatePlayer> techBump) {
    if (!activePlayer.getTechTiles().contains(techTile) && !activePlayer.getCoveredTechTiles().contains(techTile)) {
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
