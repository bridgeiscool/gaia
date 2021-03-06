package gaia.project.game.controller;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import gaia.project.game.board.BoardUtils;
import gaia.project.game.model.Player;
import gaia.project.game.model.TechTile;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MiniTechTile extends HBox {
  private static final double BASE_HEIGHT = 36;
  private static final double BASE_WIDTH = 54;
  private static final double BASE_FONT_SIZE = 12;

  private static final String NORMAL = "techTile";
  private static final String HIGHLIGHTED = "techTileHighlighted";
  private final TechTile techTile;
  @Nullable
  private SpecialAction action;

  public MiniTechTile(Player player, TechTile techTile) {
    this.techTile = techTile;
    if (techTile.isAction()) {
      this.action = new SpecialAction(techTile, techTile.display(), player.getSpecialActions().get(techTile));
      action.setTaken(player.getSpecialActions().get(techTile).get());
      getChildren().add(action);
    } else {
      Label label = new Label(techTile.display());
      label.setTextFill(Color.WHITE);
      label.setFont(Font.font(BASE_FONT_SIZE * BoardUtils.getScaling()));
      getChildren().add(label);
    }
    setPrefHeight(BASE_HEIGHT * BoardUtils.getScaling());
    setPrefWidth(BASE_WIDTH * BoardUtils.getScaling());
    setAlignment(Pos.CENTER);
    getStyleClass().add(NORMAL);
  }

  public TechTile getTechTile() {
    return techTile;
  }

  public void highlight(Player activePlayer, Consumer<Enum<?>> callBack, boolean takingAction) {
    getStyleClass().clear();
    getStyleClass().add(HIGHLIGHTED);
    this.setOnMouseClicked(me -> {
      if (takingAction) {
        techTile.onAction(activePlayer);
        this.action.setTaken(true);
      }
      callBack.accept(techTile);
    });
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
