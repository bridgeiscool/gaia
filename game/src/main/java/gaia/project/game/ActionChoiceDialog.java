package gaia.project.game;

import java.io.IOException;

import gaia.project.game.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class ActionChoiceDialog extends Dialog<Actions> {
  private static final ButtonType SUBMIT = new ButtonType("Submit", ButtonData.OK_DONE);
  private final GameController gameController;
  private Actions selectedAction;

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

  ActionChoiceDialog(GameController gameController) {
    this.gameController = gameController;

    try {
      FXMLLoader loader = new FXMLLoader(ActionChoiceDialog.class.getResource("ActionChoice.fxml"));
      loader.setController(this);
      getDialogPane().setContent((Node) loader.load());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    setResultConverter(button -> selectedAction);
    getDialogPane().getButtonTypes().add(SUBMIT);

    final Button submitButton = (Button) getDialogPane().lookupButton(SUBMIT);
    submitButton.addEventFilter(ActionEvent.ACTION, event -> {
      if (selectedAction == null) {
        event.consume();
      }
    });

    toggleButtonEnable();
  }

  private void toggleButtonEnable() {
    Player player = gameController.getGame().getPlayers().get(gameController.getGame().getActivePlayer());

    buildMine.setDisable(true);
    startGaiaProject.setDisable(true);
    upgradeBuilding.setDisable(true);
    federate.setDisable(true);
    advanceTech.setDisable(player.getResearch().getValue() < 4);
    powerAction.setDisable(true);
    specialAction.setDisable(true);
  }

  @FXML
  private void buildMine() {
    selectedAction = Actions.BUILD_MINE;
  }

  @FXML
  private void gaiaProject() {
    selectedAction = Actions.GAIA_PROJECT;
  }

  @FXML
  private void upgradeBuilding() {
    selectedAction = Actions.UPGRADE_BUILDING;
  }

  @FXML
  private void federate() {
    selectedAction = Actions.FEDERATE;
  }

  @FXML
  private void advanceTech() {
    selectedAction = Actions.ADVANCE_TECH;
    // gameController.activateTechTracks();
    // System.out.println("Closing dialog tech");
  }

  @FXML
  private void powerAction() {
    selectedAction = Actions.POWER_ACTION;
  }

  @FXML
  private void specialAction() {
    selectedAction = Actions.SPECIAL_ACTION;
  }

  @FXML
  private void pass() {
    selectedAction = Actions.PASS;
    // gameController.selectNewRoundBooster();
    // System.out.println("Closing dialog pass");

  }
}
