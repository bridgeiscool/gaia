package gaia.project.game;

import java.io.IOException;

import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import javafx.beans.property.BooleanProperty;
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
  private final Game game;
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

  ActionChoiceDialog(Game game) {
    this.game = game;

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
    Player player = game.getPlayers().get(game.getActivePlayer());

    buildMine.setDisable(
        player.getMines().size() == 8 || player.getCredits().getValue() < 2 || player.getOre().intValue() < 1);
    startGaiaProject.setDisable(
        player.getAvailableGaiaformers().intValue() == player.getGaiaformers().size()
            || player.getBin1().intValue() + player.getBin2().intValue() + player.getBin3().intValue() < player
                .getGaiaformerCost());
    upgradeBuilding.setDisable(player.getOre().intValue() < 2 || player.getCredits().intValue() < 3);
    federate.setDisable(player.getExcessBuildingPower() < 7);
    advanceTech.setDisable(player.getResearch().getValue() < 4);
    powerAction.setDisable(
        player.getBin3().intValue() < game.getCheapestPowerAction()
            && player.getQic().intValue() < game.getCheapestQicAction());
    specialAction.setDisable(
        !(player.getRoundBooster().isAction() && !player.roundBoosterUsed())
            && !(player.getSpecialActions().values().stream().map(BooleanProperty::get).anyMatch(b -> !b)));
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
  }
}
