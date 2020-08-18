package gaia.project.game;

import java.io.Serializable;

import javax.annotation.Nullable;

import gaia.project.game.model.Player;
import gaia.project.game.model.TechTile;
import javafx.collections.MapChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MiniTechTile extends HBox {
  private static final String NORMAL = "techTile";
  private static final String HIGHLIGHTED = "techTileHighlighted";
  private final TechTile techTile;
  @Nullable
  private Action action;

  public MiniTechTile(Player player, TechTile techTile) {
    this.techTile = techTile;
    if (techTile.isAction()) {
      this.action = new Action(20.0, techTile.display(), "specialAction");
      action.setTaken(player.getSpecialActions().get(techTile));
      getChildren().add(action);

      // Bind state UI to used status
      player.getSpecialActions().addListener((MapChangeListener<? super Serializable, ? super Boolean>) listener -> {
        if (listener.getKey() == techTile) {
          action.setTaken(listener.getValueAdded());
        }
      });
    } else {
      Label label = new Label(techTile.display());
      label.setTextFill(Color.WHITE);
      label.setFont(Font.font(10));
      getChildren().add(label);
    }
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
        this.action.setTaken(true);
        callBack.call();
      });
    }
  }

  public void clearHighlighting() {
    setOnMouseClicked(null);
    getStyleClass().clear();
    getStyleClass().add(NORMAL);
  }

  public boolean isAction() {
    return action != null;
  }

  public boolean isTaken() {
    return action.isTaken();
  }
}
