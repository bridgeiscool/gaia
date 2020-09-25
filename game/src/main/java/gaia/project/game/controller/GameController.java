package gaia.project.game.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
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

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import gaia.project.game.board.Academy;
import gaia.project.game.board.BoardUtils;
import gaia.project.game.board.EmptyHex;
import gaia.project.game.board.Gaiaformer;
import gaia.project.game.board.GameBoard;
import gaia.project.game.board.Hex;
import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.Mine;
import gaia.project.game.board.PlanetaryInstitute;
import gaia.project.game.board.ResearchLab;
import gaia.project.game.board.Satellite;
import gaia.project.game.board.SpaceStation;
import gaia.project.game.board.TradingPost;
import gaia.project.game.model.AmbasPlayer;
import gaia.project.game.model.BescodsPlayer;
import gaia.project.game.model.FederationTile;
import gaia.project.game.model.FiraksPlayer;
import gaia.project.game.model.Game;
import gaia.project.game.model.IvitsPlayer;
import gaia.project.game.model.JsonUtil;
import gaia.project.game.model.LantidsPlayer;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerBoardAction;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.Race;
import gaia.project.game.model.Round;
import gaia.project.game.model.RoundBooster;
import gaia.project.game.model.SetupEnum;
import gaia.project.game.model.TechTile;
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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class GameController extends BorderPane {
  private static final double BASE_BUTTON_WIDTH = 100;
  private static final double BASE_BUTTON_HEIGHT = 25;
  private static final double BASE_BUTTON_FONT = 13;

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
  @FXML
  private HBox playerBoardBox;
  @FXML
  private HBox gameBoardBox;
  @FXML
  private VBox rightBox;

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
  private final Set<String> currentFederation = new HashSet<>();
  private final IntegerProperty fedPower = new SimpleIntegerProperty();
  private boolean itarGaiaPhase;

  public GameController(GaiaProjectController parent, Game game, boolean load) {
    FXMLLoader loader = new FXMLLoader(GameController.class.getResource("GameMain.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // UI
    showActions.setPrefHeight(BASE_BUTTON_HEIGHT * BoardUtils.getScaling());
    showActions.setPrefWidth(BASE_BUTTON_WIDTH * BoardUtils.getScaling());
    showActions.setFont(new Font(BASE_BUTTON_FONT * BoardUtils.getScaling()));
    conversions.setPrefHeight(BASE_BUTTON_HEIGHT * BoardUtils.getScaling());
    conversions.setPrefWidth(BASE_BUTTON_WIDTH * BoardUtils.getScaling());
    conversions.setFont(new Font(BASE_BUTTON_FONT * BoardUtils.getScaling()));
    confirmAction.setPrefHeight(BASE_BUTTON_HEIGHT * BoardUtils.getScaling());
    confirmAction.setPrefWidth(BASE_BUTTON_WIDTH * BoardUtils.getScaling());
    confirmAction.setFont(new Font(BASE_BUTTON_FONT * BoardUtils.getScaling()));
    resetTurn.setPrefHeight(BASE_BUTTON_HEIGHT * BoardUtils.getScaling());
    resetTurn.setPrefWidth(BASE_BUTTON_WIDTH * BoardUtils.getScaling());
    resetTurn.setFont(new Font(BASE_BUTTON_FONT * BoardUtils.getScaling()));

    // Init game board
    this.parent = parent;
    this.game = game;
    this.gameBoard = new GameBoard(game.getGameBoard());
    gameBoard.addCenterLabels();
    gameBoard.planetaryHexes().forEach(hex -> {
      if (game.getGaiaformed().contains(hex.getHexId())) {
        hex.getPlanet().transdimToGaia();
      }
    });

    gameBoardBox.getChildren().add(gameBoard);

    // Init player boards
    game.getPlayers().entrySet().forEach(e -> playerBoards.put(e.getKey(), new PlayerBoardController(e.getValue())));

    // Init round bonuses
    for (int i = 0; i < 6; ++i) {
      // i + 1 because we're skipping the setup "round"
      game.getRoundScoringBonuses().get(i).addListeners(game, Round.values()[i + 1]);
    }

    // Init tech tracks
    techTracks = new TechTracks(game, this::findLostPlanet);
    powerActionsController = new PowerActionsController(this);

    HBox roundBoosterBox = new HBox(2);

    ObservableList<Node> children = roundBoosterBox.getChildren();
    this.roundBoosters = game.getRoundBoosters().stream().map(RoundBoosterTile::new).collect(Collectors.toList());
    children.addAll(roundBoosters);

    this.federationTokens = new FederationTokens(game);
    VBox boostersAndFeds = new VBox(5, federationTokens, roundBoosterBox);
    boostersAndFeds.setAlignment(Pos.CENTER);

    rightBox.getChildren()
        .addAll(
            techTracks,
            powerActionsController,
            new Separator(),
            new HBox(10, new ScoringArea(game), boostersAndFeds));

    playerBoards.entrySet().forEach(e -> playerBoardBox.getChildren().add(e.getValue()));

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

  }

  void activate() {
    // If a new game or reloading during setup, call setupGame; otherwise prompt player for action
    if (game.getCurrentRound().getValue() == Round.SETUP) {
      setupGame();
    } else {
      promptPlayerAction();
    }
  }

  private void resetGameBoard(Game game) {

    game.getPlayers().values().forEach(p -> {
      p.getMines().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getHexId().equals(m))
            .filter(h -> p.getRace() != Race.LANTIDS || !((LantidsPlayer) p).getLeechMines().contains(h.getHexId()))
            .forEach(h -> h.addMine(new Mine(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getTradingPosts().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getHexId().equals(m))
            .forEach(h -> h.addTradingPost(new TradingPost(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getResearchLabs().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getHexId().equals(m))
            .forEach(h -> h.addResearchLab(new ResearchLab(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getPi().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getHexId().equals(m))
            .forEach(h -> h.addPi(new PlanetaryInstitute(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getKa().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getHexId().equals(m))
            .forEach(h -> h.addAcademy(new Academy(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getQa().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getHexId().equals(m))
            .forEach(h -> h.addAcademy(new Academy(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getGaiaformers().forEach(m -> {
        gameBoard.planetaryHexes()
            .filter(h -> h.getHexId().equals(m))
            .forEach(h -> h.addGaiaformer(new Gaiaformer(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      p.getSatellites().forEach(s -> {
        gameBoard.emptyHexes()
            .filter(h -> h.getHexId().equals(s))
            .forEach(h -> h.addSatelliteUI(new Satellite(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      if (!p.getLostPlanet().isEmpty()) {
        gameBoard.emptyHexes()
            .filter(h -> h.getHexId().equals(Iterables.getOnlyElement(p.getLostPlanet())))
            .forEach(h -> h.addLostPlanet(p));
      }
    });

    game.getPlayers().values().stream().filter(p -> p.getRace() == Race.IVITS).forEach(p -> {
      IvitsPlayer ivits = (IvitsPlayer) p;
      gameBoard.emptyHexes()
          .filter(h -> ivits.getSpaceStations().contains(h.getHexId()))
          .forEach(h -> h.addSpaceStation(new SpaceStation(h, p.getPlayerEnum())));
    });

    game.getPlayers().values().stream().filter(p -> p.getRace() == Race.LANTIDS).forEach(p -> {
      LantidsPlayer lantids = (LantidsPlayer) p;
      gameBoard.planetaryHexes()
          .filter(h -> lantids.getLeechMines().contains(h.getHexId()))
          .forEach(h -> h.addLeechMine(new Mine(h, lantids.getRace().getColor(), p.getPlayerEnum())));
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
    ConversionDialog.create(game.getPlayers().get(game.getActivePlayer())).showAndWait();
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
      parent.loadGame(new File(getFilename()));
    } catch (IOException | ClassNotFoundException e) {
      new Alert(AlertType.ERROR, "Could not load previous turn: " + e.getMessage(), ButtonType.OK).showAndWait();
    }
  }

  private String getFilename() {
    return "r" + game.getCurrentRound().getValue().display() + "t" + game.getTurn() + ".gp";
  }

  // SETUP METHODS
  private void setupGame() {
    // Save at the beginning of the setup - will have to reset whole setup if anything goes wrong...
    // This has to run after the Gleens action that modifies their QIC/ORE values first in a separate runlater call
    Platform.runLater(() -> saveState());

    if (game.getSetup() == SetupEnum.AUCTION_RACES) {

      SelectVPsDialog selectVPsDialog = new SelectVPsDialog(game);
      selectVPsDialog.showAndWait();

      // TODO: Update for 4 players
      game.getPlayers().get(PlayerEnum.PLAYER1).updateScore(selectVPsDialog.getP1Score(), "Starting Score");
      game.getPlayers().get(PlayerEnum.PLAYER2).updateScore(selectVPsDialog.getP2Score(), "Starting Score");
      game.getPlayers().get(PlayerEnum.PLAYER3).updateScore(selectVPsDialog.getP3Score(), "Starting Score");
    } else {
      // Random races
      game.getPlayers().values().forEach(p -> p.updateScore(10, "Starting Score"));
    }

    List<PlayerEnum> order = getPlacementOrder(game.getPlayers());
    for (PlayerEnum toPrompt : order) {
      Player activePlayer = game.getPlayers().get(toPrompt);
      setupQueue.add(() -> {
        gameBoard.highlightPlanetaryHexes(
            activePlayer,
            h -> activePlayer.getRace().getHomePlanet() == h.getPlanet().getPlanetType() && !h.hasBuilding(),
            (hex, player) -> player.buildSetupMine(hex, gameBoard),
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
    resetTurn.setDisable(false);
    newRound();
  }

  private void newRound() {
    game.newRound();
    new Alert(AlertType.INFORMATION, "Starting Round " + game.getCurrentRound().getValue().display()).showAndWait();
    takeIncome();
    gaiaPhase();
    if (!itarGaiaPhase) {
      promptPlayerAction();
    }
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
          game.getGaiaformed().add(h.getHexId());
        });

    // Handle ITARs ability
    Optional<Player> maybeItars = game.getPlayers().values().stream().filter(p -> p.getRace() == Race.ITARS).findAny();
    if (maybeItars.isPresent() && maybeItars.get().getGaiaBin().get() > 3) {
      Player itars = maybeItars.get();

      Optional<ButtonType> dialog =
          new Alert(AlertType.CONFIRMATION, "Sac 4 power for a tech tile?", ButtonType.YES, ButtonType.NO)
              .showAndWait();
      if (dialog.get().equals(ButtonType.YES)) {
        itarGaiaPhase = true;
        Util.minus(itars.getGaiaBin(), 4);
        techTracks.highlightTechTiles(itars, this::finishTechTileGain, () -> {
          techTracks.clearTechtileHighlighting();
          activateTechTracks(true);
        }, this::highlightUserTechTiles);
      } else {
        finishGaiaPhase();
      }
    } else {
      finishGaiaPhase();
    }
  }

  private void finishGaiaPhase() {
    game.getPlayers().values().forEach(Player::gaiaPhase);
  }

  private Player activePlayer() {
    if (itarGaiaPhase) {
      return game.getPlayers().values().stream().filter(p -> p.getRace() == Race.ITARS).findAny().get();
    }

    return game.getPlayers().get(game.getActivePlayer());
  }

  private void promptPlayerAction() {
    saveState();
    showActions.setDisable(false);
    conversions.setDisable(false);
    confirmAction.setDisable(true);
  }

  private void selectNewRoundBooster() {
    if (game.getCurrentRound().getValue() != Round.ROUND6) {
      roundBoosters.forEach(rb -> rb.highlight(activePlayer(), this::newRoundBoosterSelected));
      roundBoosters.forEach(rb -> rb.clearToken(game.getActivePlayer()));
    } else {
      game.getPassedPlayers().add(game.getActivePlayer());
      activePlayer().roundBoosterVps();
      finishAction();
    }
  }

  private void newRoundBoosterSelected() {
    roundBoosters.forEach(RoundBoosterTile::clearHighlighting);
    game.getPassedPlayers().add(game.getActivePlayer());
    finishAction();
  }

  private void activateTechTracks(boolean free) {
    techTracks.highlightTracks(activePlayer(), this::finishTrackBump, free);
  }

  private void finishTrackBump() {
    techTracks.clearActivation();

    // We might be here during the Itars ability or on a normal action
    if (itarGaiaPhase) {
      finishGaiaPhase();
      itarGaiaPhase = false;
      finishGaiaPhase();
      promptPlayerAction();
    } else {
      finishAction();
    }
  }

  void selectMineBuild() {
    gameBoard.highlightPlanetaryHexes(activePlayer(), possibleMineBuilds(), (hex, player) -> {
      if (hex.hasBuilding()) {
        Preconditions.checkArgument(player.getRace() == Race.LANTIDS);
        ((LantidsPlayer) player).buildLeechMine(hex, gameBoard);
      } else {
        player.buildMine(hex, gameBoard);
      }
    }, this::finishMineBuild);
  }

  private Predicate<HexWithPlanet> possibleMineBuilds() {
    return hex -> (hex.hasGaiaformer()
        && hex.getPlanet().getPlanetType() == PlanetType.GAIA
        && hex.getBuilder().get() == activePlayer().getPlayerEnum())
        || (!hex.hasBuilding() && isWithinRange(activePlayer(), hex) && activePlayer().canDigTo(hex))
        || canBuildLeechMine(hex);
  }

  private boolean canBuildLeechMine(HexWithPlanet hex) {
    // Has to be Lantids, has to be within range, has to be built on by an opponent, and can't already have a leech mine
    return activePlayer().getRace() == Race.LANTIDS
        && isWithinRange(activePlayer(), hex)
        && hex.hasBuilding()
        && hex.getBuilder().get() != game.getActivePlayer()
        && hex.getLeechMine() == null;
  }

  private void finishMineBuild(HexWithPlanet hex) {
    gameBoard.clearHighlighting();
    checkForLeech(hex);
    finishAction();
  }

  private void selectGaiaProject() {
    gameBoard.highlightPlanetaryHexes(activePlayer(), possibleGaiaProjects(), (hex, player) -> {
      player.addGaiaformer(hex);
    }, this::finishGaiaProject);
  }

  private Predicate<HexWithPlanet> possibleGaiaProjects() {
    return hex -> hex.getPlanet().getPlanetType() == PlanetType.TRANSDIM
        && !hex.hasGaiaformer()
        && isWithinRange(activePlayer(), hex);
  }

  public void finishGaiaProject(Hex hex) {
    gameBoard.clearHighlighting();
    finishAction();
  }

  private void selectBuildingUpgrade() {
    gameBoard.highlightPlanetaryHexes(
        activePlayer(),
        h -> h.canUpgrade(activePlayer(), gameBoard),
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
    techTracks.highlightTechTiles(activePlayer(), this::finishTechTileGain, () -> {
      techTracks.clearTechtileHighlighting();
      activateTechTracks(true);
    }, this::highlightUserTechTiles);
  }

  void highlightUserTechTiles() {
    techTracks.clearTechtileHighlighting();
    playerBoards.get(activePlayer().getPlayerEnum()).highlightTechTiles(this::finishTechTileCover);
  }

  void finishTechTileCover(Enum<?> selected) {
    playerBoards.get(activePlayer().getPlayerEnum()).clearHighlighting();
    TechTile covered = (TechTile) selected;
    activePlayer().getTechTiles().remove(covered);
    activePlayer().getCoveredTechTiles().add(covered);
    covered.removeFrom(activePlayer());
    activateTechTracks(true);
  }

  private void finishTechTileGain() {
    techTracks.clearTechtileHighlighting();

    // We might be here during the Itars ability or on a normal action
    if (itarGaiaPhase) {
      finishGaiaPhase();
      itarGaiaPhase = false;
      finishGaiaPhase();
      promptPlayerAction();
    } else {
      finishAction();
    }
  }

  private void checkForLeech(Hex hex) {
    Map<PlayerEnum, Integer> powerMap = game.getPlayers()
        .keySet()
        .stream()
        .filter(p -> p != game.getActivePlayer())
        .collect(Collectors.toMap(p -> p, p -> 0));
    Collection<Hex> withinRange = hex.getAllHexesWithinRange(gameBoard.hexes(), 2);

    for (Hex maybeLeech : withinRange) {
      if (maybeLeech.hasBuilding() && powerMap.containsKey(maybeLeech.getBuilder().get())) {
        PlayerEnum builder = maybeLeech.getBuilder().get();
        int power = game.getPlayers().get(builder).getPower(maybeLeech);
        if (power > powerMap.get(builder)) {
          powerMap.put(builder, power);
        }
      }

      // Check leech from Lantids leech mines
      @Nullable
      Mine leechMine = maybeLeech.getLeechMine();
      if (leechMine != null
          && powerMap.containsKey(leechMine.getPlayer())
          && powerMap.get(leechMine.getPlayer()) == 0) {
        powerMap.put(leechMine.getPlayer(), 1);
      }
    }

    for (Entry<PlayerEnum, Integer> entry : powerMap.entrySet()) {
      Player player = game.getPlayers().get(entry.getKey());
      int powerToGain = player.getPowerGain(entry.getValue());
      if (powerToGain == 1) {
        player.chargePower(1);
      } else if (powerToGain > 1) {
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
    if (activePlayer().getRace() != Race.IVITS || activePlayer().getFederations().isEmpty()) {
      fedPower.setValue(0);
      currentFederation.clear();
      gameBoard.highlightPlanetaryHexes(
          activePlayer(),
          h -> ((h.getBuilder().orElse(null) == game.getActivePlayer() && h.hasBuilding())
              || (h.getLeechMine() != null && h.getLeechMine().getPlayer() == game.getActivePlayer()))
              && !activePlayer().inFederation(h.getHexId()),
          (hex, player) -> hex.highlightCyan(),
          this::checkIfSatellitesNeeded);
    } else {
      // IVITS after their first federation...
      Preconditions.checkArgument(activePlayer().getRace() == Race.IVITS);
      IvitsPlayer ivits = (IvitsPlayer) activePlayer();
      if (ivits.totalFederationPower() >= ivits.nextFederationPower()) {
        federationTokens.highlight(activePlayer(), this::finishFederationTileSelection);
      } else {
        startSelectSatellitesIvits(ivits);
      }

    }
  }

  void checkIfSatellitesNeeded(HexWithPlanet hex) {
    // Add adjacent hexes
    currentFederation.add(hex.getHexId());
    Util.plus(fedPower, activePlayer().getPower(hex));
    checkAdjacentHexes(hex);

    if (fedPower.get() < activePlayer().getFedPower()) {
      startSelectSatellites();
    } else {
      gameBoard.clearHighlighting();
      selectFederationTile();
    }
  }

  // Recursively checks all adjacent hexes to see if they should be added too
  private void checkAdjacentHexes(Hex builtOn) {
    builtOn.getOtherHexesWithinRange(gameBoard.hexes(), 1)
        .stream()
        // Space stations count as a building...
        .filter(h -> h.hasBuilding() || h.hasSpaceStation() || h.getLeechMine() != null)
        .filter(h -> !currentFederation.contains(h.getHexId()))
        .filter(
            h -> h.getBuilder().get() == game.getActivePlayer()
                || (h.getLeechMine() != null && h.getLeechMine().getPlayer() == game.getActivePlayer()))
        .forEach(h -> {
          h.highlightCyan();
          Util.plus(fedPower, activePlayer().getPower(h));
          currentFederation.add(h.getHexId());
          checkAdjacentHexes(h);
        });
  }

  void startSelectSatellites() {
    Player activePlayer = activePlayer();
    // TODO: Disallow placing next to existing federations
    if (activePlayer.canBuildSatellite()) {
      gameBoard.highlightEmptyHexes(
          activePlayer,
          EmptyHex.possibleSatellite(activePlayer, currentFederation, gameBoard),
          (hex, player) -> hex.addSatellite(activePlayer),
          this::finishSatellite);
    } else {
      new Alert(AlertType.WARNING, "You do not have enough power tokens", ButtonType.OK);
    }
  }

  void startSelectSatellitesIvits(IvitsPlayer ivits) {
    // TODO: Disallow placing next to existing federations
    currentFederation.addAll(Iterables.getOnlyElement(ivits.getFederations()));
    gameBoard.hexes().forEach(h -> {
      if (currentFederation.contains(h.getHexId())) {
        h.highlightCyan();
      }
    });
    if (ivits.canBuildSatellite()) {
      gameBoard.highlightEmptyHexes(
          ivits,
          EmptyHex.possibleSatellite(ivits, currentFederation, gameBoard),
          (hex, player) -> hex.addSatellite(ivits),
          this::finishSatelliteIvits);
    } else {
      new Alert(AlertType.WARNING, "You do not have enough QIC to build another satellite.", ButtonType.OK);
    }
  }

  void finishSatellite(EmptyHex hex) {
    hex.clearHighlighting();
    checkAdjacentHexes(hex);
    if (fedPower.get() >= activePlayer().getFedPower()) {
      gameBoard.clearHighlighting();
      selectFederationTile();
    } else {
      addMoreSatellites(hex);
    }
  }

  void finishSatelliteIvits(EmptyHex hex) {
    IvitsPlayer ivits = (IvitsPlayer) activePlayer();
    hex.clearHighlighting();
    checkAdjacentHexes(hex);
    Iterables.getOnlyElement(ivits.getFederations()).addAll(currentFederation);
    if (ivits.totalFederationPower() >= ivits.nextFederationPower()) {
      gameBoard.clearHighlighting();
      selectFederationTile();
    } else {
      addMoreSatellitesIvits(hex);
    }
  }

  void addMoreSatellites(EmptyHex lastAdded) {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    // TODO: Disallow placing next to existing federations

    if (activePlayer.canBuildSatellite()) {
      gameBoard.highlightEmptyHexes(
          activePlayer,
          EmptyHex.possibleSatellite(activePlayer, currentFederation, gameBoard)
              .or(h -> h.isWithinRangeOf(lastAdded, 1)),
          (hex, player) -> hex.addSatellite(activePlayer),
          this::finishSatellite);
    } else {
      new Alert(AlertType.WARNING, "You do not have enough power tokens", ButtonType.OK);
    }
  }

  void addMoreSatellitesIvits(EmptyHex lastAdded) {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    // TODO: Disallow placing next to existing federations

    if (activePlayer.canBuildSatellite()) {
      gameBoard.highlightEmptyHexes(
          activePlayer,
          EmptyHex.possibleSatellite(activePlayer, currentFederation, gameBoard)
              .or(h -> h.isWithinRangeOf(lastAdded, 1)),
          (hex, player) -> hex.addSatellite(activePlayer),
          this::finishSatelliteIvits);
    } else {
      new Alert(AlertType.WARNING, "You do not have enough QIC to build another satellite.", ButtonType.OK);
    }
  }

  void selectFederationTile() {
    if (activePlayer().getRace() == Race.IVITS && !activePlayer().getFederations().isEmpty()) {
      IvitsPlayer ivits = (IvitsPlayer) activePlayer();
      ivits.getBuildingsInFeds()
          .setValue(
              Iterables.getOnlyElement(ivits.getFederations())
                  .stream()
                  .filter(c -> !ivits.getSpaceStations().contains(c))
                  .count());
    } else {
      activePlayer().getFederations().add(new HashSet<>(currentFederation));
      Util.plus(activePlayer().getBuildingsInFeds(), currentFederation.size());
    }
    federationTokens.highlight(activePlayer(), this::finishFederationTileSelection);
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
        Util.minus(game.getQicFederations(), 1);
        break;
      case RESEARCH:
        Util.minus(game.getKnowledgeFederations(), 1);
        break;
      default:
        throw new IllegalStateException("Gleens Fed should never be selected!");
    }
    federationTokens.clearHighlighting();
    finishAction();
  }

  void highlightUserFeds() {
    playerBoards.get(activePlayer().getPlayerEnum()).highlightFederations(activePlayer(), this::finishQ3Action);
  }

  void finishQ3Action() {
    playerBoards.get(activePlayer().getPlayerEnum()).clearFederationHighlighting();
    finishAction();
  }

  private void highlightPowerActions() {
    powerActionsController.highlightActions(activePlayer(), this::finishPowerAction);
  }

  void finishPowerAction() {
    powerActionsController.clearHighlighting();
    finishAction();
  }

  private void highlightSpecialActions() {
    if (activePlayer().getRoundBooster().isAction() && !activePlayer().roundBoosterUsed()) {
      roundBoosters.stream()
          .filter(rb -> rb.getRoundBooster() == activePlayer().getRoundBooster())
          .findFirst()
          .get()
          .highlightSpecialAction(activePlayer(), () -> {
            clearSpecialActionHighlighting();
            if (activePlayer().getRoundBooster() == RoundBooster.JUMP) {
              jumpAction();
            } else {
              selectMineBuild();
            }
          });
    }

    playerBoards.get(game.getActivePlayer()).highlightActions(this::finishSpecialAction);
  }

  private void jumpAction() {
    // Alternative being gaiaform...
    boolean buildMine;
    Player activePlayer = activePlayer();
    if (activePlayer.canBuildMine() && activePlayer.canGaiaform()) {
      Optional<ButtonType> response;
      ButtonType mine = new ButtonType("Build Mine", ButtonData.LEFT);
      ButtonType gf = new ButtonType("Gaiaform", ButtonData.RIGHT);
      do {
        response = new Alert(AlertType.CONFIRMATION, "Research lab or PI?", mine, gf).showAndWait();
      } while (response.isEmpty());

      buildMine = response.get().getButtonData().equals(ButtonData.LEFT);
    } else {
      buildMine = activePlayer.canBuildMine();
    }

    if (buildMine) {
      selectMineBuild();
    } else {
      selectGaiaProject();
    }
  }

  private void findLostPlanet(Player activePlayer) {
    gameBoard.highlightEmptyHexes(
        activePlayer,
        hex -> (!game.getPlayers().keySet().stream().anyMatch(p -> hex.hasSatellite(p))
            && isWithinRange(activePlayer, hex)),
        (hex, player) -> {
          hex.addLostPlanet(player);
          player.addLostPlanet(hex);
        },
        this::finishLostPlanet);
  }

  private boolean isWithinRange(Player activevPlayer, Hex hex) {
    for (String hexId : activePlayer().allBuildingLocations()) {
      if (hex.isWithinRangeOf(
          gameBoard.hexWithId(hexId),
          activePlayer().getNavRange().intValue() + activePlayer().getTempNavRange().intValue())) {
        return true;
      }
    }

    return false;
  }

  private void finishLostPlanet(EmptyHex hex) {
    gameBoard.clearHighlighting();
    checkForLeech(hex);
    finishAction();
  }

  private void finishSpecialAction(Serializable action) {
    clearSpecialActionHighlighting();

    // Hooks for special actions that require game board interaction
    if (action == PlayerBoardAction.RL_TO_TP) {
      Preconditions.checkArgument(activePlayer() instanceof FiraksPlayer);
      // Firaks special action
      gameBoard.highlightPlanetaryHexes(
          activePlayer(),
          h -> activePlayer().getResearchLabs().contains(h.getHexId()),
          (hex, player) -> ((FiraksPlayer) player).piAction(hex),
          this::finishFiraksPiAction);
    } else if (action == PlayerBoardAction.MOVE_PI) {
      Preconditions.checkArgument(activePlayer() instanceof AmbasPlayer);
      // Ambas special action
      gameBoard.highlightPlanetaryHexes(
          activePlayer(),
          h -> activePlayer().getMines().contains(h.getHexId()),
          (hex, player) -> {
            AmbasPlayer ambas = (AmbasPlayer) activePlayer();
            ambas.piAction(
                hex,
                // Find the PI...
                gameBoard.planetaryHexes().filter(h -> ambas.getPi().contains(h.getHexId())).findFirst().get());
          },
          this::finishAmbasPiAction);
    } else if (action == PlayerBoardAction.BUMP_LOWEST_TECH) {
      techTracks.highlightLowestTracks((BescodsPlayer) activePlayer(), this::finishTrackBump);
    } else if (action == PlayerBoardAction.SPACE_STATION) {
      gameBoard.highlightEmptyHexes(
          activePlayer(),
          possibleSpaceStations(),
          (hex, player) -> ((IvitsPlayer) player).buildSpaceStation(hex, gameBoard),
          this::finishIvitsPiAction);
    } else {
      finishAction();
    }
  }

  private Predicate<EmptyHex> possibleSpaceStations() {
    return hex -> !hex.hasSpaceStation()
        && !hex.hasBuilding()
        && !hex.hasSatellite(game.getActivePlayer())
        && isWithinRange(activePlayer(), hex);
  }

  private void clearSpecialActionHighlighting() {
    roundBoosters.forEach(rb -> rb.clearHighlighting());
    playerBoards.get(game.getActivePlayer()).clearHighlighting();
  }

  private void finishFiraksPiAction(Hex hex) {
    gameBoard.clearHighlighting();
    activateTechTracks(true);
  }

  private void finishAmbasPiAction(Hex hex) {
    gameBoard.clearHighlighting();
    finishAction();
  }

  private void finishIvitsPiAction(Hex hex) {
    gameBoard.clearHighlighting();
    finishAction();
  }

  private void finishAction() {
    confirmAction.setDisable(false);
    game.getPlayers().get(game.getActivePlayer()).getTempNavRange().setValue(0);
    game.getPlayers().get(game.getActivePlayer()).getCurrentDigs().setValue(0);
  }

  private void finishRound() {
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

    new ScoreDialog(game).show();
  }

  private void saveState() {
    try (FileWriter out = new FileWriter(new File(getFilename()))) {
      game.write(JsonUtil.GSON.newJsonWriter(out));
    } catch (IOException e) {
      new Alert(AlertType.ERROR, "Could not save game state: " + e.getMessage(), ButtonType.OK).showAndWait();
    }
  }
}
