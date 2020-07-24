package gaia.project.game;

import java.io.IOException;
import java.util.Random;

import gaia.project.game.board.GameBoard;
import gaia.project.game.model.Player;
import gaia.project.game.model.Race;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class GameController extends BorderPane {
  @FXML
  private BorderPane mainPane;

  public GameController() {
    FXMLLoader loader = new FXMLLoader(GameController.class.getResource("GameMain.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Init game board
    GameBoard gameBoard = GameBoard.random(new Random(System.currentTimeMillis()));
    mainPane.centerProperty().set(gameBoard);

    // Init player boards
    PlayerBoardController xenos = new PlayerBoardController(new Player(Race.XENOS));
    PlayerBoardController terrans = new PlayerBoardController(new Player(Race.TERRANS));
    PlayerBoardController hadschHallas = new PlayerBoardController(new Player(Race.HADSCH_HALLAS));
    TechTracks techTracks = new TechTracks();
    VBox vbox = new VBox(5, xenos, terrans, hadschHallas, techTracks);
    mainPane.setRight(vbox);
  }

  public void newGame() {
    // TODO Auto-generated method stub

  }
}
