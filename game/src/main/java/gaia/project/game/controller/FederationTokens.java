package gaia.project.game.controller;

import java.io.IOException;
import java.util.function.Consumer;

import gaia.project.game.board.BoardUtils;
import gaia.project.game.model.FederationTile;
import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.converter.NumberStringConverter;

public class FederationTokens extends HBox {
  public static final double BASE_FONT_SIZE = 16;

  private static final String ZERO = "0";

  @FXML
  private VBox knowledgeBox;
  @FXML
  private Label knowledgeCount;
  @FXML
  private VBox coinsBox;
  @FXML
  private Label coinsCount;
  @FXML
  private VBox oreBox;
  @FXML
  private Label oreCount;
  @FXML
  private VBox ptBox;
  @FXML
  private Label ptCount;
  @FXML
  private VBox qicBox;
  @FXML
  private Label qicCount;
  @FXML
  private VBox vpBox;
  @FXML
  private Label vpCount;

  private final FederationTokenPane research;
  private final FederationTokenPane credits;
  private final FederationTokenPane ore;
  private final FederationTokenPane pt;
  private final FederationTokenPane qic;
  private final FederationTokenPane vp;

  public FederationTokens(Game game) {
    FXMLLoader loader = new FXMLLoader(FederationTokens.class.getResource("FederationTokens.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.research = FederationTokenPane.regular(FederationTile.RESEARCH);
    this.credits = FederationTokenPane.regular(FederationTile.CREDITS);
    this.ore = FederationTokenPane.regular(FederationTile.ORE);
    this.pt = FederationTokenPane.regular(FederationTile.POWER);
    this.qic = FederationTokenPane.regular(FederationTile.QIC);
    this.vp = FederationTokenPane.regular(FederationTile.VP);

    // UI setup
    knowledgeCount.setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));
    coinsCount.setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));
    oreCount.setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));
    ptCount.setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));
    qicCount.setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));
    vpCount.setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));

    // Add tokens to UI
    knowledgeBox.getChildren().add(research);
    knowledgeCount.textProperty().bindBidirectional(game.getKnowledgeFederations(), new NumberStringConverter());
    knowledgeCount.textFillProperty()
        .bind(
            Bindings.createObjectBinding(
                () -> intToColor(game.getKnowledgeFederations().get()),
                game.getKnowledgeFederations()));
    coinsBox.getChildren().add(credits);
    coinsCount.textProperty().bindBidirectional(game.getCreditFederations(), new NumberStringConverter());
    coinsCount.textFillProperty()
        .bind(
            Bindings
                .createObjectBinding(() -> intToColor(game.getCreditFederations().get()), game.getCreditFederations()));
    oreBox.getChildren().add(ore);
    oreCount.textProperty().bindBidirectional(game.getOreFederations(), new NumberStringConverter());
    oreCount.textFillProperty()
        .bind(Bindings.createObjectBinding(() -> intToColor(game.getOreFederations().get()), game.getOreFederations()));
    ptBox.getChildren().add(pt);
    ptCount.textProperty().bindBidirectional(game.getPtFederations(), new NumberStringConverter());
    ptCount.textFillProperty()
        .bind(Bindings.createObjectBinding(() -> intToColor(game.getPtFederations().get()), game.getPtFederations()));
    qicBox.getChildren().add(qic);
    qicCount.textProperty().bindBidirectional(game.getQicFederations(), new NumberStringConverter());
    qicCount.textFillProperty()
        .bind(Bindings.createObjectBinding(() -> intToColor(game.getQicFederations().get()), game.getQicFederations()));
    vpBox.getChildren().add(vp);
    vpCount.textProperty().bindBidirectional(game.getVpFederations(), new NumberStringConverter());
    vpCount.textFillProperty()
        .bind(Bindings.createObjectBinding(() -> intToColor(game.getVpFederations().get()), game.getVpFederations()));
  }

  private Color intToColor(int numLeft) {
    return numLeft == 0 ? Color.RED : Color.WHITE;
  }

  void highlight(Player activePlayer, Consumer<FederationTile> callback) {
    if (!knowledgeCount.getText().equals(ZERO)) {
      research.highlight(activePlayer, callback);
    }

    if (!coinsCount.getText().equals(ZERO)) {
      credits.highlight(activePlayer, callback);
    }

    if (!oreCount.getText().equals(ZERO)) {
      ore.highlight(activePlayer, callback);
    }

    if (!ptCount.getText().equals(ZERO)) {
      pt.highlight(activePlayer, callback);
    }

    if (!qicCount.getText().equals(ZERO)) {
      qic.highlight(activePlayer, callback);
    }

    if (!vpCount.getText().equals(ZERO)) {
      vp.highlight(activePlayer, callback);
    }
  }

  void clearHighlighting() {
    research.clearHighlighting();
    credits.clearHighlighting();
    ore.clearHighlighting();
    pt.clearHighlighting();
    qic.clearHighlighting();
    vp.clearHighlighting();
  }
}
