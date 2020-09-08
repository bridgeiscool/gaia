package gaia.project.game.controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;

public class GameMenu extends MenuBar {
  private final GaiaProjectController mainController;

  public GameMenu(GaiaProjectController parent) {
    this.mainController = parent;
    FXMLLoader loader = new FXMLLoader(GameMenu.class.getResource("GameMenu.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // File Menu
  @FXML
  private void newGame() {
    mainController.newGame();
  }

  @FXML
  private void loadGame() {
    mainController.loadGame();
  }

  @FXML
  private void saveGame() {
    mainController.saveGame();
  }

  @FXML
  private void exitGame() {
    Platform.exit();
  }
}
