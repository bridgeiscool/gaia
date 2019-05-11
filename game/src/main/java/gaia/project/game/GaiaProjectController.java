package gaia.project.game;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GaiaProjectController {
  private final Stage primaryStage;

  private StartScreenController startScreenController;
  private Scene startScreenScene;
  private GameController gameController;
  private Scene gameScene;

  public GaiaProjectController(Stage primaryStage) {
    this.primaryStage = primaryStage;
    initializeScenes();
  }

  private void initializeScenes() {
    startScreenController = new StartScreenController(this);
    startScreenScene = new Scene(new MenuPane(startScreenController));

    gameController = new GameController();
    gameScene = new Scene(new MenuPane(gameController));

    primaryStage.setScene(startScreenScene);
  }

  // Game flow methods
  public void newGame() {
    gameController.newGame();
    showView(View.GAME);
  }

  public void showView(View view) {
    switch (view) {
      case GAME:
        primaryStage.setScene(gameScene);
        break;
      default:
        throw new IllegalStateException();
    }
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
