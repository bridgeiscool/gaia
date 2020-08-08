package gaia.project.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import gaia.project.game.board.GameBoard;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.Race;
import gaia.project.game.model.Round;
import gaia.project.game.model.RoundBooster;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameController extends BorderPane {
  @FXML
  private BorderPane mainPane;

  // Top
  @FXML
  private Button showActions;
  @FXML
  private Button conversions;
  @FXML
  private Button confirmAction;
  @FXML
  private Button resetTurn;

  private final Game game;
  private final GameBoard gameBoard;
  private final List<RoundBoosterTile> roundBoosters;

  private final List<Runnable> setupQueue = new ArrayList<>();
  private final TechTracks techTracks;

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
    PlayerBoardController player1 = new PlayerBoardController(game.getPlayers().get(PlayerEnum.PLAYER1));
    PlayerBoardController player2 = new PlayerBoardController(game.getPlayers().get(PlayerEnum.PLAYER2));
    PlayerBoardController player3 = new PlayerBoardController(game.getPlayers().get(PlayerEnum.PLAYER3));

    // Init tech tracks
    techTracks = new TechTracks(game);
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

    HBox hbox = new HBox(5, player1, player2, player3);
    mainPane.setBottom(hbox);

    // Top buttons
  }

  Game getGame() {
    return game;
  }

  // Button Methods
  @FXML
  private void showActions() {
    showActions.setDisable(true);
    Optional<Actions> selectedAction = new ActionChoiceDialog(this).showAndWait();
    if (selectedAction.isPresent()) {
      switch (selectedAction.get()) {
        case ADVANCE_TECH:
          activateTechTracks();
          break;
        case PASS:
          selectNewRoundBooster();
          break;
        case BUILD_MINE:
        case GAIA_PROJECT:
        case UPGRADE_BUILDING:
        case FEDERATE:
        case POWER_ACTION:
        case SPECIAL_ACTION:
          throw new IllegalStateException("Not implemented yet!");
      }
    } else {
      showActions.setDisable(false);
    }
  }

  @FXML
  private void showConversions() {
    // TODO: Implements
  }

  @FXML
  private void confirmAction() {
    if (game.allPlayersPassed()) {
      finishRound();
    } else {
      game.nextActivePlayer();
      promptPlayerAction();
    }
  }

  @FXML
  private void resetTurn() {
    // TODO: Implement
  }

  // SETUP METHODS
  void setupGame() {
    List<PlayerEnum> order = getPlacementOrder(game.getPlayers());
    for (PlayerEnum toPrompt : order) {
      setupQueue.add(() -> {
        gameBoard.highlightHexes(game.getPlayers().get(toPrompt), this::finishUserSetupMine);
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
    List<PlayerEnum> toReverse = new ArrayList<>(Arrays.asList(PlayerEnum.values()));
    Collections.reverse(toReverse);
    for (PlayerEnum player : toReverse) {
      setupQueue.add(() -> {
        roundBoosters.forEach(rb -> rb.highlight(game.getPlayers().get(player), this::finishRoundBoosterSelection));
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

  private List<PlayerEnum> getPlacementOrder(Map<PlayerEnum, Player> players) {
    List<PlayerEnum> placementOrder = new ArrayList<>();
    for (PlayerEnum playerNo : Arrays.asList(PlayerEnum.values())) {
      if (players.get(playerNo).getRace() != Race.IVITS) {
        placementOrder.add(playerNo);
      }
    }

    List<PlayerEnum> reversed = new ArrayList<>(Arrays.asList(PlayerEnum.values()));
    Collections.reverse(reversed);

    for (PlayerEnum playerNo : reversed) {
      if (players.get(playerNo).getRace() != Race.IVITS) {
        placementOrder.add(playerNo);
      }
    }

    // Add 3rd Xenos mine
    players.entrySet()
        .stream()
        .filter(e -> e.getValue().getRace() == Race.XENOS)
        .forEach(e -> placementOrder.add(e.getKey()));

    // Add Ivits
    players.entrySet()
        .stream()
        .filter(e -> e.getValue().getRace() == Race.IVITS)
        .forEach(e -> placementOrder.add(e.getKey()));

    return placementOrder;
  }

  // END SETUP METHODS

  // MAIN ACTION METHODS
  private void startGame() {
    System.out.println("Starting game!");
    resetTurn.setDisable(false);
    newRound();
  }

  private void newRound() {
    game.newRound();
    new Alert(AlertType.INFORMATION, "Starting round " + game.getCurrentRound().getValue()).showAndWait();
    takeIncome();
    gaiaPhase();
    promptPlayerAction();
  }

  private void takeIncome() {
    for (Player player : game.getPlayers().values()) {
      player.takeIncome();
    }
  }

  private void gaiaPhase() {
    // Empty for now...
  }

  private void promptPlayerAction() {
    showActions.setDisable(false);
    conversions.setDisable(false);
    confirmAction.setDisable(true);
  }

  void selectNewRoundBooster() {
    if (game.getCurrentRound().getValue() != Round.ROUND6) {
      roundBoosters
          .forEach(rb -> rb.highlight(game.getPlayers().get(game.getActivePlayer()), this::newRoundBoosterSelected));
      roundBoosters.forEach(rb -> rb.clearToken(game.getActivePlayer()));
    } else {
      game.getPassedPlayers().add(game.getActivePlayer());
      finishAction();
    }
  }

  private void newRoundBoosterSelected() {
    roundBoosters.forEach(RoundBoosterTile::clearHighlighting);
    game.getPassedPlayers().add(game.getActivePlayer());
    finishAction();
  }

  void activateTechTracks() {
    techTracks.highlightTracks(game.getPlayers().get(game.getActivePlayer()), this::finishTrackBump);
  }

  void finishTrackBump() {
    techTracks.clearActivation();
    finishAction();
  }

  void finishAction() {
    confirmAction.setDisable(false);
  }

  void finishRound() {
    System.out.println("Round over!");
    // TODO: Clear actions

    if (game.getCurrentRound().getValue() == Round.ROUND6) {
      endGame();
    } else {

      game.getCurrentPlayerOrder().clear();
      game.getCurrentPlayerOrder().addAll(game.getPassedPlayers());
      game.getPassedPlayers().clear();
      newRound();
    }
  }

  void endGame() {
    System.out.println("Game is over!");
  }

}
