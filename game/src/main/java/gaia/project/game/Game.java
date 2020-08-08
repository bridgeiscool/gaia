package gaia.project.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.base.Preconditions;

import gaia.project.game.model.AdvancedTechTile;
import gaia.project.game.model.EndScoring;
import gaia.project.game.model.FederationTile;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.Race;
import gaia.project.game.model.Round;
import gaia.project.game.model.RoundBooster;
import gaia.project.game.model.RoundScoringBonus;
import gaia.project.game.model.TechTile;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public class Game implements Serializable {
  private final List<RoundBooster> roundBoosters;
  private final List<TechTile> techTiles;
  private final List<AdvancedTechTile> advancedTechTiles;
  private final List<RoundScoringBonus> roundScoringBonuses;
  private final EndScoring endScoring1;
  private final EndScoring endScoring2;
  private final Map<PlayerEnum, Player> players;
  private final FederationTile terraBonus;

  // Game state
  private final Property<Round> currentRound;
  private final List<PlayerEnum> currentPlayerOrder;
  private final List<PlayerEnum> passedPlayers;
  private PlayerEnum activePlayer;

  public static Game generateGame() {
    return generateGame(System.currentTimeMillis());
  }

  public static Game generateGame(long seed) {
    Random random = new Random(seed);

    List<TechTile> techTiles = new ArrayList<>(Arrays.asList(TechTile.values()));
    Collections.shuffle(techTiles, random);
    List<AdvancedTechTile> advTechTiles = new ArrayList<>(Arrays.asList(AdvancedTechTile.values()));
    Collections.shuffle(advTechTiles, random);
    List<FederationTile> federationTiles = new ArrayList<>(Arrays.asList(FederationTile.values()));
    Collections.shuffle(federationTiles, random);
    List<RoundScoringBonus> roundScoringBonuses = new ArrayList<>(Arrays.asList(RoundScoringBonus.values()));
    Collections.shuffle(roundScoringBonuses);
    List<EndScoring> endScoring = new ArrayList<>(Arrays.asList(EndScoring.values()));
    Collections.shuffle(endScoring, random);
    List<RoundBooster> allBoosters = new ArrayList<>(Arrays.asList(RoundBooster.values()));
    Collections.shuffle(allBoosters, random);

    Map<PlayerEnum, Player> players = new HashMap<>();
    players.put(PlayerEnum.PLAYER1, new Player(Race.XENOS, PlayerEnum.PLAYER1));
    players.put(PlayerEnum.PLAYER2, new Player(Race.TERRANS, PlayerEnum.PLAYER2));
    players.put(PlayerEnum.PLAYER3, new Player(Race.HADSCH_HALLAS, PlayerEnum.PLAYER3));

    return new Game(
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
      List<RoundBooster> roundBoosters,
      List<TechTile> techTiles,
      List<AdvancedTechTile> advancedTechTiles,
      FederationTile terraBonus,
      List<RoundScoringBonus> roundScoringBonuses,
      EndScoring endScoring1,
      EndScoring endScoring2,
      Map<PlayerEnum, Player> players) {
    this.roundBoosters = roundBoosters;
    this.techTiles = techTiles;
    this.advancedTechTiles = advancedTechTiles;
    this.terraBonus = terraBonus;
    this.roundScoringBonuses = roundScoringBonuses;
    this.endScoring1 = endScoring1;
    this.endScoring2 = endScoring2;
    this.players = players;

    this.currentRound = new SimpleObjectProperty<>(Round.SETUP);
    this.currentPlayerOrder = new ArrayList<>(Arrays.asList(PlayerEnum.values()));
    this.passedPlayers = new ArrayList<>();
    this.activePlayer = PlayerEnum.PLAYER1;
  }

  public List<RoundBooster> getRoundBoosters() {
    return roundBoosters;
  }

  public List<TechTile> getTechTiles() {
    return techTiles;
  }

  public List<AdvancedTechTile> getAdvancedTechTiles() {
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

  public List<PlayerEnum> getCurrentPlayerOrder() {
    return currentPlayerOrder;
  }

  public List<PlayerEnum> getPassedPlayers() {
    return passedPlayers;
  }

  public PlayerEnum getActivePlayer() {
    return activePlayer;
  }

  public void newRound() {
    currentRound.setValue(currentRound.getValue().nextRound());
    activePlayer = currentPlayerOrder.get(0);
  }

  public void nextActivePlayer() {
    Preconditions.checkArgument(passedPlayers.size() != currentPlayerOrder.size());

    // Keep cycling until we hit a player who has not passed...
    int currentIdx = currentPlayerOrder.indexOf(activePlayer);
    do {
      currentIdx = (currentIdx + 1) % currentPlayerOrder.size();
    } while (passedPlayers.contains(currentPlayerOrder.get(currentIdx)));

    this.activePlayer = currentPlayerOrder.get(currentIdx);
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
}
