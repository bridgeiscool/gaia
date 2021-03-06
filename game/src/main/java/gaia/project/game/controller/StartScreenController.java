package gaia.project.game.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class StartScreenController extends BorderPane {
  private final GaiaProjectController controller;

  public StartScreenController(GaiaProjectController controller) {
    this.controller = controller;

    FXMLLoader loader = new FXMLLoader(StartScreenController.class.getResource("StartScreen.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // Button Handling Methods
  @FXML
  public void startGame() {
    controller.newGame();
  }

  @FXML
  public void loadGame() {
    controller.loadGame();
  }

  @FXML
  void exitGame() {
    controller.exit();
  }
}
