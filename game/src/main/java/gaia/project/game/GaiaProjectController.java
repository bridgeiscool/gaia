package gaia.project.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import gaia.project.game.model.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GaiaProjectController {
  private final Stage primaryStage;

  public GaiaProjectController(Stage primaryStage) {
    this.primaryStage = primaryStage;
    StartScreenController startScreenController = new StartScreenController(this);
    Scene startScreenScene = new Scene(new MenuPane(startScreenController));

    primaryStage.setScene(startScreenScene);
  }

  // Game flow methods
  public void newGame() {
    Game game = Game.generateGame();

    GameController gameController = new GameController(this, game, false);
    primaryStage.setScene(new Scene(new MenuPane(gameController)));
  }

  public void loadGame() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose a game to load");
    File saveFile = fileChooser.showOpenDialog(primaryStage);
    try {
      loadGame(saveFile);
    } catch (IOException | ClassNotFoundException e) {
      new Alert(AlertType.ERROR, "Could not load previous turn: " + e.getMessage(), ButtonType.OK).showAndWait();
    }
  }

  public void loadGame(File file) throws IOException, ClassNotFoundException {
    try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {
      Game game = (Game) ois.readObject();
      GameController gameController = new GameController(this, game, true);
      primaryStage.setScene(new Scene(new MenuPane(gameController)));
    }
  }

  public void saveGame() {

  }

  private class MenuPane extends BorderPane {

    MenuPane(Pane center) {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("GaiaProjectApp.fxml"));
      try {
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      setTop(new GameMenu(GaiaProjectController.this));
      setCenter(center);
    }
  }

  public void exit() {
    primaryStage.close();
  }
}
