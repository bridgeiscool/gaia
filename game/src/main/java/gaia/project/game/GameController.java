package gaia.project.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import gaia.project.game.board.GameBoard;
import gaia.project.game.model.Player;
import gaia.project.game.model.Race;
import gaia.project.game.model.RoundBooster;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    Random random = new Random(System.currentTimeMillis());
    GameBoard gameBoard = GameBoard.random(random);
    mainPane.centerProperty().set(gameBoard);

    Game game = Game.generateGame();

    // Init player boards
    PlayerBoardController xenos = new PlayerBoardController(new Player(Race.XENOS));
    PlayerBoardController terrans = new PlayerBoardController(new Player(Race.TERRANS));
    PlayerBoardController hadschHallas = new PlayerBoardController(new Player(Race.HADSCH_HALLAS));

    // Init tech tracks
    TechTracks techTracks = new TechTracks(game.getTechTiles(), game.getAdvancedTechTiles(), game.getTerraBonus());
    PowerActionsController powerActions = new PowerActionsController();

    HBox roundBoosters = new HBox(2);
    List<RoundBooster> allBoosters = new ArrayList<>(Arrays.asList(RoundBooster.values()));
    Collections.shuffle(allBoosters, random);

    ObservableList<Node> children = roundBoosters.getChildren();
    children.addAll(allBoosters.subList(0, 6).stream().map(RoundBoosterTile::new).collect(Collectors.toList()));

    VBox boostersAndFeds = new VBox(5, new FederationTokens(game), roundBoosters);
    boostersAndFeds.setAlignment(Pos.CENTER);

    HBox miscContent = new HBox(
        10,
        new ScoringArea(game.getRoundScoringBonuses(), game.getEndScoring1(), game.getEndScoring2()),
        boostersAndFeds);

    VBox vbox = new VBox(5, techTracks, powerActions, new Separator(), miscContent);
    mainPane.setRight(vbox);

    HBox hbox = new HBox(5, xenos, terrans, hadschHallas);
    mainPane.setBottom(hbox);
  }

  public void newGame() {
    // TODO Auto-generated method stub

  }
}
