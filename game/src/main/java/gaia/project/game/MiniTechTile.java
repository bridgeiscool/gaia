package gaia.project.game;

import java.util.function.Consumer;

import javax.annotation.Nullable;

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
  @Nullable
  private Action action;

  public MiniTechTile(Player player, TechTile techTile) {
    this.techTile = techTile;
    if (techTile.isAction()) {
      this.action = new Action(20.0, techTile.display(), "specialAction");
      action.setTaken(player.getSpecialActions().get(techTile).get());
      getChildren().add(action);

      // Bind state UI to used status
      player.getSpecialActions().get(techTile).addListener((o, oldValue, newValue) -> action.setTaken(newValue));
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
