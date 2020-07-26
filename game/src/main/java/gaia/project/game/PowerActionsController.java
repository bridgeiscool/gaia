package gaia.project.game;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class PowerActionsController extends GridPane {
  public PowerActionsController() {
    FXMLLoader loader = new FXMLLoader(PlayerBoardController.class.getResource("PowerActions.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
