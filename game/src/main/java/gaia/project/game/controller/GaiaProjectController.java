package gaia.project.game.controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import gaia.project.game.model.Game;
import gaia.project.game.model.JsonUtil;
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

  private GameController currentGame;
  private MenuPane menuPane;
  private File currentDirectory;

  public GaiaProjectController(Stage primaryStage) {
    this.primaryStage = primaryStage;

    StartScreenController startScreenController = new StartScreenController(this);
    this.menuPane = new MenuPane(startScreenController);
    Scene startScreenScene = new Scene(menuPane);

    primaryStage.setScene(startScreenScene);
  }

  public File getCurrentDirectory() {
    return currentDirectory;
  }

  public void setCurrentDirectory(File currentDirectory) {
    this.currentDirectory = currentDirectory;
  }

  // Game flow methods
  public void newGame() {
    GameOptionsDialog optionsDialog = new GameOptionsDialog();

    optionsDialog.showAndWait().ifPresent(opts -> {
      Game game = Game.generateGame(opts);

      currentGame = new GameController(this, game, false);
      currentDirectory = new File(".");
      menuPane = new MenuPane(currentGame);
      menuPane.setSaveasDisable(false);
      primaryStage.setScene(new Scene(menuPane));
      primaryStage.setMaximized(true);
      currentGame.activate();
    });
  }

  public void loadGame() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose a game to load");
    File loadFile = fileChooser.showOpenDialog(primaryStage);
    if (loadFile != null) {
      try {
        loadGame(loadFile);
      } catch (IOException e) {
        new Alert(AlertType.ERROR, "Could not load previous turn: " + e.getMessage(), ButtonType.OK).showAndWait();
      }
    }
  }

  public void loadGame(File file) throws IOException {
    try (FileReader reader = new FileReader(file)) {
      Game game = Game.read(JsonUtil.GSON.newJsonReader(reader));
      currentGame = new GameController(this, game, true);
      currentDirectory = file.getParentFile();
      currentGame.checkEnablePreviousAndNext();
      menuPane = new MenuPane(currentGame);
      menuPane.setSaveasDisable(false);
      primaryStage.setScene(new Scene(menuPane));
      primaryStage.setMaximized(true);
      currentGame.activate();
    }
  }

  public void saveGame() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose save file");
    fileChooser.setInitialFileName(currentGame.getFilename());
    File saveFile = fileChooser.showSaveDialog(primaryStage);
    if (saveFile != null) {
      try (FileWriter writer = new FileWriter(saveFile)) {
        currentGame.getGame().write(JsonUtil.GSON.newJsonWriter(writer));
      } catch (IOException e) {
        new Alert(AlertType.ERROR, "Could not save game: " + e.getMessage(), ButtonType.OK).showAndWait();
      }
    }
  }

  private class MenuPane extends BorderPane {

    private GameMenu gameMenu;

    MenuPane(Pane center) {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("GaiaProjectApp.fxml"));
      try {
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      gameMenu = new GameMenu(GaiaProjectController.this);

      setTop(gameMenu);
      setCenter(center);
    }

    private void setSaveasDisable(boolean disable) {
      gameMenu.setSaveasDisable(disable);
    }
  }

  public void exit() {
    primaryStage.close();
  }
}
