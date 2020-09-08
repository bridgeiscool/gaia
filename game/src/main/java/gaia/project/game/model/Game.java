package gaia.project.game.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import gaia.project.game.board.GameBoard;
import gaia.project.game.board.SectorLocation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Game {
  private final List<SectorLocation> gameBoard = new ArrayList<>(10);
  private final Set<Coords> gaiaformed = new HashSet<>();

  private final List<RoundBooster> roundBoosters = new ArrayList<>(6);
  private final List<TechTile> techTiles = new ArrayList<>(6);
  // Boolean indicates if it's been taken
  private final Map<AdvancedTechTile, Boolean> advancedTechTiles = new LinkedHashMap<>(6);
  private final List<RoundScoringBonus> roundScoringBonuses = new ArrayList<>(6);
  private EndScoring endScoring1;
  private EndScoring endScoring2;
  private final Map<PlayerEnum, Player> players = new HashMap<>();
  private FederationTile terraBonus;

  // Power Actions
  private final Property<Boolean> k3ActionTaken = new SimpleBooleanProperty(false);
  private final Property<Boolean> doubleTfActionTaken = new SimpleBooleanProperty(false);
  private final Property<Boolean> creditsActionTaken = new SimpleBooleanProperty(false);
  private final Property<Boolean> oreActionTaken = new SimpleBooleanProperty(false);
  private final Property<Boolean> k2ActionTaken = new SimpleBooleanProperty(false);
  private final Property<Boolean> tfActionTaken = new SimpleBooleanProperty(false);
  private final Property<Boolean> ptActionTaken = new SimpleBooleanProperty(false);

  private final Property<Boolean> q2ActionTaken = new SimpleBooleanProperty(false);
  private final Property<Boolean> q3ActionTaken = new SimpleBooleanProperty(false);
  private final Property<Boolean> q4ActionTaken = new SimpleBooleanProperty(false);

  // Federation tokens
  private final IntegerProperty vpFederations = new SimpleIntegerProperty();
  private final IntegerProperty ptFederations = new SimpleIntegerProperty();
  private final IntegerProperty qicFederations = new SimpleIntegerProperty();
  private final IntegerProperty oreFederations = new SimpleIntegerProperty();
  private final IntegerProperty creditFederations = new SimpleIntegerProperty();
  private final IntegerProperty knowledgeFederations = new SimpleIntegerProperty();

  // Game state
  private final Property<Round> currentRound = new SimpleObjectProperty<>();
  private int turn;
  private final ObservableList<PlayerEnum> currentPlayerOrder = FXCollections.observableArrayList();
  private final ObservableList<PlayerEnum> passedPlayers = FXCollections.observableArrayList();
  private final Property<PlayerEnum> activePlayer = new SimpleObjectProperty<>();

  public static Game generateGame() {
    return generateGame(System.currentTimeMillis());
  }

  public static Game generateGame(long seed) {
    Game game = new Game();

    Random random = new Random(seed);

    List<SectorLocation> gameBoard = GameBoard.random(random).getSectorLocations();

    List<TechTile> techTiles = new ArrayList<>(Arrays.asList(TechTile.values()));
    Collections.shuffle(techTiles, random);
    List<AdvancedTechTile> advTechTiles = new ArrayList<>(Arrays.asList(AdvancedTechTile.values()));
    Collections.shuffle(advTechTiles, random);
    List<FederationTile> federationTiles = new ArrayList<>(FederationTile.normalFederations());
    Collections.shuffle(federationTiles, random);
    List<RoundScoringBonus> roundScoringBonuses = new ArrayList<>(Arrays.asList(RoundScoringBonus.values()));
    Collections.shuffle(roundScoringBonuses);
    List<EndScoring> endScoring = new ArrayList<>(Arrays.asList(EndScoring.values()));
    Collections.shuffle(endScoring, random);
    List<RoundBooster> allBoosters = new ArrayList<>(Arrays.asList(RoundBooster.values()));
    Collections.shuffle(allBoosters, random);

    List<Race> races = new ArrayList<>(Arrays.asList(Race.values()));

    // TODO: Update for 4 players
    do {
      Collections.shuffle(races, random);
    } while (races.subList(0, 3).stream().map(Race::getHomePlanet).collect(Collectors.toSet()).size() < 3);

    game.players.put(PlayerEnum.PLAYER1, races.get(0).newPlayer(PlayerEnum.PLAYER1));
    game.players.put(PlayerEnum.PLAYER2, races.get(1).newPlayer(PlayerEnum.PLAYER2));
    game.players.put(PlayerEnum.PLAYER3, races.get(2).newPlayer(PlayerEnum.PLAYER3));

    game.gameBoard.addAll(gameBoard);
    game.roundBoosters.addAll(allBoosters.subList(0, 6));
    game.techTiles.addAll(techTiles);
    advTechTiles.subList(0, 6).forEach(tt -> game.advancedTechTiles.put(tt, Boolean.FALSE));
    game.terraBonus = federationTiles.get(0);
    game.roundScoringBonuses.addAll(roundScoringBonuses.subList(0, 6));
    game.endScoring1 = endScoring.get(0);
    game.endScoring2 = endScoring.get(1);

    game.vpFederations.setValue(game.terraBonus == FederationTile.VP ? 2 : 3);
    game.ptFederations.setValue(game.terraBonus == FederationTile.POWER ? 2 : 3);
    game.qicFederations.setValue(game.terraBonus == FederationTile.QIC ? 2 : 3);
    game.oreFederations.setValue(game.terraBonus == FederationTile.ORE ? 2 : 3);
    game.creditFederations.setValue(game.terraBonus == FederationTile.CREDITS ? 2 : 3);
    game.knowledgeFederations.setValue(game.terraBonus == FederationTile.RESEARCH ? 2 : 3);

    game.currentRound.setValue(Round.SETUP);
    game.currentPlayerOrder.addAll(Arrays.asList(PlayerEnum.values()));
    game.activePlayer.setValue(PlayerEnum.PLAYER1);

    return game;
  }

  private Game() {}

  public List<RoundBooster> getRoundBoosters() {
    return roundBoosters;
  }

  public List<TechTile> getTechTiles() {
    return techTiles;
  }

  public Map<AdvancedTechTile, Boolean> getAdvancedTechTiles() {
    return advancedTechTiles;
  }

  public FederationTile getTerraBonus() {
    return terraBonus;
  }

  public List<RoundScoringBonus> getRoundScoringBonuses() {
    return roundScoringBonuses;
  }

  public EndScoring getEndScoring1() {
    return endScoring1;
  }

  public EndScoring getEndScoring2() {
    return endScoring2;
  }

  public Map<PlayerEnum, Player> getPlayers() {
    return players;
  }

  public Property<Round> getCurrentRound() {
    return currentRound;
  }

  public int getTurn() {
    return turn;
  }

  public ObservableList<PlayerEnum> getCurrentPlayerOrder() {
    return currentPlayerOrder;
  }

  public ObservableList<PlayerEnum> getPassedPlayers() {
    return passedPlayers;
  }

  public PlayerEnum getActivePlayer() {
    return activePlayer.getValue();
  }

  public Property<PlayerEnum> getActivePlayerProperty() {
    return activePlayer;
  }

  public List<SectorLocation> getGameBoard() {
    return gameBoard;
  }

  public Set<Coords> getGaiaformed() {
    return gaiaformed;
  }

  public Property<Boolean> getK3ActionTaken() {
    return k3ActionTaken;
  }

  public Property<Boolean> getDoubleTfActionTaken() {
    return doubleTfActionTaken;
  }

  public Property<Boolean> getCreditsActionTaken() {
    return creditsActionTaken;
  }

  public Property<Boolean> getOreActionTaken() {
    return oreActionTaken;
  }

  public Property<Boolean> getK2ActionTaken() {
    return k2ActionTaken;
  }

  public Property<Boolean> getTfActionTaken() {
    return tfActionTaken;
  }

  public Property<Boolean> getPtActionTaken() {
    return ptActionTaken;
  }

  public Property<Boolean> getQ2ActionTaken() {
    return q2ActionTaken;
  }

  public Property<Boolean> getQ3ActionTaken() {
    return q3ActionTaken;
  }

  public Property<Boolean> getQ4ActionTaken() {
    return q4ActionTaken;
  }

  public IntegerProperty getVpFederations() {
    return vpFederations;
  }

  public IntegerProperty getPtFederations() {
    return ptFederations;
  }

  public IntegerProperty getQicFederations() {
    return qicFederations;
  }

  public IntegerProperty getOreFederations() {
    return oreFederations;
  }

  public IntegerProperty getCreditFederations() {
    return creditFederations;
  }

  public IntegerProperty getKnowledgeFederations() {
    return knowledgeFederations;
  }

  public void newRound() {
    currentRound.setValue(currentRound.getValue().nextRound());
    turn = 0;
    activePlayer.setValue(currentPlayerOrder.get(0));
    k3ActionTaken.setValue(false);
    doubleTfActionTaken.setValue(false);
    oreActionTaken.setValue(false);
    creditsActionTaken.setValue(false);
    k2ActionTaken.setValue(false);
    tfActionTaken.setValue(false);
    ptActionTaken.setValue(false);
    q2ActionTaken.setValue(false);
    q3ActionTaken.setValue(false);
    q4ActionTaken.setValue(false);
  }

  public void nextActivePlayer() {
    Preconditions.checkArgument(passedPlayers.size() != currentPlayerOrder.size());

    // Keep cycling until we hit a player who has not passed...
    int currentIdx = currentPlayerOrder.indexOf(activePlayer.getValue());
    do {
      currentIdx = (currentIdx + 1) % currentPlayerOrder.size();
    } while (passedPlayers.contains(currentPlayerOrder.get(currentIdx)));

    this.activePlayer.setValue(currentPlayerOrder.get(currentIdx));
    this.turn++;
  }

  public boolean allPlayersPassed() {
    return passedPlayers.size() == players.size();
  }

  public void endGameScoring() {
    endScoring1.addToFinalScores(players.values());
    endScoring2.addToFinalScores(players.values());
    for (Player player : players.values()) {
      player.techScoring();
      player.convertResourcesToVps();
    }
  }

  public int getCheapestPowerAction() {
    if (!ptActionTaken.getValue() || !tfActionTaken.getValue()) {
      return 3;
    }

    if (!creditsActionTaken.getValue() || !oreActionTaken.getValue() || !k2ActionTaken.getValue()) {
      return 4;
    }

    if (!doubleTfActionTaken.getValue()) {
      return 5;
    }

    if (!k3ActionTaken.getValue()) {
      return 7;
    }

    // Just has to be bigger than possible power someone could have
    return 100;
  }

  public int getCheapestQicAction() {
    if (!q2ActionTaken.getValue()) {
      return 2;
    }

    if (!q3ActionTaken.getValue()) {
      return 3;
    }

    if (!q4ActionTaken.getValue()) {
      return 4;
    }

    return 20;
  }

  public final void write(JsonWriter json) throws IOException {
    json.beginObject();

    json.name(JsonUtil.CURRENT_ROUND).value(currentRound.getValue().name());
    json.name(JsonUtil.K3_ACTION_TAKEN).value(k3ActionTaken.getValue());
    json.name(JsonUtil.TF2_ACTION_TAKEN).value(doubleTfActionTaken.getValue());
    json.name(JsonUtil.CREDITS_ACTION_TAKEN).value(creditsActionTaken.getValue());
    json.name(JsonUtil.ORE_ACTION_TAKEN).value(oreActionTaken.getValue());
    json.name(JsonUtil.K2_ACTION_TAKEN).value(k2ActionTaken.getValue());
    json.name(JsonUtil.TF_ACTION_TAKEN).value(tfActionTaken.getValue());
    json.name(JsonUtil.PT_ACTION_TAKEN).value(ptActionTaken.getValue());
    json.name(JsonUtil.Q2_ACTION_TAKEN).value(q2ActionTaken.getValue());
    json.name(JsonUtil.Q3_ACTION_TAKEN).value(q3ActionTaken.getValue());
    json.name(JsonUtil.Q4_ACTION_TAKEN).value(q4ActionTaken.getValue());

    json.name(JsonUtil.VP_FEDS).value(vpFederations.get());
    json.name(JsonUtil.PT_FEDS).value(ptFederations.get());
    json.name(JsonUtil.QIC_FEDS).value(qicFederations.get());
    json.name(JsonUtil.ORE_FEDS).value(oreFederations.get());
    json.name(JsonUtil.CREDIT_FEDS).value(creditFederations.get());
    json.name(JsonUtil.K_FEDS).value(knowledgeFederations.get());

    JsonUtil.writeCollection(json, JsonUtil.PLAYER_ORDER, currentPlayerOrder, PlayerEnum::name);
    JsonUtil.writeCollection(json, JsonUtil.PASSED_PLAYERS, passedPlayers, PlayerEnum::name);
    json.name(JsonUtil.ACTIVE_PLAYER).value(activePlayer.getValue().name());

    JsonUtil.writeCollection(json, JsonUtil.GAME_BOARD, gameBoard, SectorLocation::name);
    JsonUtil.writeCoordsCollection(json, JsonUtil.GAIAFORMED, gaiaformed);
    JsonUtil.writeCollection(json, JsonUtil.ROUND_BOOSTERS, roundBoosters, RoundBooster::name);
    JsonUtil.writeCollection(json, JsonUtil.TECH_TILES, techTiles, TechTile::name);
    json.name(JsonUtil.ADV_TECH_TILES).beginArray();
    for (Entry<AdvancedTechTile, Boolean> entry : advancedTechTiles.entrySet()) {
      json.beginObject().name(entry.getKey().name()).value(entry.getValue()).endObject();
    }
    json.endArray();
    JsonUtil.writeCollection(json, JsonUtil.ROUND_SCORING, roundScoringBonuses, RoundScoringBonus::name);
    json.name(JsonUtil.END_SCORING1).value(endScoring1.name());
    json.name(JsonUtil.END_SCORING2).value(endScoring2.name());
    json.name(JsonUtil.TERRA_BONUS).value(terraBonus.name());

    json.name(JsonUtil.TURN).value(turn);

    for (Player player : players.values()) {
      json.name(player.getRace().name());
      player.write(json);
    }

    json.endObject();
  }

  public static Game read(JsonReader json) throws IOException {
    Game game = new Game();
    json.beginObject();
    while (json.hasNext()) {
      String name = json.nextName();
      switch (name) {
        case JsonUtil.CURRENT_ROUND:
          game.currentRound.setValue(Round.valueOf(json.nextString()));
          break;
        case JsonUtil.K3_ACTION_TAKEN:
          game.k3ActionTaken.setValue(json.nextBoolean());
          break;
        case JsonUtil.TF2_ACTION_TAKEN:
          game.doubleTfActionTaken.setValue(json.nextBoolean());
          break;
        case JsonUtil.CREDITS_ACTION_TAKEN:
          game.creditsActionTaken.setValue(json.nextBoolean());
          break;
        case JsonUtil.ORE_ACTION_TAKEN:
          game.oreActionTaken.setValue(json.nextBoolean());
          break;
        case JsonUtil.K2_ACTION_TAKEN:
          game.k2ActionTaken.setValue(json.nextBoolean());
          break;
        case JsonUtil.TF_ACTION_TAKEN:
          game.tfActionTaken.setValue(json.nextBoolean());
          break;
        case JsonUtil.PT_ACTION_TAKEN:
          game.ptActionTaken.setValue(json.nextBoolean());
          break;
        case JsonUtil.Q2_ACTION_TAKEN:
          game.q2ActionTaken.setValue(json.nextBoolean());
          break;
        case JsonUtil.Q3_ACTION_TAKEN:
          game.q3ActionTaken.setValue(json.nextBoolean());
          break;
        case JsonUtil.Q4_ACTION_TAKEN:
          game.q4ActionTaken.setValue(json.nextBoolean());
          break;
        case JsonUtil.VP_FEDS:
          game.vpFederations.setValue(json.nextInt());
          break;
        case JsonUtil.PT_FEDS:
          game.ptFederations.setValue(json.nextInt());
          break;
        case JsonUtil.QIC_FEDS:
          game.qicFederations.setValue(json.nextInt());
          break;
        case JsonUtil.ORE_FEDS:
          game.oreFederations.setValue(json.nextInt());
          break;
        case JsonUtil.CREDIT_FEDS:
          game.creditFederations.setValue(json.nextInt());
          break;
        case JsonUtil.K_FEDS:
          game.knowledgeFederations.setValue(json.nextInt());
          break;
        case JsonUtil.PLAYER_ORDER:
          JsonUtil.readStringArray(game.currentPlayerOrder, json, PlayerEnum::valueOf);
          break;
        case JsonUtil.PASSED_PLAYERS:
          JsonUtil.readStringArray(game.passedPlayers, json, PlayerEnum::valueOf);
          break;
        case JsonUtil.ACTIVE_PLAYER:
          game.activePlayer.setValue(PlayerEnum.valueOf(json.nextString()));
          break;
        case JsonUtil.GAME_BOARD:
          JsonUtil.readStringArray(game.gameBoard, json, SectorLocation::valueOf);
          break;
        case JsonUtil.GAIAFORMED:
          JsonUtil.readCoordsArray(game.gaiaformed, json);
          break;
        case JsonUtil.ROUND_BOOSTERS:
          JsonUtil.readStringArray(game.roundBoosters, json, RoundBooster::valueOf);
          break;
        case JsonUtil.ROUND_SCORING:
          JsonUtil.readStringArray(game.roundScoringBonuses, json, RoundScoringBonus::valueOf);
          break;
        case JsonUtil.TECH_TILES:
          JsonUtil.readStringArray(game.techTiles, json, TechTile::valueOf);
          break;
        case JsonUtil.ADV_TECH_TILES:
          readAdvancedTech(json, game.advancedTechTiles);
          break;
        case JsonUtil.END_SCORING1:
          game.endScoring1 = EndScoring.valueOf(json.nextString());
          break;
        case JsonUtil.END_SCORING2:
          game.endScoring2 = EndScoring.valueOf(json.nextString());
          break;
        case JsonUtil.TERRA_BONUS:
          game.terraBonus = FederationTile.valueOf(json.nextString());
          break;
        case JsonUtil.TURN:
          game.turn = json.nextInt();
          break;
        default:
          // Other keys should be race names...
          readPlayer(name, json, game.players);
      }
    }
    json.endObject();

    return game;
  }

  private static void readAdvancedTech(JsonReader json, Map<AdvancedTechTile, Boolean> advTech) throws IOException {
    json.beginArray();
    while (json.hasNext()) {
      json.beginObject();
      advTech.put(AdvancedTechTile.valueOf(json.nextName()), json.nextBoolean());
      json.endObject();
    }

    json.endArray();
  }

  private static void readPlayer(String name, JsonReader json, Map<PlayerEnum, Player> players) throws IOException {
    // This call should crash if it's an invalid name
    Race race = Race.valueOf(name);
    Player empty = race.emptyPlayer();
    Player.read(empty, json);
    players.put(empty.getPlayerEnum(), empty);
  }
}
