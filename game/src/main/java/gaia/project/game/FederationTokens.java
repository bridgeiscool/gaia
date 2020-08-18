package gaia.project.game;

import java.io.IOException;
import java.util.function.Consumer;

import gaia.project.game.model.FederationTile;
import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

public class FederationTokens extends HBox {
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

    this.research = new FederationTokenPane(FederationTile.RESEARCH, 1.0);
    this.credits = new FederationTokenPane(FederationTile.CREDITS, 1.0);
    this.ore = new FederationTokenPane(FederationTile.ORE, 1.0);
    this.pt = new FederationTokenPane(FederationTile.POWER, 1.0);
    this.qic = new FederationTokenPane(FederationTile.QIC, 1.0);
    this.vp = new FederationTokenPane(FederationTile.VP, 1.0);

    // Add tokens to UI
    knowledgeBox.getChildren().add(research);
    knowledgeCount.textProperty().bindBidirectional(game.getKnowledgeFederations(), new NumberStringConverter());
    coinsBox.getChildren().add(credits);
    coinsCount.textProperty().bindBidirectional(game.getCreditFederations(), new NumberStringConverter());
    oreBox.getChildren().add(ore);
    oreCount.textProperty().bindBidirectional(game.getOreFederations(), new NumberStringConverter());
    ptBox.getChildren().add(pt);
    ptCount.textProperty().bindBidirectional(game.getPtFederations(), new NumberStringConverter());
    qicBox.getChildren().add(qic);
    qicCount.textProperty().bindBidirectional(game.getQicFederations(), new NumberStringConverter());
    vpBox.getChildren().add(vp);
    vpCount.textProperty().bindBidirectional(game.getVpFederations(), new NumberStringConverter());
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
