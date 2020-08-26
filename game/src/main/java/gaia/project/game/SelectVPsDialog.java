package gaia.project.game;

import java.io.IOException;

import gaia.project.game.model.Game;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.Util;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;

public class SelectVPsDialog extends Dialog<Void> {
  private static final ButtonType DONE = new ButtonType("OK", ButtonData.OK_DONE);

  @FXML
  private Label p1Vps;
  @FXML
  private Label p2Vps;
  @FXML
  private Label p3Vps;

  private IntegerProperty p1Score;
  private IntegerProperty p2Score;
  private IntegerProperty p3Score;

  public SelectVPsDialog(Game game) {
    try {
      FXMLLoader loader = new FXMLLoader(SelectVPsDialog.class.getResource("SelectVPs.fxml"));
      loader.setController(this);
      getDialogPane().setContent((Node) loader.load());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    getDialogPane().getButtonTypes().add(DONE);

    p1Score = game.getPlayers().get(PlayerEnum.PLAYER1).getScore();
    p2Score = game.getPlayers().get(PlayerEnum.PLAYER2).getScore();
    p3Score = game.getPlayers().get(PlayerEnum.PLAYER3).getScore();

    p1Vps.textProperty()
        .bindBidirectional(game.getPlayers().get(PlayerEnum.PLAYER1).getScore(), new NumberStringConverter());
    p2Vps.textProperty()
        .bindBidirectional(game.getPlayers().get(PlayerEnum.PLAYER2).getScore(), new NumberStringConverter());
    p3Vps.textProperty()
        .bindBidirectional(game.getPlayers().get(PlayerEnum.PLAYER3).getScore(), new NumberStringConverter());

  }

  @FXML
  private void addP1VP() {
    Util.plus(p1Score, 1);
  }

  @FXML
  private void subtractP1VP() {
    Util.minus(p1Score, 1);
  }

  @FXML
  private void addP2VP() {
    Util.plus(p2Score, 1);
  }

  @FXML
  private void subtractP2VP() {
    Util.minus(p2Score, 1);
  }

  @FXML
  private void addP3VP() {
    Util.plus(p3Score, 1);
  }

  @FXML
  private void subtractP3VP() {
    Util.minus(p3Score, 1);
  }

}
