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
  private void qicToNav() {}

  @FXML
  private void powerToQic() {}

  @FXML
  private void qicToOre() {}

  @FXML
  private void powerToOre() {}

  @FXML
  private void powerToKnowledge() {}

  @FXML
  private void knowledgeToCredits() {}

  @FXML
  private void powerToCredits() {}

  @FXML
  private void oreToCredits() {

  }

  @FXML
  private void oreToPower() {

  }
}
