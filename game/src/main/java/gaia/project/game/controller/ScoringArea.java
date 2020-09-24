package gaia.project.game.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import gaia.project.game.board.BoardUtils;
import gaia.project.game.model.EndScoring;
import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.Race;
import gaia.project.game.model.Round;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class ScoringArea extends BorderPane {
  private static final double BASE_GAP = 5;
  private static final double BASE_SCORING_WIDTH = 240;
  private static final double SCORING_TOP = 10;
  private static final double SCORING_SIDE = 8;

  private static final double BASE_FONT_SIZE = 18;

  @FXML
  private Label scoringLabel;

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

  // Turn markers
  @FXML
  private HBox p1;
  @FXML
  private HBox nextp1;
  @FXML
  private HBox p2;
  @FXML
  private HBox nextp2;
  @FXML
  private HBox p3;
  @FXML
  private HBox nextp3;

  // Containers
  @FXML
  private GridPane endScoringPane;
  @FXML
  private GridPane turnMarkerPane;

  private Map<PlayerEnum, TurnMarker> turnMarkers = new HashMap<>();
  private final List<HBox> thisTurn;
  private final List<HBox> nextTurn;

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

    // UI Sizing
    endScoringPane.setHgap(BASE_GAP * BoardUtils.getScaling());
    endScoringPane.setVgap(BASE_GAP * BoardUtils.getScaling());
    endScoringPane.setPrefWidth(BASE_SCORING_WIDTH * BoardUtils.getScaling());
    endScoringPane.setPadding(
        new Insets(
            SCORING_TOP * BoardUtils.getScaling(),
            SCORING_SIDE * BoardUtils.getScaling(),
            SCORING_TOP * BoardUtils.getScaling(),
            SCORING_SIDE * BoardUtils.getScaling()));

    turnMarkerPane.setHgap(BASE_GAP * BoardUtils.getScaling());
    turnMarkerPane.setVgap(BASE_GAP * BoardUtils.getScaling());
    turnMarkerPane.setPrefWidth(BASE_SCORING_WIDTH * BoardUtils.getScaling());
    turnMarkerPane.setPadding(
        new Insets(
            SCORING_TOP * BoardUtils.getScaling(),
            SCORING_SIDE * BoardUtils.getScaling(),
            SCORING_TOP * BoardUtils.getScaling(),
            SCORING_SIDE * BoardUtils.getScaling()));
    for (Node node : lookupAll(".label")) {
      ((Label) node).setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));
    }

    thisTurn = ImmutableList.of(p1, p2, p3);
    nextTurn = ImmutableList.of(nextp1, nextp2, nextp3);

    EndScoring scoring1 = game.getEndScoring1();
    EndScoring scoring2 = game.getEndScoring2();

    round1.setText(game.getRoundScoringBonuses().get(0).getText());
    round2.setText(game.getRoundScoringBonuses().get(1).getText());
    round3.setText(game.getRoundScoringBonuses().get(2).getText());
    round4.setText(game.getRoundScoringBonuses().get(3).getText());
    round5.setText(game.getRoundScoringBonuses().get(4).getText());
    round6.setText(game.getRoundScoringBonuses().get(5).getText());

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

    // Set up round highlighting
    setup.setTextFill(game.getCurrentRound().getValue() == Round.SETUP ? Color.LIME : Color.WHITE);
    round1.setTextFill(game.getCurrentRound().getValue() == Round.ROUND1 ? Color.LIME : Color.WHITE);
    round2.setTextFill(game.getCurrentRound().getValue() == Round.ROUND2 ? Color.LIME : Color.WHITE);
    round3.setTextFill(game.getCurrentRound().getValue() == Round.ROUND3 ? Color.LIME : Color.WHITE);
    round4.setTextFill(game.getCurrentRound().getValue() == Round.ROUND4 ? Color.LIME : Color.WHITE);
    round5.setTextFill(game.getCurrentRound().getValue() == Round.ROUND5 ? Color.LIME : Color.WHITE);
    round6.setTextFill(game.getCurrentRound().getValue() == Round.ROUND6 ? Color.LIME : Color.WHITE);
    game.getCurrentRound().addListener((o, oldValue, newValue) -> {
      switch (newValue) {
        case ROUND1:
          setup.setTextFill(Color.WHITE);
          round1.setTextFill(Color.LIME);
          break;
        case ROUND2:
          round1.setTextFill(Color.WHITE);
          round2.setTextFill(Color.LIME);
          break;
        case ROUND3:
          round2.setTextFill(Color.WHITE);
          round3.setTextFill(Color.LIME);
          break;
        case ROUND4:
          round3.setTextFill(Color.WHITE);
          round4.setTextFill(Color.LIME);
          break;
        case ROUND5:
          round4.setTextFill(Color.WHITE);
          round5.setTextFill(Color.LIME);
          break;
        case ROUND6:
          round5.setTextFill(Color.WHITE);
          round6.setTextFill(Color.LIME);
          break;
        case SETUP:
          // Do nothing it starts on this...
      }
    });

    for (Player player : game.getPlayers().values()) {
      turnMarkers.put(player.getPlayerEnum(), new TurnMarker(player.getRace()));
    }

    // Update for current state
    for (int i = 0; i < game.getCurrentPlayerOrder().size(); ++i) {
      thisTurn.get(i).getChildren().clear();
      thisTurn.get(i).getChildren().add(turnMarkers.get(game.getCurrentPlayerOrder().get(i)));
    }

    for (int i = 0; i < game.getPassedPlayers().size(); ++i) {
      nextTurn.get(i).getChildren().clear();
      nextTurn.get(i).getChildren().add(turnMarkers.get(game.getPassedPlayers().get(i)));
    }

    // Add listeners
    game.getCurrentPlayerOrder().addListener((ListChangeListener<PlayerEnum>) change -> {
      change.next();
      if (change.wasRemoved()) {
        change.getRemoved()
            .forEach(p -> thisTurn.stream().map(HBox::getChildren).anyMatch(l -> l.contains(turnMarkers.get(p))));
      }

      if (change.wasAdded()) {
        Preconditions.checkArgument(change.getAddedSize() == 3);
        for (int i = 0; i < thisTurn.size(); ++i) {
          thisTurn.get(i).getChildren().add(turnMarkers.get(change.getAddedSubList().get(i)));
        }
      }
    });

    game.getPassedPlayers().addListener((ListChangeListener<PlayerEnum>) change -> {
      change.next();
      if (change.wasRemoved()) {
        Preconditions.checkArgument(change.getRemovedSize() == 3);
        nextTurn.stream().forEach(hbox -> hbox.getChildren().clear());
      }

      if (change.wasAdded()) {
        nextTurn.get(change.getFrom())
            .getChildren()
            .add(turnMarkers.get(Iterables.getOnlyElement(change.getAddedSubList())));
      }
    });

    turnMarkers.get(game.getActivePlayer()).highlight();
    game.getActivePlayerProperty().addListener((o, oldValue, newValue) -> {
      turnMarkers.get(oldValue).clearHighlighting();
      turnMarkers.get(newValue).highlight();
    });
  }

  private static class TurnMarker extends Rectangle {
    private static final int BASE_SIZE = 15;

    TurnMarker(Race race) {
      super(BASE_SIZE * BoardUtils.getScaling(), BASE_SIZE * BoardUtils.getScaling(), race.getColor());
    }

    void highlight() {
      getStyleClass().add("turnMarker");
    }

    void clearHighlighting() {
      getStyleClass().clear();
    }
  }
}
