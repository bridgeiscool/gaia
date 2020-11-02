package gaia.project.game.controller;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import gaia.project.game.model.EndScoring;
import gaia.project.game.model.FederationTile;
import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ScoreDialog extends Dialog<Void> {
  private final String DIALOG_TITLE = "Scoring Results";

  @FXML
  Button okButton;
  @FXML
  GridPane top;
  @FXML
  GridPane bottom;

  public ScoreDialog(Game game) {
    try {
      FXMLLoader loader = new FXMLLoader(ScoreDialog.class.getResource("ScoreDialog.fxml"));
      loader.setController(this);
      getDialogPane().setContent((Node) loader.load());

      displayScores(game);
      setTitle(DIALOG_TITLE);
      getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonData.OK_DONE));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void displayScores(Game game) {
    int colIdx = 0;
    for (Player player : game.getPlayers().values()) {
      HBox topBox = new HBox(0);
      topBox.setAlignment(Pos.CENTER);
      topBox.getChildren().add(new BoldLabel(player.getRace().getRaceName()));
      top.add(topBox, colIdx, 0, 2, 1);
      int rowIdx = 1;

      addScores(colIdx, player, rowIdx);

      bottom.add(new BoldLabel(player.getScore().get()), colIdx + 1, 0);

      colIdx = colIdx + 2;
    }
  }

  private void addScores(int colIdx, Player player, int startingRowIdx) {
    int rowIdx = startingRowIdx;
    Map<String, Integer> scores = new HashMap<>(player.getScoreLog());

    // Starting score, leech, resources
    rowIdx = addSingleLine(GameController.STARTING_SCORE, scores, colIdx, rowIdx);
    rowIdx = addSingleLine(Player.LEECH, scores, colIdx, rowIdx);
    rowIdx = addSingleLine(Player.RESOURCES, scores, colIdx, rowIdx);

    // End game scoring
    rowIdx = addScoreGrouping("End Game Scoring", EndScoring.PREFIX + "(.+)", scores, colIdx, rowIdx, true);

    // Tech
    rowIdx = addSingleLine(Player.TECH_SCORING, scores, colIdx, rowIdx);

    // Federations
    rowIdx = addSingleLine(FederationTile.PREFIX, scores, colIdx, rowIdx);

    // Round Scoring
    rowIdx = addScoreGrouping("Round Scoring", "R[1-6] (.+)", scores, colIdx, rowIdx, false);

    // Round Boosters
    rowIdx = addScoreGrouping("Round Boosters", "RB " + "(.+)", scores, colIdx, rowIdx, true);

    // Tech Tiles
    rowIdx = addScoreGrouping("Tech Tiles", "(?:TT|ATT) " + "(.+)", scores, colIdx, rowIdx, true);

    Set<Entry<String, Integer>> sortedEntries = new TreeSet<>(new HighestVPFirst());
    sortedEntries.addAll(scores.entrySet());

    for (Entry<String, Integer> scoreEntry : sortedEntries) {
      if (scoreEntry.getValue() != 0) {
        top.add(new BoldLabel(scoreEntry.getKey()), colIdx, rowIdx);
        top.add(new BoldLabel(format(scoreEntry.getValue())), colIdx + 1, rowIdx);
        ++rowIdx;
      }
    }
  }

  private int addScoreGrouping(
      String title,
      String filterRegex,
      Map<String, Integer> scores,
      int colIdx,
      int startingRowIdx,
      boolean replaceRegex) {
    int rowIdx = startingRowIdx;
    TreeSet<String> keys = new TreeSet<>();
    scores.keySet().stream().filter(s -> s.matches(filterRegex)).forEach(keys::add);
    int total = scores.entrySet()
        .stream()
        .filter(e -> keys.contains(e.getKey()))
        .map(Entry::getValue)
        .collect(Collectors.summingInt(i -> i));
    top.add(new BoldLabel(title), colIdx, rowIdx);
    top.add(new BoldLabel(total), colIdx + 1, rowIdx);
    ++rowIdx;

    for (String key : keys) {
      Matcher matcher = Pattern.compile(filterRegex).matcher(key);
      matcher.find();
      top.add(new RegLabel("\t" + (replaceRegex ? matcher.group(1) : key)), colIdx, rowIdx);
      top.add(new RegLabel(scores.get(key)), colIdx + 1, rowIdx);
      ++rowIdx;
      scores.remove(key);
    }

    return rowIdx;
  }

  private int addSingleLine(String title, Map<String, Integer> scores, int colIdx, int rowIdx) {
    top.add(new BoldLabel(title), colIdx, rowIdx);
    top.add(new BoldLabel(scores.remove(title)), colIdx + 1, rowIdx);

    return rowIdx + 1;
  }

  private static String format(int score) {
    return String.format("%d", score);
  }

  static class RegLabel extends Label {
    public RegLabel(String text) {
      super(text);
      setStyle("-fx-font-size: 12pt;");
    }

    public RegLabel(int value) {
      this(format(value));
    }
  }

  static class BoldLabel extends Label {
    public BoldLabel(String text) {
      super(text);
      setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;");
    }

    public BoldLabel(int value) {
      this(format(value));
    }
  }

  private static class HighestVPFirst implements Comparator<Entry<String, Integer>> {

    @Override
    public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
      return o1.getValue().equals(o2.getValue())
          ? o1.getKey().compareTo(o2.getKey())
          : o2.getValue().compareTo(o1.getValue());
    }

  }
}
