package gaia.project.game.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import gaia.project.game.board.GameBoard;
import gaia.project.game.board.SectorLocation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Game implements Serializable {
  private static final long serialVersionUID = -3521179457356267066L;

  private final List<SectorLocation> gameBoard;
  private final Set<Coords> gaiaformed;

  private final List<RoundBooster> roundBoosters;
  private final List<TechTile> techTiles;
  // Boolean indicates if it's been taken
  private final Map<AdvancedTechTile, Boolean> advancedTechTiles;
  private final List<RoundScoringBonus> roundScoringBonuses;
  private final EndScoring endScoring1;
  private final EndScoring endScoring2;
  private final Map<PlayerEnum, Player> players;
  private final FederationTile terraBonus;

  // Power Actions
  private transient Property<Boolean> k3ActionTaken = new SimpleBooleanProperty(false);
  private transient Property<Boolean> doubleTfActionTaken = new SimpleBooleanProperty(false);
  private transient Property<Boolean> creditsActionTaken = new SimpleBooleanProperty(false);
  private transient Property<Boolean> oreActionTaken = new SimpleBooleanProperty(false);
  private transient Property<Boolean> k2ActionTaken = new SimpleBooleanProperty(false);
  private transient Property<Boolean> tfActionTaken = new SimpleBooleanProperty(false);
  private transient Property<Boolean> ptActionTaken = new SimpleBooleanProperty(false);

  private transient Property<Boolean> q2ActionTaken = new SimpleBooleanProperty(false);
  private transient Property<Boolean> q3ActionTaken = new SimpleBooleanProperty(false);
  private transient Property<Boolean> q4ActionTaken = new SimpleBooleanProperty(false);

  // Federation tokens
  private transient IntegerProperty vpFederations;
  private transient IntegerProperty ptFederations;
  private transient IntegerProperty qicFederations;
  private transient IntegerProperty oreFederations;
  private transient IntegerProperty creditFederations;
  private transient IntegerProperty knowledgeFederations;

  // Game state
  private transient Property<Round> currentRound;
  private int turn;
  private transient ObservableList<PlayerEnum> currentPlayerOrder;
  private transient ObservableList<PlayerEnum> passedPlayers;
  private transient Property<PlayerEnum> activePlayer;

  public static Game generateGame() {
    return generateGame(System.currentTimeMillis());
  }

  public static Game generateGame(long seed) {
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

    Map<PlayerEnum, Player> players = new HashMap<>();
    players.put(PlayerEnum.PLAYER1, Race.BALTAKS.getPlayer(PlayerEnum.PLAYER1));
    players.put(PlayerEnum.PLAYER2, Race.NEVLAS.getPlayer(PlayerEnum.PLAYER2));
    players.put(PlayerEnum.PLAYER3, Race.AMBAS.getPlayer(PlayerEnum.PLAYER3));

    return new Game(
        gameBoard,
        allBoosters.subList(0, 6),
        techTiles,
        advTechTiles.subList(0, 6),
        federationTiles.get(0),
        roundScoringBonuses.subList(0, 6),
        endScoring.get(0),
        endScoring.get(1),
        players);
  }

  private Game(
      List<SectorLocation> gameBoard,
      List<RoundBooster> roundBoosters,
      List<TechTile> techTiles,
      List<AdvancedTechTile> advancedTechTiles,
      FederationTile terraBonus,
      List<RoundScoringBonus> roundScoringBonuses,
      EndScoring endScoring1,
      EndScoring endScoring2,
      Map<PlayerEnum, Player> players) {
    this.gameBoard = ImmutableList.copyOf(gameBoard);
    this.gaiaformed = new HashSet<>();
    this.roundBoosters = ImmutableList.copyOf(roundBoosters);
    this.techTiles = ImmutableList.copyOf(techTiles);
    this.advancedTechTiles = new LinkedHashMap<>(6);
    advancedTechTiles.forEach(tt -> this.advancedTechTiles.put(tt, Boolean.FALSE));
    this.terraBonus = terraBonus;
    this.roundScoringBonuses = ImmutableList.copyOf(roundScoringBonuses);
    this.endScoring1 = endScoring1;
    this.endScoring2 = endScoring2;
    this.players = ImmutableMap.copyOf(players);

    this.vpFederations = new SimpleIntegerProperty(terraBonus == FederationTile.VP ? 2 : 3);
    this.ptFederations = new SimpleIntegerProperty(terraBonus == FederationTile.POWER ? 2 : 3);
    this.qicFederations = new SimpleIntegerProperty(terraBonus == FederationTile.QIC ? 2 : 3);
    this.oreFederations = new SimpleIntegerProperty(terraBonus == FederationTile.ORE ? 2 : 3);
    this.creditFederations = new SimpleIntegerProperty(terraBonus == FederationTile.CREDITS ? 2 : 3);
    this.knowledgeFederations = new SimpleIntegerProperty(terraBonus == FederationTile.RESEARCH ? 2 : 3);

    this.currentRound = new SimpleObjectProperty<>(Round.SETUP);
    this.currentPlayerOrder = FXCollections.observableArrayList(Arrays.asList(PlayerEnum.values()));
    this.passedPlayers = FXCollections.observableArrayList();
    this.activePlayer = new SimpleObjectProperty<>(PlayerEnum.PLAYER1);
  }

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
      player.getScore().setValue(player.getScore().getValue() + player.getProjectedTechScoring().getValue());
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

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    oos.writeUTF(currentRound.getValue().name());
    oos.writeBoolean(k3ActionTaken.getValue());
    oos.writeBoolean(doubleTfActionTaken.getValue());
    oos.writeBoolean(creditsActionTaken.getValue());
    oos.writeBoolean(oreActionTaken.getValue());
    oos.writeBoolean(k2ActionTaken.getValue());
    oos.writeBoolean(tfActionTaken.getValue());
    oos.writeBoolean(ptActionTaken.getValue());
    oos.writeBoolean(q2ActionTaken.getValue());
    oos.writeBoolean(q3ActionTaken.getValue());
    oos.writeBoolean(q4ActionTaken.getValue());

    oos.writeInt(vpFederations.get());
    oos.writeInt(ptFederations.get());
    oos.writeInt(qicFederations.get());
    oos.writeInt(oreFederations.get());
    oos.writeInt(creditFederations.get());
    oos.writeInt(knowledgeFederations.get());

    oos.writeObject(new ArrayList<>(currentPlayerOrder));
    oos.writeObject(new ArrayList<>(passedPlayers));
    oos.writeObject(activePlayer.getValue());
  }

  @SuppressWarnings("unchecked")
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
    currentRound = new SimpleObjectProperty<>(Round.valueOf(ois.readUTF()));
    k3ActionTaken = new SimpleBooleanProperty(ois.readBoolean());
    doubleTfActionTaken = new SimpleBooleanProperty(ois.readBoolean());
    creditsActionTaken = new SimpleBooleanProperty(ois.readBoolean());
    oreActionTaken = new SimpleBooleanProperty(ois.readBoolean());
    k2ActionTaken = new SimpleBooleanProperty(ois.readBoolean());
    tfActionTaken = new SimpleBooleanProperty(ois.readBoolean());
    ptActionTaken = new SimpleBooleanProperty(ois.readBoolean());
    q2ActionTaken = new SimpleBooleanProperty(ois.readBoolean());
    q3ActionTaken = new SimpleBooleanProperty(ois.readBoolean());
    q4ActionTaken = new SimpleBooleanProperty(ois.readBoolean());

    vpFederations = new SimpleIntegerProperty(ois.readInt());
    ptFederations = new SimpleIntegerProperty(ois.readInt());
    qicFederations = new SimpleIntegerProperty(ois.readInt());
    oreFederations = new SimpleIntegerProperty(ois.readInt());
    creditFederations = new SimpleIntegerProperty(ois.readInt());
    knowledgeFederations = new SimpleIntegerProperty(ois.readInt());

    currentPlayerOrder = FXCollections.observableList((ArrayList<PlayerEnum>) ois.readObject());
    passedPlayers = FXCollections.observableList((ArrayList<PlayerEnum>) ois.readObject());
    activePlayer = new SimpleObjectProperty<>((PlayerEnum) ois.readObject());
  }
}
