package gaia.project.game;

import java.io.IOException;
import java.util.Map.Entry;

import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScoreDialog {
  private final String DIALOG_TITLE = "Scoring Results";
  private final Stage dialogStage;

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
      Parent parent = (Parent) loader.load();
      dialogStage = new Stage();
      dialogStage.setScene(new Scene(parent));
      dialogStage.setResizable(false);
      dialogStage.setTitle(DIALOG_TITLE);
      dialogStage.initModality(Modality.APPLICATION_MODAL);

      displayScores(game);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void displayScores(Game game) {
    int colIdx = 0;
    for (Player player : game.getPlayers().values()) {
      top.add(new Label(player.getRace().getRaceName()), colIdx, 0, 2, 1);
      int rowIdx = 1;
      for (Entry<String, Integer> scoreEntry : player.getScoreLog().entrySet()) {
        top.add(new Label(scoreEntry.getKey()), colIdx, rowIdx);
        top.add(new Label(scoreEntry.getValue().toString()), colIdx + 1, rowIdx);
        ++rowIdx;
      }

      colIdx = colIdx + 2;
    }
  }

  private static String format(int score) {
    return String.format("%d", score);
  }

  void show() {
    dialogStage.showAndWait();
  }

  @FXML
  private void onClick() {
    dialogStage.close();
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
}
