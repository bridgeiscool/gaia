package gaia.project.game;

import java.io.IOException;

import gaia.project.game.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class ConversionDialog extends Dialog<Void> {
  private static final ButtonType DONE = new ButtonType("Done", ButtonData.OK_DONE);
  private final Player activePlayer;

  ConversionDialog(Player activePlayer) {
    this.activePlayer = activePlayer;

    try {
      FXMLLoader loader = new FXMLLoader(ConversionDialog.class.getResource("Conversions.fxml"));
      loader.setController(this);
      getDialogPane().setContent((Node) loader.load());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    setResultConverter(button -> null);
    getDialogPane().getButtonTypes().add(DONE);
  }

  @FXML
  private void qicToNav() {
    if (activePlayer.getQic().getValue() > 0) {
      activePlayer.getQic().setValue(activePlayer.getQic().getValue() - 1);
      activePlayer.getTempNavRange().setValue(activePlayer.getTempNavRange().getValue() + 2);
    }
  }

  @FXML
  private void powerToQic() {
    if (activePlayer.getBin3().getValue() > 3) {
      activePlayer.getBin3().setValue(activePlayer.getBin3().getValue() - 4);
      activePlayer.getQic().setValue(activePlayer.getQic().getValue() + 1);
    }
  }

  @FXML
  private void qicToOre() {
    if (activePlayer.getQic().getValue() > 0) {
      activePlayer.getQic().setValue(activePlayer.getQic().getValue() - 1);
      activePlayer.getOre().setValue(activePlayer.getOre().getValue() + 1);
    }
  }

  @FXML
  private void powerToOre() {
    if (activePlayer.getBin3().getValue() > 2) {
      activePlayer.getBin3().setValue(activePlayer.getBin3().getValue() - 3);
      activePlayer.getOre().setValue(activePlayer.getOre().getValue() + 1);
    }
  }

  @FXML
  private void powerToKnowledge() {
    if (activePlayer.getBin3().getValue() > 3) {
      activePlayer.getBin3().setValue(activePlayer.getBin3().getValue() - 4);
      activePlayer.getResearch().setValue(activePlayer.getResearch().getValue() + 1);
    }
  }

  @FXML
  private void knowledgeToCredits() {
    if (activePlayer.getResearch().getValue() > 0) {
      activePlayer.getResearch().setValue(activePlayer.getResearch().getValue() - 1);
      activePlayer.getCredits().setValue(activePlayer.getCredits().getValue() + 1);
    }
  }

  @FXML
  private void powerToCredits() {
    if (activePlayer.getBin3().getValue() > 0) {
      activePlayer.getBin3().setValue(activePlayer.getBin3().getValue() - 1);
      activePlayer.getCredits().setValue(activePlayer.getCredits().getValue() + 1);
    }
  }

  @FXML
  private void oreToCredits() {
    if (activePlayer.getOre().intValue() > 0) {
      activePlayer.getOre().setValue(activePlayer.getOre().getValue() - 1);
      activePlayer.getCredits().setValue(activePlayer.getCredits().getValue() + 1);
    }
  }

  @FXML
  private void oreToPower() {
    if (activePlayer.getOre().intValue() > 0) {
      activePlayer.getOre().setValue(activePlayer.getOre().getValue() - 1);
      activePlayer.getBin1().setValue(activePlayer.getBin1().getValue() + 1);
    }
  }

  @FXML
  private void sacPower() {
    if (activePlayer.getBin2().intValue() > 1) {
      activePlayer.sacPower(1);
    }
  }
}
