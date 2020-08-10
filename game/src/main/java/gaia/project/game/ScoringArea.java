package gaia.project.game;

import java.io.IOException;

import com.google.common.base.Preconditions;

import gaia.project.game.model.EndScoring;
import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class ScoringArea extends BorderPane {
  @FXML
  private Label round6;
  @FXML
  private Label round5;
  @FXML
  private Label round4;
  @FXML
  private Label round3;
  @FXML
  private Label round2;
  @FXML
  private Label round1;
  @FXML
  private Label setup;

  @FXML
  private Label endScoring1;
  @FXML
  private Label endScoring2;
  @FXML
  private Label p1Scoring1;
  @FXML
  private Label p1Scoring2;
  @FXML
  private Label p2Scoring1;
  @FXML
  private Label p2Scoring2;
  @FXML
  private Label p3Scoring1;
  @FXML
  private Label p3Scoring2;

  public ScoringArea(Game game) {
    Preconditions.checkArgument(game.getRoundScoringBonuses().size() == 6);

    FXMLLoader loader = new FXMLLoader(TechTracks.class.getResource("ScoringArea.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    EndScoring scoring1 = game.getEndScoring1();
    EndScoring scoring2 = game.getEndScoring2();

    round1.setText(game.getRoundScoringBonuses().get(0).getText());
    round2.setText(game.getRoundScoringBonuses().get(1).getText());
    round3.setText(game.getRoundScoringBonuses().get(2).getText());
    round4.setText(game.getRoundScoringBonuses().get(3).getText());
    round5.setText(game.getRoundScoringBonuses().get(4).getText());
    round6.setText(game.getRoundScoringBonuses().get(5).getText());

    // Initialize highlighted round, which may not be SETUP when reload happens
    switch (game.getCurrentRound().getValue()) {
      case SETUP:
        setup.setTextFill(Color.WHITE);
        break;
      case ROUND1:
        round1.setTextFill(Color.WHITE);
        break;
      case ROUND2:
        round2.setTextFill(Color.WHITE);
        break;
      case ROUND3:
        round3.setTextFill(Color.WHITE);
        break;
      case ROUND4:
        round4.setTextFill(Color.WHITE);
        break;
      case ROUND5:
        round5.setTextFill(Color.WHITE);
        break;
      case ROUND6:
        round6.setTextFill(Color.WHITE);
        break;
    }

    this.endScoring1.setText(scoring1.getDisplayText());
    this.endScoring2.setText(scoring2.getDisplayText());

    Player player1 = game.getPlayers().get(PlayerEnum.PLAYER1);
    scoring1.bindToPlayer(player1, p1Scoring1);
    p1Scoring1.setTextFill(player1.getRace().getColor());
    scoring2.bindToPlayer(game.getPlayers().get(PlayerEnum.PLAYER1), p1Scoring2);
    p1Scoring2.setTextFill(player1.getRace().getColor());

    Player player2 = game.getPlayers().get(PlayerEnum.PLAYER2);
    scoring1.bindToPlayer(player2, p2Scoring1);
    p2Scoring1.setTextFill(player2.getRace().getColor());
    scoring2.bindToPlayer(player2, p2Scoring2);
    p2Scoring2.setTextFill(player2.getRace().getColor());

    Player player3 = game.getPlayers().get(PlayerEnum.PLAYER3);
    scoring1.bindToPlayer(player3, p3Scoring1);
    p3Scoring1.setTextFill(player3.getRace().getColor());
    scoring2.bindToPlayer(player3, p3Scoring2);
    p3Scoring2.setTextFill(player3.getRace().getColor());

    game.getCurrentRound().addListener((o, oldValue, newValue) -> {
      switch (newValue) {
        case ROUND1:
          setup.setTextFill(Color.BLACK);
          round1.setTextFill(Color.WHITE);
          break;
        case ROUND2:
          round1.setTextFill(Color.BLACK);
          round2.setTextFill(Color.WHITE);
          break;
        case ROUND3:
          round2.setTextFill(Color.BLACK);
          round3.setTextFill(Color.WHITE);
          break;
        case ROUND4:
          round3.setTextFill(Color.BLACK);
          round4.setTextFill(Color.WHITE);
          break;
        case ROUND5:
          round4.setTextFill(Color.BLACK);
          round5.setTextFill(Color.WHITE);
          break;
        case ROUND6:
          round5.setTextFill(Color.BLACK);
          round6.setTextFill(Color.WHITE);
          break;
        case SETUP:
          // Do nothing it starts on this...
      }
    });
  }
}
