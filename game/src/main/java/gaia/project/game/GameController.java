package gaia.project.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import gaia.project.game.board.Academy;
import gaia.project.game.board.EmptyHex;
import gaia.project.game.board.Gaiaformer;
import gaia.project.game.board.GameBoard;
import gaia.project.game.board.Hex;
import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.Mine;
import gaia.project.game.board.PlanetaryInstitute;
import gaia.project.game.board.ResearchLab;
import gaia.project.game.board.TradingPost;
import gaia.project.game.model.Coords;
import gaia.project.game.model.FederationTile;
import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.Race;
import gaia.project.game.model.Round;
import gaia.project.game.model.Util;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
  private final TechTracks techTracks;
  private final PowerActionsController powerActionsController;
  private final TreeMap<PlayerEnum, PlayerBoardController> playerBoards = new TreeMap<>();
  private final FederationTokens federationTokens;

  private final List<Runnable> setupQueue = new ArrayList<>();

  // For federations
  private final Set<Coords> currentFederation = new HashSet<>();
  private final IntegerProperty fedPower = new SimpleIntegerProperty();

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
    gameBoard.planetaryHexes().forEach(hex -> {
      if (game.getGaiaformed().contains(hex.getCoords())) {
        hex.getPlanet().transdimToGaia();
      }
    });

    mainPane.centerProperty().set(gameBoard);

    // Init player boards
    game.getPlayers().entrySet().forEach(e -> playerBoards.put(e.getKey(), new PlayerBoardController(e.getValue())));

    // Init round bonuses
    for (int i = 0; i < 6; ++i) {
      // i + 1 because we're skipping the setup "round"
      game.getRoundScoringBonuses().get(i).addListeners(game, Round.values()[i + 1]);
    }

    // Init tech tracks
    techTracks = new TechTracks(game);
    powerActionsController = new PowerActionsController(this);

    HBox roundBoosterBox = new HBox(2);

    ObservableList<Node> children = roundBoosterBox.getChildren();
    this.roundBoosters = game.getRoundBoosters().stream().map(RoundBoosterTile::new).collect(Collectors.toList());
    children.addAll(roundBoosters);

    this.federationTokens = new FederationTokens(game);
    VBox boostersAndFeds = new VBox(5, federationTokens, roundBoosterBox);
    boostersAndFeds.setAlignment(Pos.CENTER);

    HBox miscContent = new HBox(10, new ScoringArea(game), boostersAndFeds);

    VBox vbox = new VBox(5, techTracks, powerActionsController, new Separator(), miscContent);
    mainPane.setRight(vbox);

    HBox playerBoardBox = new HBox(5);
    playerBoards.entrySet().forEach(e -> playerBoardBox.getChildren().add(e.getValue()));
    mainPane.setBottom(playerBoardBox);

    if (load) {
      resetGameBoard(game);

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

  private void resetGameBoard(Game game) {
    game.getPlayers().values().forEach(p -> {
      p.getMines().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getCoords().equals(m))
            .forEach(h -> h.addMine(new Mine(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getTradingPosts().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getCoords().equals(m))
            .forEach(h -> h.addTradingPost(new TradingPost(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getResearchLabs().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getCoords().equals(m))
            .forEach(h -> h.addResearchLab(new ResearchLab(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getPi().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getCoords().equals(m))
            .forEach(h -> h.addPi(new PlanetaryInstitute(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getKa().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getCoords().equals(m))
            .forEach(h -> h.addAcademy(new Academy(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getQa().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getCoords().equals(m))
            .forEach(h -> h.addAcademy(new Academy(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getGaiaformers().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getCoords().equals(m))
            .forEach(h -> h.addGaiaformer(new Gaiaformer(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });
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
        case BUILD_MINE:
          selectMineBuild();
          break;
        case GAIA_PROJECT:
          selectGaiaProject();
          break;
        case UPGRADE_BUILDING:
          selectBuildingUpgrade();
          break;
        case FEDERATE:
          selectFederation();
          break;
        case ADVANCE_TECH:
          activateTechTracks(false);
          break;
        case POWER_ACTION:
          highlightPowerActions();
          break;
        case SPECIAL_ACTION:
          highlightSpecialActions();
          break;
        case PASS:
          selectNewRoundBooster();
          break;
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
        gameBoard.highlightPlanetaryHexes(
            activePlayer,
            h -> activePlayer.getRace().getHomePlanet() == h.getPlanet().getPlanetType(),
            (hex, player) -> player.buildSetupMine(hex),
            this::finishUserSetupMine);
      });
    }

    Platform.runLater(setupQueue.remove(0));
  }

  private void finishUserSetupMine(Hex hex) {
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
    gameBoard.planetaryHexes()
        .filter(h -> h.hasGaiaformer() && h.getPlanet().getPlanetType() == PlanetType.TRANSDIM)
        .forEach(h -> {
          h.getPlanet().transdimToGaia();
          game.getGaiaformed().add(h.getCoords());
        });
    if (game.getPlayers().values().stream().anyMatch(p -> p.getRace() == Race.TERRANS && p.getGaiaBin().get() > 0)) {
      System.out.println("Terran Special Ability!");
    }
    game.getPlayers().values().forEach(Player::gaiaPhase);
  }

  private void promptPlayerAction() {
    saveState();
    showActions.setDisable(false);
    conversions.setDisable(false);
    confirmAction.setDisable(true);
  }

  private void selectNewRoundBooster() {
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

  private void activateTechTracks(boolean free) {
    techTracks.highlightTracks(game.getPlayers().get(game.getActivePlayer()), this::finishTrackBump, free);
  }

  private void finishTrackBump() {
    techTracks.clearActivation();
    finishAction();
  }

  void selectMineBuild() {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    gameBoard.highlightPlanetaryHexes(activePlayer, possibleMineBuilds(activePlayer), (hex, player) -> {
      player.buildMine(hex);
    }, this::finishMineBuild);
  }

  private Predicate<HexWithPlanet> possibleMineBuilds(Player activePlayer) {
    return hex -> {
      if (hex.hasGaiaformer()
          && hex.getPlanet().getPlanetType() == PlanetType.GAIA
          && hex.getBuilder().get() == activePlayer.getPlayerEnum()) {
        return true;
      }
      for (Coords coords : activePlayer.allBuildingLocations()) {
        if (!hex.hasBuilding()
            && hex.isWithinRangeOf(
                coords,
                activePlayer.getNavRange().intValue() + activePlayer.getTempNavRange().intValue())
            && activePlayer.canDigTo(hex)) {
          return true;
        }
      }

      return false;
    };
  }

  private void finishMineBuild(HexWithPlanet hex) {
    gameBoard.clearHighlighting();
    checkForLeech(hex);
    finishAction();
  }

  private void selectGaiaProject() {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    gameBoard.highlightPlanetaryHexes(activePlayer, possibleGaiaProjects(activePlayer), (hex, player) -> {
      player.addGaiaformer(hex);
    }, this::finishGaiaProject);
  }

  private Predicate<HexWithPlanet> possibleGaiaProjects(Player activePlayer) {
    return hex -> {
      for (Coords coords : activePlayer.allBuildingLocations()) {
        if (hex.getPlanet().getPlanetType() == PlanetType.TRANSDIM
            && !hex.hasGaiaformer()
            && hex.isWithinRangeOf(
                coords,
                activePlayer.getNavRange().intValue() + activePlayer.getTempNavRange().intValue())) {
          return true;
        }
      }

      return false;
    };
  }

  public void finishGaiaProject(Hex hex) {
    gameBoard.clearHighlighting();
    finishAction();
  }

  private void selectBuildingUpgrade() {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    gameBoard.highlightPlanetaryHexes(
        activePlayer,
        h -> h.canUpgrade(activePlayer, gameBoard),
        (hex, player) -> hex.upgradeBuilding(player, gameBoard),
        this::finishBuildingUpgrade);
  }

  private void finishBuildingUpgrade(HexWithPlanet hex) {
    gameBoard.clearHighlighting();
    checkForLeech(hex);
    if (hex.checkTechTile()) {
      highlightTechTiles();
    } else {
      finishAction();
    }
  }

  void highlightTechTiles() {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    techTracks.highlightTechTiles(activePlayer, this::finishTechTileGain, () -> {
      techTracks.clearTechtileHighlighting();
      activateTechTracks(true);
    });
  }

  private void finishTechTileGain() {
    techTracks.clearTechtileHighlighting();
    finishAction();
  }

  private void checkForLeech(HexWithPlanet hex) {
    Collection<Hex> withinRange = hex.getHexesWithinRange(gameBoard.hexes(), 2);
    Map<PlayerEnum, HexWithPlanet> powerMap = withinRange.stream()
        .filter(h -> !h.isEmpty())
        .map(HexWithPlanet.class::cast)
        .filter(h -> h.hasBuilding())
        .filter(h -> h.getBuilder().get() != game.getActivePlayer())
        .collect(
            Collectors.toMap(h -> h.getBuilder().get(), h -> h, (h1, h2) -> h1.getPower() > h2.getPower() ? h1 : h2));
    for (Entry<PlayerEnum, HexWithPlanet> entry : powerMap.entrySet()) {
      Player player = game.getPlayers().get(entry.getKey());
      int powerToGain = player.getPowerGain(entry.getValue());
      if (powerToGain == 1) {
        player.chargePower(1);
      } else {
        new Alert(
            AlertType.CONFIRMATION,
            player.getRace().getRaceName() + " may gain " + powerToGain + " for " + (powerToGain - 1),
            ButtonType.NO,
            ButtonType.YES).showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(bt -> player.leechPower(powerToGain));
      }
    }
  }

  void selectFederation() {
    fedPower.setValue(0);
    currentFederation.clear();
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    gameBoard.highlightPlanetaryHexes(
        activePlayer,
        h -> h.getBuilder().orElse(null) == game.getActivePlayer() && !activePlayer.inFederation(h.getCoords()),
        (hex, player) -> hex.highlightGreen(),
        this::checkFedPower);
  }

  void checkFedPower(HexWithPlanet hex) {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    // Add adjacent hexes
    currentFederation.add(hex.getCoords());
    Util.plus(fedPower, hex.getPower() == 3 ? activePlayer.getBigBuildingPower().intValue() : hex.getPower());
    checkAdjacentHexes(activePlayer, hex);

    if (fedPower.get() < activePlayer.getFedPower()) {
      selectSatellites();
    } else {
      gameBoard.clearHighlighting();
      selectFederationTile(activePlayer);
    }
  }

  // Recursively checks all adjacent hexes to see if they should be added too
  private void checkAdjacentHexes(Player activePlayer, Hex builtOn) {
    HexWithPlanet.fromHexes(builtOn.getHexesWithinRange(gameBoard.hexes(), 1).stream())
        .filter(HexWithPlanet::hasBuilding)
        .filter(h -> !currentFederation.contains(h.getCoords()))
        .filter(h -> h.getBuilder().get() == game.getActivePlayer())
        .forEach(h -> {
          h.highlightGreen();
          Util.plus(fedPower, h.getPower() == 3 ? activePlayer.getBigBuildingPower().intValue() : h.getPower());
          currentFederation.add(h.getCoords());
          checkAdjacentHexes(activePlayer, h);
        });
  }

  void selectSatellites() {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    // TODO: Disallow placing next to existing federations
    gameBoard.highlightEmptyHexes(activePlayer, EmptyHex.possibleSatellite(activePlayer), (hex, player) -> {
      hex.addSatellite(activePlayer);
    }, this::finishSatellite);
  }

  void finishSatellite(EmptyHex hex) {
    hex.clearHighlighting();
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    checkAdjacentHexes(activePlayer, hex);
    if (fedPower.get() >= activePlayer.getFedPower()) {
      gameBoard.clearHighlighting();
      selectFederationTile(activePlayer);
    } else {
      selectSatellites();
    }
  }

  void selectFederationTile(Player activePlayer) {
    federationTokens.highlight(activePlayer, this::finishFederationTileSelection);
  }

  void finishFederationTileSelection(FederationTile chosen) {
    switch (chosen) {
      case CREDITS:
        Util.minus(game.getCreditFederations(), 1);
        break;
      case ORE:
        Util.minus(game.getOreFederations(), 1);
        break;
      case VP:
        Util.minus(game.getVpFederations(), 1);
        break;
      case POWER:
        Util.minus(game.getPtFederations(), 1);
        break;
      case QIC:
        Util.minus(game.getOreFederations(), 1);
        break;
      case RESEARCH:
        Util.minus(game.getKnowledgeFederations(), 1);
        break;
    }
    federationTokens.clearHighlighting();
    finishAction();
  }

  void highlightUserFeds() {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    playerBoards.get(activePlayer.getPlayerEnum()).highlightFederations(activePlayer, this::finishQ3Action);
  }

  void finishQ3Action() {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    playerBoards.get(activePlayer.getPlayerEnum()).clearFederationHighlighting();
    finishAction();
  }

  private void highlightPowerActions() {
    powerActionsController.highlightActions(game.getPlayers().get(game.getActivePlayer()), this::finishPowerAction);
  }

  void finishPowerAction() {
    powerActionsController.clearHighlighting();
    finishAction();
  }

  private void highlightSpecialActions() {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    if (activePlayer.getRoundBooster().isAction() && !activePlayer.roundBoosterUsed()) {
      roundBoosters.stream()
          .filter(rb -> rb.getRoundBooster() == activePlayer.getRoundBooster())
          .findFirst()
          .get()
          .highlightSpecialAction(activePlayer, () -> {
            clearSpecialActionHighlighting();
            selectMineBuild();
          });
    }

    playerBoards.get(game.getActivePlayer()).highlightActions(() -> {
      finishSpecialAction();
    });
  }

  private void clearSpecialActionHighlighting() {
    roundBoosters.forEach(rb -> rb.clearHighlighting());
    playerBoards.get(game.getActivePlayer()).clearHighlighting();
  }

  private void finishSpecialAction() {
    clearSpecialActionHighlighting();
    finishAction();
  }

  private void finishAction() {
    confirmAction.setDisable(false);
    game.getPlayers().get(game.getActivePlayer()).getTempNavRange().setValue(0);
    game.getPlayers().get(game.getActivePlayer()).getCurrentDigs().setValue(0);
  }

  private void finishRound() {
    System.out.println("Round over!");
    roundBoosters.forEach(RoundBoosterTile::clearAction);
    game.getPlayers().values().forEach(Player::clearSpecialActions);

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
