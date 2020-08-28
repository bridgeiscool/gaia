package gaia.project.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import gaia.project.game.board.Academy;
import gaia.project.game.board.EmptyHex;
import gaia.project.game.board.Gaiaformer;
import gaia.project.game.board.GameBoard;
import gaia.project.game.board.Hex;
import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.Mine;
import gaia.project.game.board.PlanetaryInstitute;
import gaia.project.game.board.ResearchLab;
import gaia.project.game.board.Satellite;
import gaia.project.game.board.TradingPost;
import gaia.project.game.model.AmbasPlayer;
import gaia.project.game.model.BescodsPlayer;
import gaia.project.game.model.Coords;
import gaia.project.game.model.FederationTile;
import gaia.project.game.model.FiraksPlayer;
import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerBoardAction;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.Race;
import gaia.project.game.model.Round;
import gaia.project.game.model.RoundBooster;
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
    techTracks = new TechTracks(game, this::findLostPlanet);
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

    game.getPlayers().values().forEach(p -> {
      p.getSatellites().forEach(s -> {
        gameBoard.emptyHexes()
            .filter(h -> h.getCoords().equals(s))
            .forEach(h -> h.addSatelliteUI(new Satellite(h, p.getRace().getColor(), p.getPlayerEnum())));
      });
    });

    game.getPlayers().values().forEach(p -> {
      if (!p.getLostPlanet().isEmpty()) {
        gameBoard.emptyHexes()
            .filter(h -> h.getCoords().equals(Iterables.getOnlyElement(p.getLostPlanet())))
            .forEach(h -> h.addLostPlanet(p));
      }
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
    new SelectVPsDialog(game).showAndWait();
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
          game.getGaiaformed().add(h.getCoords());
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
      player.buildMine(hex);
    }, this::finishMineBuild);
  }

  private Predicate<HexWithPlanet> possibleMineBuilds() {
    return hex -> (hex.hasGaiaformer()
        && hex.getPlanet().getPlanetType() == PlanetType.GAIA
        && hex.getBuilder().get() == activePlayer().getPlayerEnum())
        || (!hex.hasBuilding() && isWithinRange(activePlayer(), hex) && activePlayer().canDigTo(hex));
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

  void finishTechTileCover(Serializable selected) {
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
    Collection<Hex> withinRange = hex.getHexesWithinRange(gameBoard.hexes(), 2);

    for (Hex maybeLeech : withinRange) {
      if (maybeLeech.getBuilder().isPresent() && powerMap.containsKey(maybeLeech.getBuilder().get())) {
        PlayerEnum builder = maybeLeech.getBuilder().get();
        int power = game.getPlayers().get(builder).getPower(maybeLeech);
        if (power > powerMap.get(builder)) {
          powerMap.put(builder, power);
        }
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
    fedPower.setValue(0);
    currentFederation.clear();
    gameBoard.highlightPlanetaryHexes(
        activePlayer(),
        h -> h.getBuilder().orElse(null) == game.getActivePlayer() && !activePlayer().inFederation(h.getCoords()),
        (hex, player) -> hex.highlightCyan(),
        this::checkIfSatellitesNeeded);
  }

  void checkIfSatellitesNeeded(HexWithPlanet hex) {
    // Add adjacent hexes
    currentFederation.add(hex.getCoords());
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
    builtOn.getHexesWithinRange(gameBoard.hexes(), 1)
        .stream()
        .filter(Hex::hasBuilding)
        .filter(h -> !currentFederation.contains(h.getCoords()))
        .filter(h -> h.getBuilder().get() == game.getActivePlayer())
        .forEach(h -> {
          h.highlightCyan();
          Util.plus(fedPower, activePlayer().getPower(h));
          currentFederation.add(h.getCoords());
          checkAdjacentHexes(h);
        });
  }

  void startSelectSatellites() {
    Player activePlayer = activePlayer();
    // TODO: Disallow placing next to existing federations
    if (activePlayer.getBin1().get() + activePlayer.getBin2().get() + activePlayer.getBin3().get() > 0) {
      gameBoard.highlightEmptyHexes(
          activePlayer,
          EmptyHex.possibleSatellite(activePlayer, currentFederation),
          (hex, player) -> {
            hex.addSatellite(activePlayer);
          },
          this::finishSatellite);
    } else {
      new Alert(AlertType.WARNING, "You do not have enough power tokens", ButtonType.OK);
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

  void addMoreSatellites(EmptyHex lastAdded) {
    Player activePlayer = game.getPlayers().get(game.getActivePlayer());
    // TODO: Disallow placing next to existing federations

    if (activePlayer.getBin1().get() + activePlayer.getBin2().get() + activePlayer.getBin3().get() > 0) {
      gameBoard.highlightEmptyHexes(
          activePlayer,
          EmptyHex.possibleSatellite(activePlayer, currentFederation)
              .or(h -> h.isWithinRangeOf(lastAdded.getCoords(), 1)),
          (hex, player) -> {
            hex.addSatellite(activePlayer);
          },
          this::finishSatellite);
    } else {
      new Alert(AlertType.WARNING, "You do not have enough power tokens", ButtonType.OK);
    }
  }

  void selectFederationTile() {
    activePlayer().getFederations().add(new HashSet<>(currentFederation));
    Util.plus(activePlayer().getBuildingsInFeds(), currentFederation.size());
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
    for (Coords coords : activePlayer().allBuildingLocations()) {
      if (hex.isWithinRangeOf(
          coords,
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
          h -> activePlayer().getResearchLabs().contains(h.getCoords()),
          (hex, player) -> {
            FiraksPlayer cast = (FiraksPlayer) player;
            cast.piAction(hex);
          },
          this::finishFiraksPiAction);
    } else if (action == PlayerBoardAction.MOVE_PI) {
      Preconditions.checkArgument(activePlayer() instanceof AmbasPlayer);
      // Ambas special action
      gameBoard.highlightPlanetaryHexes(
          activePlayer(),
          h -> activePlayer().getMines().contains(h.getCoords()),
          (hex, player) -> {
            AmbasPlayer ambas = (AmbasPlayer) activePlayer();
            ambas.piAction(
                hex,
                // Find the PI...
                gameBoard.planetaryHexes().filter(h -> ambas.getPi().contains(h.getCoords())).findFirst().get());
          },
          this::finishAmbasPiAction);
    } else if (action == PlayerBoardAction.BUMP_LOWEST_TECH) {
      techTracks.highlightLowestTracks((BescodsPlayer) activePlayer(), this::finishTrackBump);
    } else {
      finishAction();
    }
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

    System.out.println("Game is over!");
  }

  private void saveState() {
    try (FileOutputStream fos = new FileOutputStream(new File(getFilename()));
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(game);
    } catch (IOException e) {
      new Alert(AlertType.ERROR, "Could not save game state: " + e.getMessage(), ButtonType.OK).showAndWait();
    }
  }
}
