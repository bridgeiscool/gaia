package gaia.project.game;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

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
import javafx.scene.layout.VBox;

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
      topBox.getChildren().add(new Label(player.getRace().getRaceName()));
      top.add(topBox, colIdx, 0, 2, 1);
      int rowIdx = 1;

      Set<Entry<String, Integer>> sortedEntries = new TreeSet<>(new HighestVPFirst());
      sortedEntries.addAll(player.getScoreLog().entrySet());

      for (Entry<String, Integer> scoreEntry : sortedEntries) {
        if (scoreEntry.getValue() != 0) {
          top.add(new Label(scoreEntry.getKey()), colIdx, rowIdx);
          top.add(new Label(format(scoreEntry.getValue())), colIdx + 1, rowIdx);
          ++rowIdx;
        }
      }

      bottom.add(new Label(format(player.getScore().get())), colIdx + 1, 0);

      colIdx = colIdx + 2;
    }
  }

  private static String format(int score) {
    return String.format("%d", score);
  }

  static class UnderlinedLabel extends VBox {
    UnderlinedLabel(String label) {
      getChildren().add(new Label(label));
      getStyleClass().add("bottom-line");
    }
  }

  static class MaybeBoldLabel extends Label {
    public MaybeBoldLabel(int value) {
      super(format(value));
      if (value > 120) {
        setStyle("-fx-font-weight: bold; -fx-font-size: 16pt;");
      }
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
