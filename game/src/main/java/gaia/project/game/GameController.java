package gaia.project.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import gaia.project.game.board.GameBoard;
import gaia.project.game.board.Mine;
import gaia.project.game.board.Planet;
import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.Race;
import gaia.project.game.model.Round;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameController extends BorderPane {
  private static final File PREV_TURN_START = new File("temp.gp");

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

  private final GaiaProjectController parent;

  private final Game game;
  private final GameBoard gameBoard;
  private final List<RoundBoosterTile> roundBoosters;

  private final List<Runnable> setupQueue = new ArrayList<>();
  private final TechTracks techTracks;

  public GameController(GaiaProjectController parent, Game game, boolean load) {
    FXMLLoader loader = new FXMLLoader(GameController.class.getResource("GameMain.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Init game board
    this.parent = parent;
    this.game = game;
    this.gameBoard = new GameBoard(game.getGameBoard());
    mainPane.centerProperty().set(gameBoard);

    // Init player boards
    PlayerBoardController player1 = new PlayerBoardController(game.getPlayers().get(PlayerEnum.PLAYER1));
    PlayerBoardController player2 = new PlayerBoardController(game.getPlayers().get(PlayerEnum.PLAYER2));
    PlayerBoardController player3 = new PlayerBoardController(game.getPlayers().get(PlayerEnum.PLAYER3));

    // Init round bonuses
    for (int i = 0; i < 6; ++i) {
      // i + 1 because we're skipping the setup "round"
      game.getRoundScoringBonuses().get(i).addListeners(game, Round.values()[i + 1]);
    }

    // Init tech tracks
    techTracks = new TechTracks(game);
    PowerActionsController powerActions = new PowerActionsController();

    HBox roundBoosterBox = new HBox(2);

    ObservableList<Node> children = roundBoosterBox.getChildren();
    this.roundBoosters = game.getRoundBoosters().stream().map(RoundBoosterTile::new).collect(Collectors.toList());
    children.addAll(roundBoosters);

    VBox boostersAndFeds = new VBox(5, new FederationTokens(game), roundBoosterBox);
    boostersAndFeds.setAlignment(Pos.CENTER);

    HBox miscContent = new HBox(10, new ScoringArea(game), boostersAndFeds);

    VBox vbox = new VBox(5, techTracks, powerActions, new Separator(), miscContent);
    mainPane.setRight(vbox);

    HBox hbox = new HBox(5, player1, player2, player3);
    mainPane.setBottom(hbox);

    if (load) {
      game.getPlayers().values().forEach(p -> {
        p.getMines().forEach(m -> {
          gameBoard.hexes()
              .stream()
              .filter(h -> h.hasCoords(m))
              .forEach(h -> h.addMine(new Mine(h, p.getRace().getColor())));
        });
      });

      // TODO: Add other buildings...

      // Add back round booster tokens
      if (game.getCurrentRound().getValue() != Round.SETUP) {
        for (Player player : game.getPlayers().values()) {
          roundBoosters.forEach(rb -> {
            if (rb.getRoundBooster() == player.getRoundBooster()) {
              rb.addToken(player);
            }
          });
        }
      }
    }

    // If a new game or reloading during setup, call setupGame; otherwise prompt player for action
    if (!load || game.getCurrentRound().getValue() == Round.SETUP) {
      setupGame();
    } else {
      promptPlayerAction();
    }
  }

  Game getGame() {
    return game;
  }

  // Button Methods
  @FXML
  private void showActions() {
    showActions.setDisable(true);
    Optional<Actions> selectedAction = new ActionChoiceDialog(game).showAndWait();
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
    new ConversionDialog(game.getPlayers().get(game.getActivePlayer())).showAndWait();
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
    try {
      parent.loadGame(PREV_TURN_START);
    } catch (IOException | ClassNotFoundException e) {
      new Alert(AlertType.ERROR, "Could not load previous turn: " + e.getMessage(), ButtonType.OK).showAndWait();
    }
  }

  // SETUP METHODS
  private void setupGame() {
    // Save at the beginning of the setup - will have to reset whole setup if anything goes wrong...
    saveState();
    List<PlayerEnum> order = getPlacementOrder(game.getPlayers());
    for (PlayerEnum toPrompt : order) {
      Player activePlayer = game.getPlayers().get(toPrompt);
      setupQueue.add(() -> {
        gameBoard.highlightHexes(
            activePlayer,
            h -> activePlayer.getRace()
                .getHomePlanet() == h.getPlanet().map(Planet::getPlanetType).orElse(PlanetType.NONE),
            this::finishUserSetupMine);
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
    saveState();
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
    game.getPlayers().get(game.getActivePlayer()).getTempNavRange().setValue(0);
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
    game.endGameScoring();

    System.out.println("Game is over!");
  }

  private void saveState() {
    try (FileOutputStream fos = new FileOutputStream(PREV_TURN_START);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(game);
    } catch (IOException e) {
      new Alert(AlertType.ERROR, "Could not save game state: " + e.getMessage(), ButtonType.OK).showAndWait();
    }
  }
}
