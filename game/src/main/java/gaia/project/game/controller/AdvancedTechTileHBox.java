package gaia.project.game.controller;

import java.util.Map.Entry;

import gaia.project.game.board.BoardUtils;
import gaia.project.game.model.AdvancedTechTile;
import gaia.project.game.model.Player;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class AdvancedTechTileHBox extends HBox {
  private static final double BASE_HEIGHT = 60;
  private static final double BASE_WIDTH = 96;

  private static final String NORMAL = "advTechTile";
  private static final String HIGHLIGHTED = "advTechTileHighlighted";
  private final AdvancedTechTile techTile;
  private BooleanProperty taken;

  public AdvancedTechTileHBox(Entry<AdvancedTechTile, Boolean> entry) {
    this.techTile = entry.getKey();
    this.taken = new SimpleBooleanProperty(entry.getValue());
    if (!taken.get()) {
      if (techTile.isAction()) {
        SpecialAction action = new SpecialAction(techTile, techTile.display(), this.taken);
        getChildren().add(action);
      } else {
        Label label = new Label(techTile.display());
        label.setTextFill(Color.WHITE);
        getChildren().add(label);
      }
    }
    setPrefHeight(BASE_HEIGHT * BoardUtils.getScaling());
    setPrefWidth(BASE_WIDTH * BoardUtils.getScaling());
    setAlignment(Pos.CENTER);
    getStyleClass().add(NORMAL);
  }

  public AdvancedTechTile getTechTile() {
    return techTile;
  }

  public BooleanProperty getTaken() {
    return taken;
  }

  boolean isTaken() {
    return taken.get();
  }

  void setTaken() {
    this.taken.setValue(true);
    getChildren().clear();
  }

  public void highlight(Player activePlayer, CallBack callBack) {
    getStyleClass().clear();
    getStyleClass().add(HIGHLIGHTED);
    this.setOnMouseClicked(me -> {
      techTile.updatePlayer(activePlayer);
      activePlayer.getAdvTechTiles().add(techTile);
      activePlayer.exhaustFederationTile();
      setTaken();
      callBack.call();
    });
  }

  public void clearHighlighting() {
    setOnMouseClicked(null);
    getStyleClass().clear();
    getStyleClass().add(NORMAL);
  }
}
