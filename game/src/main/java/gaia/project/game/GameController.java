package gaia.project.game;

import java.io.IOException;

import gaia.project.game.board.GameBoard;
import gaia.project.game.model.Player;
import gaia.project.game.model.Race;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

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
    // Sector sector = new Sector(this, 500, 500, 200, 200);
    GameBoard gameBoard = new GameBoard();
    mainPane.centerProperty().set(gameBoard);

    // Init player's board
    PlayerBoardController playerBoard = new PlayerBoardController(new Player(Race.XENOS));
    mainPane.setBottom(playerBoard);
  }

  public void newGame() {
    // TODO Auto-generated method stub

  }
}
