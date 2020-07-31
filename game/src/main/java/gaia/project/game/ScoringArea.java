package gaia.project.game;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Preconditions;

import gaia.project.game.model.EndScoring;
import gaia.project.game.model.RoundScoringBonus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

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

  public ScoringArea(List<RoundScoringBonus> roundScoringBonuses, EndScoring endScoring1, EndScoring endScoring2) {
    Preconditions.checkArgument(roundScoringBonuses.size() == 6);

    FXMLLoader loader = new FXMLLoader(TechTracks.class.getResource("ScoringArea.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    round1.setText(roundScoringBonuses.get(0).getText());
    round2.setText(roundScoringBonuses.get(1).getText());
    round3.setText(roundScoringBonuses.get(2).getText());
    round4.setText(roundScoringBonuses.get(3).getText());
    round5.setText(roundScoringBonuses.get(4).getText());
    round6.setText(roundScoringBonuses.get(5).getText());

    this.endScoring1.setText(endScoring1.getDisplayText());
    this.endScoring2.setText(endScoring2.getDisplayText());

    // TODO: Bind to actual player facets
    p1Scoring1.setText("0");
    p1Scoring2.setText("0");

    p2Scoring1.setText("0");
    p2Scoring2.setText("0");

    p3Scoring1.setText("0");
    p3Scoring2.setText("0");
  }
}
