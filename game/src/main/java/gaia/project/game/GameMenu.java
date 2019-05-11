package gaia.project.game;

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
  private void exitGame() {
    Platform.exit();
  }

  // View menu
  @FXML
  private void gameView() {
    mainController.showView(View.GAME);
  }
}
