package gaia.project.game;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ActionChoiceDialog {
  private final GameController gameController;
  private final Stage dialogStage;

  @FXML
  private Label actionPrompt;
  @FXML
  private Button buildMine;
  @FXML
  private Button startGaiaProject;
  @FXML
  private Button upgradeBuilding;
  @FXML
  private Button federate;
  @FXML
  private Button advanceTech;
  @FXML
  private Button powerAction;
  @FXML
  private Button specialAction;
  @FXML
  private Button pass;

  public ActionChoiceDialog(GameController gameController) {
    this.gameController = gameController;

    try {
      FXMLLoader loader = new FXMLLoader(ActionChoiceDialog.class.getResource("ActionChoice.fxml"));
      loader.setController(this);
      Parent parent = (Parent) loader.load();
      dialogStage = new Stage();
      dialogStage.setScene(new Scene(parent));
      dialogStage.setResizable(false);
      dialogStage.initModality(Modality.APPLICATION_MODAL);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public void show() {
    dialogStage.setTitle(gameController.getGame().getActivePlayer() + "'s turn.");
    dialogStage.showAndWait();
  }

  @FXML
  private void buildMine() {
    System.out.println("Mine!");
  }

  @FXML
  private void gaiaProject() {

  }

  @FXML
  private void upgradeBuilding() {

  }

  @FXML
  private void federate() {

  }

  @FXML
  private void advanceTech() {

  }

  @FXML
  private void powerAction() {

  }

  @FXML
  private void specialAction() {

  }

  @FXML
  private void pass() {
    Game game = gameController.getGame();
    System.out.println("Player " + game.getActivePlayer() + " passed!");
    game.getPassedPlayers().add(game.getActivePlayer());
    dialogStage.hide();
    gameController.finishAction();
  }
}
