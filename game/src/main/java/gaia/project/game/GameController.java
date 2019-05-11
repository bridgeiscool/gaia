package gaia.project.game;

import java.io.IOException;

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
    Sector sector = new Sector(this, 500, 500, 200, 200);
    mainPane.setCenter(sector);
    
    // Init player's board
    GameBoardController gameBoard = new GameBoardController(new Player(Race.XENOS));
    mainPane.setBottom(gameBoard);
  }

  public void newGame() {
    // TODO Auto-generated method stub
    
  }
}
