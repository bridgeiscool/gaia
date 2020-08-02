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
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.Race;
import gaia.project.game.model.RoundBooster;
import javafx.application.Platform;
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

  private final Game game;
  private final GameBoard gameBoard;
  private final List<RoundBoosterTile> roundBoosters;

  private final List<Runnable> setupQueue = new ArrayList<>();

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
    this.gameBoard = GameBoard.random(random);
    mainPane.centerProperty().set(gameBoard);

    this.game = Game.generateGame();

    // Init player boards
    PlayerBoardController xenos = new PlayerBoardController(game.getPlayers().get(0));
    PlayerBoardController terrans = new PlayerBoardController(game.getPlayers().get(1));
    PlayerBoardController hadschHallas = new PlayerBoardController(game.getPlayers().get(2));

    // Init tech tracks
    TechTracks techTracks = new TechTracks(game.getTechTiles(), game.getAdvancedTechTiles(), game.getTerraBonus());
    PowerActionsController powerActions = new PowerActionsController();

    HBox roundBoosterBox = new HBox(2);
    List<RoundBooster> allBoosters = new ArrayList<>(Arrays.asList(RoundBooster.values()));
    Collections.shuffle(allBoosters, random);

    ObservableList<Node> children = roundBoosterBox.getChildren();
    this.roundBoosters = allBoosters.subList(0, 6).stream().map(RoundBoosterTile::new).collect(Collectors.toList());
    children.addAll(roundBoosters);

    VBox boostersAndFeds = new VBox(5, new FederationTokens(game), roundBoosterBox);
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

  // SETUP METHODS
  void setupGame() {
    List<PlayerEnum> order = getPlacementOrder(game.getPlayers());
    for (PlayerEnum toPrompt : order) {
      setupQueue.add(() -> {
        gameBoard.highlightHexes(game.getPlayers().get(toPrompt.getIdx()), me -> {
          finishUserSetupMine();
        });
      });
    }

    Platform.runLater(setupQueue.remove(0));
  }

  private void finishUserSetupMine() {
    gameBoard.clearHighlighting();
    if (setupQueue.isEmpty()) {
      pickRoundBoosters();
    } else {
      Platform.runLater(setupQueue.remove(0));
    }
  }

  private void pickRoundBoosters() {
    List<Player> toReverse = new ArrayList<>(game.getPlayers());
    Collections.reverse(toReverse);
    for (Player player : toReverse) {
      setupQueue.add(() -> {
        roundBoosters.forEach(rb -> rb.highlight(player, me -> {
          finishRoundBoosterSelection();
        }));
      });
    }

    Platform.runLater(setupQueue.remove(0));
  }

  private void finishRoundBoosterSelection() {
    roundBoosters.forEach(RoundBoosterTile::clearHighlighting);
    if (setupQueue.isEmpty()) {
      startGame();
    } else {
      Platform.runLater(setupQueue.remove(0));
    }
  }

  private void startGame() {
    System.out.println("Starting game!");
  }

  private List<PlayerEnum> getPlacementOrder(List<Player> players) {
    List<PlayerEnum> placementOrder = new ArrayList<>();
    for (Player player : players) {
      if (player.getRace() != Race.IVITS) {
        placementOrder.add(player.getPlayerEnum());
      }
    }

    List<Player> reversed = new ArrayList<>(players);
    Collections.reverse(reversed);

    for (Player player : reversed) {
      if (player.getRace() != Race.IVITS) {
        placementOrder.add(player.getPlayerEnum());
      }
    }

    // Add 3rd Xenos mine
    players.stream().filter(p -> p.getRace() == Race.XENOS).forEach(p -> placementOrder.add(p.getPlayerEnum()));

    // Add Ivits
    players.stream().filter(p -> p.getRace() == Race.IVITS).forEach(p -> placementOrder.add(p.getPlayerEnum()));

    return placementOrder;
  }

  // END SETUP METHODS
}
