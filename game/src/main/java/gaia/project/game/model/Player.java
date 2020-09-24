package gaia.project.game.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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
import gaia.project.game.controller.PlanetType;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

// Backing bean to store player's information
public class Player {
  private Race race;
  private PlayerEnum playerEnum;
  private IntegerProperty gaiaBin = new SimpleIntegerProperty();
  private IntegerProperty bin1 = new SimpleIntegerProperty();
  private IntegerProperty bin2 = new SimpleIntegerProperty();
  private IntegerProperty bin3 = new SimpleIntegerProperty();

  private IntegerProperty ore = new SimpleIntegerProperty();
  private IntegerProperty credits = new SimpleIntegerProperty();
  private IntegerProperty research = new SimpleIntegerProperty();
  private IntegerProperty qic = new SimpleIntegerProperty();

  private IntegerProperty availableGaiaformers = new SimpleIntegerProperty();

  private Income currentIncome;

  private IntegerProperty score = new SimpleIntegerProperty();

  private Property<RoundBooster> roundBooster = new SimpleObjectProperty<>();
  private boolean roundBoosterUsed;

  // Tech track related
  private IntegerProperty terraformingLevel = new SimpleIntegerProperty(0);
  private IntegerProperty navLevel = new SimpleIntegerProperty(0);
  private IntegerProperty aiLevel = new SimpleIntegerProperty(0);
  private IntegerProperty gaiaformingLevel = new SimpleIntegerProperty(0);
  private IntegerProperty econLevel = new SimpleIntegerProperty(0);
  private IntegerProperty knowledgeLevel = new SimpleIntegerProperty(0);
  private IntegerProperty terraCost = new SimpleIntegerProperty(3);
  private IntegerProperty navRange = new SimpleIntegerProperty(1);
  private IntegerProperty gaiaformerCost = new SimpleIntegerProperty(50);
  private IntegerProperty tempNavRange = new SimpleIntegerProperty(0);
  private IntegerProperty currentDigs = new SimpleIntegerProperty(0);

  // Tech tile related
  private ObservableSet<TechTile> techTiles = FXCollections.observableSet();
  private IntegerProperty bigBuildingPower = new SimpleIntegerProperty(3);
  private ObservableSet<PlanetType> builtOn = FXCollections.observableSet();
  private IntegerProperty gaiaPlanets = new SimpleIntegerProperty(0);

  // Boolean indicates whether the action has been used this round
  private ObservableMap<Enum<?>, BooleanProperty> specialActions = FXCollections.observableHashMap();
  private ObservableSet<AdvancedTechTile> advTechTiles = FXCollections.observableSet();
  private ObservableSet<TechTile> coveredTechTiles = FXCollections.observableSet();

  // Federation related
  private ObservableSet<Set<String>> federations = FXCollections.observableSet(new HashSet<>());
  private ObservableSet<String> satellites = FXCollections.observableSet();
  // Set of objects with the tile and whether not it still can be flipped...
  private ObservableSet<FedToken> federationTiles = FXCollections.observableSet();
  private int fedPower = 7;

  // Buildings, etc
  private ObservableSet<String> mines = FXCollections.observableSet();
  private ObservableSet<String> tradingPosts = FXCollections.observableSet();
  private ObservableSet<String> researchLabs = FXCollections.observableSet();
  private ObservableSet<String> pi = FXCollections.observableSet();
  private ObservableSet<String> ka = FXCollections.observableSet();
  private ObservableSet<String> qa = FXCollections.observableSet();
  private ObservableSet<String> gaiaformers = FXCollections.observableSet();
  private ObservableSet<String> lostPlanet = FXCollections.observableSet();

  // End Scoring Related
  private ObservableSet<Integer> sectors = FXCollections.observableSet();
  private IntegerProperty totalBuildings = new SimpleIntegerProperty(0);
  private IntegerProperty buildingsInFeds = new SimpleIntegerProperty(0);
  private Map<String, Integer> scoreLog = new HashMap<>();

  public static Player create(Race race, PlayerEnum playerEnum) {
    Player p = new Player();
    p.fromRace(race, playerEnum);

    return p;
  }

  public static Player empty() {
    return new Player();
  }

  protected void fromRace(Race race, PlayerEnum playerEnum) {
    this.race = race;
    this.playerEnum = playerEnum;
    gaiaBin.setValue(0);
    bin1.setValue(race.getStartingBin1());
    bin2.setValue(race.getStartingBin2());
    bin3.setValue(race.getStartingBin3());
    ore.setValue(race.getStartingOre());
    credits.setValue(race.getStartingCredits());
    research.setValue(race.getStartingKnowledge());
    qic.setValue(race.getStartingQic());
    availableGaiaformers.setValue(gaiaformingLevel.get());
    currentIncome = Income.fromRace(race);
    score.setValue(0);

    // This has to be run before setting up tech bonuses so that Gleens QIC gets transformed
    addAdditionalListeners();

    // We set up tech bonuses so that when we add race starting techs we get the bonus
    setupTechBonuses();
    terraformingLevel.setValue(race.getStartingTerraformingLevel());
    navLevel.setValue(race.getStartingNavLevel());
    aiLevel.setValue(race.getStartingAiLevel());
    gaiaformingLevel.setValue(race.getStartingGaiaformingLevel());
    econLevel.setValue(race.getStartingEconLevel());
    knowledgeLevel.setValue(race.getStartingKnowledgeLevel());
  }

  protected Player() {}

  private void setupTechBonuses() {
    terraformingLevel.addListener((o, oldValue, newValue) -> {
      switch (newValue.intValue()) {
        case 1:
        case 4:
          Util.plus(ore, 2);
          break;
        case 3:
          chargePower(3);
          // Fall through
        case 2:
          terraCost.setValue(4 - newValue.intValue());
          break;
        case 5:
          exhaustFederationTile();
          // Handled by TechTracks.java
          break;
      }
    });

    navLevel.addListener((o, oldValue, newValue) -> {
      switch (newValue.intValue()) {
        case 3:
          chargePower(3);
          // Fall through
        case 1:
          Util.plus(qic, 1);
          break;
        case 2:
          navRange.setValue(2);
          break;
        case 4:
          navRange.setValue(3);
          break;
        case 5:
          exhaustFederationTile();
          navRange.setValue(4);
          // Lost planet handled via UI
          break;
      }
    });

    aiLevel.addListener((o, oldValue, newValue) -> {
      switch (newValue.intValue()) {
        case 1:
        case 2:
          Util.plus(qic, 1);
          break;
        case 3:
          chargePower(3);
          // Fall through
        case 4:
          Util.plus(qic, 2);
          break;
        case 5:
          exhaustFederationTile();
          Util.plus(qic, 4);
          break;
      }
    });

    gaiaformingLevel.addListener((o, oldValue, newValue) -> {
      switch (newValue.intValue()) {
        case 1:
          gaiaformerCost.setValue(6);
          availableGaiaformers.setValue(availableGaiaformers.getValue() + 1);
          break;
        case 2:
          Util.plus(bin1, 3);
          break;
        case 3:
          chargePower(3);
          gaiaformerCost.setValue(4);
          availableGaiaformers.setValue(availableGaiaformers.getValue() + 1);
          break;
        case 4:
          gaiaformerCost.setValue(3);
          availableGaiaformers.setValue(availableGaiaformers.getValue() + 1);
          break;
        case 5:
          exhaustFederationTile();
          updateScore(4 + gaiaPlanets.getValue(), "GF Level 5");
          break;
      }
    });

    econLevel.addListener((o, oldValue, newValue) -> {
      switch (newValue.intValue()) {
        case 1:
          currentIncome.getCreditIncome().setValue(currentIncome.getCreditIncome().getValue() + 2);
          currentIncome.getChargeIncome().setValue(currentIncome.getChargeIncome().getValue() + 1);
          break;
        case 2:
          currentIncome.getOreIncome().setValue(currentIncome.getOreIncome().getValue() + 1);
          currentIncome.getChargeIncome().setValue(currentIncome.getChargeIncome().getValue() + 1);
          break;
        case 3:
          chargePower(3);
          currentIncome.getCreditIncome().setValue(currentIncome.getCreditIncome().getValue() + 1);
          currentIncome.getChargeIncome().setValue(currentIncome.getChargeIncome().getValue() + 1);
          break;
        case 4:
          currentIncome.getCreditIncome().setValue(currentIncome.getCreditIncome().getValue() + 1);
          currentIncome.getOreIncome().setValue(currentIncome.getOreIncome().getValue() + 1);
          currentIncome.getChargeIncome().setValue(currentIncome.getChargeIncome().getValue() + 1);
          break;
        case 5:
          exhaustFederationTile();
          currentIncome.getCreditIncome().setValue(currentIncome.getCreditIncome().getValue() - 4);
          currentIncome.getOreIncome().setValue(currentIncome.getOreIncome().getValue() - 2);
          currentIncome.getChargeIncome().setValue(currentIncome.getChargeIncome().getValue() - 4);
          Util.plus(ore, 3);
          Util.plus(credits, 6);
          chargePower(6);
          break;
      }
    });

    knowledgeLevel.addListener((o, oldValue, newValue) -> {
      switch (newValue.intValue()) {
        case 3:
          chargePower(3);
          // Fall through
        case 1:
        case 2:
        case 4:
          currentIncome.getResearchIncome().setValue(currentIncome.getResearchIncome().getValue() + 1);
          break;
        case 5:
          exhaustFederationTile();
          currentIncome.getResearchIncome().setValue(currentIncome.getResearchIncome().getValue() - 4);
          // Have to do this so it doesn't erroneously overflow 15 cap
          Platform.runLater(() -> Util.plus(research, 9));
          break;
      }
    });
  }

  private void addAdditionalListeners() {
    // Re-add listeners from tech tiles and anything race-specific
    ore.addListener((o, oldValue, newValue) -> {
      if (newValue.intValue() > 15) {
        ore.setValue(15);
      }

      if (newValue.intValue() < 0) {
        throw new IllegalStateException("Ore < 0");
      }
    });

    credits.addListener((o, oldValue, newValue) -> {
      if (newValue.intValue() > 30) {
        credits.setValue(30);
      }

      if (newValue.intValue() < 0) {
        throw new IllegalStateException("Credits < 0");
      }
    });

    research.addListener((o, oldValue, newValue) -> {
      if (newValue.intValue() > 15) {
        research.setValue(15);
      }

      if (newValue.intValue() < 0) {
        throw new IllegalStateException("Research < 0");
      }
    });

    techTiles.stream().filter(TechTile::addsListener).forEach(tt -> tt.addTo(this));
    advTechTiles.stream().filter(AdvancedTechTile::addsListener).forEach(tt -> tt.updatePlayer(this));

    raceSpecificListeners();
  }

  protected void raceSpecificListeners() {
    // Override for race-specific abilities
  }

  public Race getRace() {
    return race;
  }

  public PlayerEnum getPlayerEnum() {
    return playerEnum;
  }

  // Major functionality methods
  public void addFederationTile(FederationTile federationTile) {
    federationTile.updatePlayer(this);
    federationTiles.add(new FedToken(federationTile, federationTile.isFlippable()));
  }

  public void leechPower(int power) {
    chargePower(power);
    updateScore(-1 * (power - 1), "Leech");
  }

  public int canCharge() {
    return 2 * bin1.get() + bin2.get();
  }

  public void chargePower(int toCharge) {
    Preconditions.checkArgument(toCharge > 0);
    int remainingCharge = toCharge;
    if (bin1.getValue().intValue() > 0) {
      do {
        Util.minus(bin1, 1);
        Util.plus(bin2, 1);
        --remainingCharge;
      } while (bin1.getValue().intValue() > 0 && remainingCharge > 0);
    }

    if (remainingCharge > 0 && bin2.getValue().intValue() > 0) {
      do {
        Util.minus(bin2, 1);
        Util.plus(bin3, 1);
        --remainingCharge;
      } while (bin2.getValue().intValue() > 0 && remainingCharge > 0);
    }
  }

  public void sacPower(int numTimes) {
    Preconditions.checkArgument(numTimes <= bin2.intValue() / 2);

    bin2.setValue(bin2.getValue() - numTimes * 2);
    bin3.setValue(bin3.getValue() + numTimes);
  }

  public void spendPower(int numPower) {
    Util.minus(bin3, numPower);
    Util.plus(bin1, numPower);
  }

  public void takeIncome() {
    currentIncome.updatePlayer(this);
    // Remove this so it doesn't show up for next turn's predicted income
    this.roundBooster.getValue().removeIncome(currentIncome);
  }

  // Getters for properties
  public IntegerProperty getGaiaBin() {
    return gaiaBin;
  }

  public IntegerProperty getBin1() {
    return bin1;
  }

  public IntegerProperty getBin2() {
    return bin2;
  }

  public IntegerProperty getBin3() {
    return bin3;
  }

  public IntegerProperty getOre() {
    return ore;
  }

  public IntegerProperty getCredits() {
    return credits;
  }

  public IntegerProperty getResearch() {
    return research;
  }

  public IntegerProperty getQic() {
    return qic;
  }

  public IntegerProperty getScore() {
    return score;
  }

  public Map<String, Integer> getScoreLog() {
    return scoreLog;
  }

  public void updateScore(int update, String source) {
    Util.plus(score, update);
    scoreLog.merge(source, update, (a, b) -> a + b);
  }

  public boolean hasFlippableFederationTile() {
    return federationTiles.stream().anyMatch(ft -> ft.getFlippable().get());
  }

  public Income getCurrentIncome() {
    return currentIncome;
  }

  public ObservableSet<FedToken> getFederationTiles() {
    return federationTiles;
  }

  public ObservableSet<Set<String>> getFederations() {
    return federations;
  }

  public ObservableSet<TechTile> getTechTiles() {
    return techTiles;
  }

  public IntegerProperty getBigBuildingPower() {
    return bigBuildingPower;
  }

  public ObservableSet<PlanetType> getBuiltOn() {
    return builtOn;
  }

  public ObservableSet<String> getMines() {
    return mines;
  }

  public ObservableSet<String> getTradingPosts() {
    return tradingPosts;
  }

  public ObservableSet<String> getResearchLabs() {
    return researchLabs;
  }

  public ObservableSet<String> getPi() {
    return pi;
  }

  public ObservableSet<String> getKa() {
    return ka;
  }

  public ObservableSet<String> getQa() {
    return qa;
  }

  public ObservableSet<String> getLostPlanet() {
    return lostPlanet;
  }

  public IntegerProperty getAvailableGaiaformers() {
    return availableGaiaformers;
  }

  public int getGaiaformerCost() {
    return gaiaformerCost.get();
  }

  public ObservableSet<String> getGaiaformers() {
    return gaiaformers;
  }

  public IntegerProperty getTerraformingLevel() {
    return terraformingLevel;
  }

  public IntegerProperty getNavLevel() {
    return navLevel;
  }

  public IntegerProperty getAiLevel() {
    return aiLevel;
  }

  public IntegerProperty getGaiaformingLevel() {
    return gaiaformingLevel;
  }

  public IntegerProperty getEconLevel() {
    return econLevel;
  }

  public IntegerProperty getKnowledgeLevel() {
    return knowledgeLevel;
  }

  public IntegerProperty getGaiaPlanets() {
    return gaiaPlanets;
  }

  public IntegerProperty getNavRange() {
    return navRange;
  }

  public IntegerProperty getTempNavRange() {
    return tempNavRange;
  }

  public IntegerProperty getCurrentDigs() {
    return currentDigs;
  }

  public void setRoundBooster(RoundBooster roundBooster) {
    roundBoosterVps();

    // Add new income
    roundBooster.addIncome(currentIncome);
    this.roundBooster.setValue(roundBooster);
  }

  public void roundBoosterVps() {
    if (this.roundBooster.getValue() != null) {
      this.roundBooster.getValue().addVps(this);
    }

    for (AdvancedTechTile tt : advTechTiles) {
      tt.endOfRound(this);
    }
  }

  public RoundBooster getRoundBooster() {
    return roundBooster.getValue();
  }

  public boolean roundBoosterUsed() {
    return roundBoosterUsed;
  }

  public void setRoundBoosterUsed() {
    roundBoosterUsed = true;
  }

  public ObservableSet<Integer> getSectors() {
    return sectors;
  }

  public ObservableSet<String> getSatellites() {
    return satellites;
  }

  public IntegerProperty getTotalBuildings() {
    return totalBuildings;
  }

  public IntegerProperty getBuildingsInFeds() {
    return buildingsInFeds;
  }

  public ObservableMap<Enum<?>, BooleanProperty> getSpecialActions() {
    return specialActions;
  }

  public int getFedPower() {
    return fedPower;
  }

  public ObservableSet<AdvancedTechTile> getAdvTechTiles() {
    return advTechTiles;
  }

  public ObservableSet<TechTile> getCoveredTechTiles() {
    return coveredTechTiles;
  }

  // Override for Firaks downgrade to TP
  public boolean ignoreTpRoundBonus() {
    return false;
  }

  // Override for Ambas PI switch ability
  public boolean ignorePiAndMineBonuses() {
    return false;
  }

  // Utility methods
  public Set<String> allBuildingLocations() {
    return ImmutableSet.<String> builder()
        .addAll(mines)
        .addAll(tradingPosts)
        .addAll(researchLabs)
        .addAll(pi)
        .addAll(ka)
        .addAll(qa)
        .addAll(lostPlanet)
        .build();
  }

  public boolean canDigTo(HexWithPlanet hex) {
    Preconditions.checkArgument(!hex.isEmpty());
    PlanetType planetType = hex.getPlanet().getPlanetType();
    if (planetType == PlanetType.TRANSDIM) {
      return false;
    }

    if (planetType == PlanetType.GAIA) {
      return qic.get() > 0 && !(hex.hasGaiaformer() && hex.getBuilder().get() != playerEnum);
    }

    return race.getHomePlanet().numDigsTo(planetType) - currentDigs.intValue() <= (ore.intValue() - 1)
        / terraCost.intValue();
  }

  public boolean canBuildMine() {
    return mines.size() < 8 && ore.get() > 0 && credits.get() > 1;
  }

  public boolean canGaiaform() {
    return availableGaiaformers.get() > gaiaformers.size()
        && bin1.get() + bin2.get() + bin3.get() >= gaiaformerCost.get();
  }

  protected boolean gaiaformed(Hex hex) {
    return gaiaformers.stream().anyMatch(c -> hex.getHexId().equals(c));
  }

  public void endTurn() {
    currentDigs.setValue(0);
    tempNavRange.setValue(0);
  }

  public void clearSpecialActions() {
    // Should set each value to false
    specialActions.keySet().forEach(k -> specialActions.get(k).setValue(false));
    roundBoosterUsed = false;
  }

  public int getPower(Hex hex) {
    if (Sets.union(mines, lostPlanet).contains(hex.getHexId())) {
      return 1;
    }

    if (Sets.union(tradingPosts, researchLabs).contains(hex.getHexId())) {
      return 2;
    }

    if (Sets.union(Sets.union(pi, qa), ka).contains(hex.getHexId())) {
      return bigBuildingPower.intValue();
    }

    return 0;
  }

  public int getPowerGain(Integer maybeLeech) {
    return Math.min(maybeLeech, canCharge());
  }

  public int spendablePower() {
    return bin3.get();
  }

  public boolean canBuildSatellite() {
    return bin1.get() + bin2.get() + bin3.get() > 0;
  }

  public boolean couldFederate() {
    return getExcessBuildingPower() >= getFedPower();
  }

  protected int getExcessBuildingPower() {
    int totalPower = 0;

    for (String coords : Sets.union(mines, lostPlanet)) {
      if (!inFederation(coords)) {
        totalPower += 1;
      }
    }

    for (String coords : Sets.union(tradingPosts, researchLabs)) {
      if (!inFederation(coords)) {
        totalPower += 2;
      }
    }

    for (String coords : Sets.union(Sets.union(pi, qa), ka)) {
      if (!inFederation(coords)) {
        totalPower += bigBuildingPower.intValue();
      }
    }

    return totalPower;
  }

  public boolean inFederation(String coords) {
    for (Set<String> federation : federations) {
      for (String inFed : federation) {
        if (inFed.equals(coords)) {
          return true;
        }
      }
    }

    return false;
  }

  public void exhaustFederationTile() {
    Preconditions.checkArgument(hasFlippableFederationTile());
    for (FedToken fedToken : federationTiles) {
      if (fedToken.getFlippable().get()) {
        fedToken.getFlippable().setValue(false);
        break;
      }
    }
  }

  // Action methods
  public void buildMine(HexWithPlanet hex, GameBoard gameBoard) {
    buildMine(hex, false, gameBoard);
  }

  public void buildSetupMine(HexWithPlanet hex, GameBoard gameBoard) {
    buildMine(hex, true, gameBoard);
  }

  private void buildMine(HexWithPlanet hex, boolean setup, GameBoard gameBoard) {
    Mine mine = new Mine(hex, race.getColor(), playerEnum);
    mines.add(hex.getHexId());
    hex.addMine(mine);

    // Update income
    if (mines.size() != 3) {
      Util.plus(currentIncome.getOreIncome(), 1);
    }

    // Update planet counts
    PlanetType planetType = hex.getPlanet().getPlanetType();
    builtOn.add(planetType);
    if (planetType == PlanetType.GAIA) {
      Util.plus(gaiaPlanets, 1);
    }
    Util.plus(totalBuildings, 1);
    sectors.add(hex.getSectorId());

    if (!setup) {
      // Handle cost for transformation
      if (planetType == PlanetType.GAIA) {
        if (gaiaformed(hex)) {
          gaiaformers.remove(hex.getHexId());
        } else {
          race.gaiaTerraformCost().updatePlayer(this);
        }
      } else {
        if (race.getHomePlanet().numDigsTo(planetType) > currentDigs.intValue()) {
          int diff = race.getHomePlanet().numDigsTo(planetType) - currentDigs.intValue();
          Util.plus(currentDigs, diff);
          Util.minus(ore, diff * terraCost.intValue());
        }
      }

      // Pay mine cost
      Util.minus(ore, 1);
      Util.minus(credits, 2);

      for (Set<String> federation : federations) {
        if (federation.stream().anyMatch(c -> hex.isWithinRangeOf(gameBoard.hexWithId(c), 1))) {
          federation.add(hex.getHexId());
        }
      }

      for (String sat : satellites) {
        if (hex.isWithinRangeOf(gameBoard.hexWithId(sat), 1)) {
          // Just add to the first possible fed for now. Shouldn't matter
          // TODO: Check and add logic to check which fed later
          federations.iterator().next().add(hex.getHexId());
        }
      }

      // We might have added something that causes more additions to an existing federation
      recheckAllBuildingsInFederations(gameBoard);
    }
  }

  protected void recheckAllBuildingsInFederations(GameBoard gameBoard) {
    if (!federations.isEmpty()) {
      Set<String> toAdd = new HashSet<>();
      do {
        toAdd.clear();
        for (String building : allBuildingLocations()) {
          Hex hex = gameBoard.hexWithId(building);
          if (!inFederation(building)) {
            for (Set<String> federation : getFederations()) {
              for (String hexId : federation) {
                if (hex.isWithinRangeOf(gameBoard.hexWithId(hexId), 1)) {
                  toAdd.add(building);
                }
              }
            }
          }
        }

        // TODO: Add to first federation - is this ok?!?
        getFederations().iterator().next().addAll(toAdd);
      } while (!toAdd.isEmpty());
    }
  }

  public void addGaiaformer(HexWithPlanet hex) {
    Preconditions.checkArgument(bin1.get() + bin2.get() + bin3.get() >= gaiaformerCost.get());
    Gaiaformer gaiaformer = new Gaiaformer(hex, race.getColor(), playerEnum);
    hex.addGaiaformer(gaiaformer);

    gaiaformers.add(hex.getHexId());

    if (bin1.get() >= gaiaformerCost.get()) {
      Util.minus(bin1, gaiaformerCost.get());
    } else {
      int remainingPower = gaiaformerCost.get() - bin1.get();
      bin1.setValue(0);
      if (bin2.get() >= remainingPower) {
        Util.minus(bin2, remainingPower);
      } else {
        remainingPower = remainingPower - bin2.get();
        bin2.setValue(0);
        Util.minus(bin3, remainingPower);
      }
    }

    Util.plus(gaiaBin, gaiaformerCost.get());
  }

  public void gaiaPhase() {
    Util.plus(bin1, gaiaBin.get());
    gaiaBin.setValue(0);
  }

  public void advanceTech(IntegerProperty techTrack, boolean free) {
    techTrack.setValue(techTrack.getValue() + 1);
    if (!free) {
      research.setValue(research.getValue() - 4);
    }
  }

  public void buildTradingPost(HexWithPlanet hex, boolean cheap) {
    TradingPost tp = new TradingPost(hex, race.getColor(), playerEnum);
    hex.switchBuildingUI(tp);
    tradingPosts.add(hex.getHexId());
    mines.remove(hex.getHexId());

    Util.minus(ore, 2);
    Util.minus(credits, cheap ? 3 : 6);

    // Update Income
    if (mines.size() != 2) {
      Util.minus(currentIncome.getOreIncome(), 1);
    }
    race.getTpIncome().get(tradingPosts.size() - 1).addTo(currentIncome);
  }

  public void buildResearchLab(HexWithPlanet hex) {
    ResearchLab rl = new ResearchLab(hex, race.getColor(), playerEnum);
    hex.switchBuildingUI(rl);
    researchLabs.add(hex.getHexId());
    tradingPosts.remove(hex.getHexId());

    Util.minus(ore, 3);
    Util.minus(credits, 5);

    // Update Income
    race.getTpIncome().get(tradingPosts.size()).removeFrom(currentIncome);
    race.getRlIncome().get(researchLabs.size() - 1).addTo(currentIncome);
  }

  public void buildPI(HexWithPlanet hex) {
    PlanetaryInstitute pi = new PlanetaryInstitute(hex, race.getColor(), playerEnum);
    hex.switchBuildingUI(pi);
    this.pi.add(hex.getHexId());
    tradingPosts.remove(hex.getHexId());

    Util.minus(ore, 4);
    Util.minus(credits, 6);

    // Update income
    race.getTpIncome().get(tradingPosts.size()).removeFrom(currentIncome);
    race.getPiIncome().addTo(currentIncome);
  }

  public void buildAcademy(HexWithPlanet hex, boolean ka) {
    Academy academy = new Academy(hex, race.getColor(), playerEnum);
    hex.switchBuildingUI(academy);
    if (ka) {
      this.ka.add(hex.getHexId());
    } else {
      this.qa.add(hex.getHexId());
    }
    researchLabs.remove(hex.getHexId());

    Util.minus(ore, 6);
    Util.minus(credits, 6);

    // Update Income
    race.getRlIncome().get(researchLabs.size()).removeFrom(currentIncome);
    if (ka) {
      Util.plus(currentIncome.getResearchIncome(), race.getKaIncome());
    } else {
      specialActions.put(race.getQaAction(), new SimpleBooleanProperty(false));
    }
  }

  public void addSatellite(EmptyHex emptyHex) {
    Preconditions.checkArgument(bin1.get() + bin2.get() + bin3.get() > 0);
    satellites.add(emptyHex.getHexId());
    Satellite satellite = new Satellite(emptyHex, race.getColor(), playerEnum);
    emptyHex.addSatelliteUI(satellite);

    if (bin1.get() > 0) {
      Util.minus(bin1, 1);
    } else if (bin2.get() > 0) {
      Util.minus(bin2, 1);
    } else {
      Util.minus(bin3, 1);
    }
  }

  public void takeSpecialAction(Enum<?> specialAction) {
    ((UpdatePlayer) specialAction).updatePlayer(this);

    specialActions.get(specialAction).setValue(true);
  }

  // END GAME
  public void convertResourcesToVps() {
    if (bin2.intValue() > 1) {
      sacPower(bin2.intValue() / 2);
    }

    updateScore(
        (credits.intValue() + ore.intValue() + research.intValue() + qic.intValue() + bin3.intValue()) / 3,
        "Resources");
  }

  public void techScoring() {
    int points = 0;

    points += terraformingLevel.get() > 2 ? 4 * (terraformingLevel.get() - 2) : 0;
    points += navLevel.get() > 2 ? 4 * (navLevel.get() - 2) : 0;
    points += aiLevel.get() > 2 ? 4 * (aiLevel.get() - 2) : 0;
    points += gaiaformingLevel.get() > 2 ? 4 * (gaiaformingLevel.get() - 2) : 0;
    points += econLevel.get() > 2 ? 4 * (econLevel.get() - 2) : 0;
    points += knowledgeLevel.get() > 2 ? 4 * (knowledgeLevel.get() - 2) : 0;

    updateScore(points, "Tech Scoring");
  }

  public void addLostPlanet(EmptyHex hex) {
    lostPlanet.add(hex.getHexId());

    // Update planet counts
    builtOn.add(PlanetType.LOST);
    Util.plus(totalBuildings, 1);
    sectors.add(hex.getSectorId());
  }

  // Serialization
  public final void write(JsonWriter json) throws IOException {
    json.beginObject();

    json.name(JsonUtil.RACE).value(race.name());
    json.name(JsonUtil.PLAYER_ENUM).value(playerEnum.name());

    json.name(JsonUtil.GAIA_BIN).value(gaiaBin.get());
    json.name(JsonUtil.BIN1).value(bin1.get());
    json.name(JsonUtil.BIN2).value(bin2.get());
    json.name(JsonUtil.BIN3).value(bin3.get());
    json.name(JsonUtil.ORE).value(ore.get());
    json.name(JsonUtil.CREDITS).value(credits.get());
    json.name(JsonUtil.RESEARCH).value(research.get());
    json.name(JsonUtil.QIC).value(qic.get());
    json.name(JsonUtil.AVAILABLE_GAIAFORMERS).value(availableGaiaformers.get());
    json.name(JsonUtil.CURRENT_INCOME);
    currentIncome.write(json);
    json.name(JsonUtil.SCORE).value(score.get());
    if (roundBooster.getValue() != null) {
      json.name(JsonUtil.ROUND_BOOSTER).value(roundBooster.getValue().name());
      json.name(JsonUtil.ROUND_BOOSTER_USED).value(roundBoosterUsed);
    }

    // Tech track
    json.name(JsonUtil.TERRA_LEVEL).value(terraformingLevel.get());
    json.name(JsonUtil.NAV_LEVEL).value(navLevel.get());
    json.name(JsonUtil.AI_LEVEL).value(aiLevel.get());
    json.name(JsonUtil.GAIA_LEVEL).value(gaiaformingLevel.get());
    json.name(JsonUtil.ECON_LEVEL).value(econLevel.get());
    json.name(JsonUtil.K_LEVEL).value(knowledgeLevel.get());
    json.name(JsonUtil.TERRA_COST).value(terraCost.get());
    json.name(JsonUtil.NAV_RANGE).value(navRange.get());
    json.name(JsonUtil.GAIA_COST).value(gaiaformerCost.get());

    // Tech tile
    json.name(JsonUtil.BB_POWER).value(bigBuildingPower.get());
    JsonUtil.writeCollection(json, JsonUtil.BUILT_ON, builtOn, PlanetType::name);
    json.name(JsonUtil.GAIA_PLANETS).value(gaiaPlanets.get());
    JsonUtil.writeCollection(json, JsonUtil.TECH_TILES, techTiles, TechTile::name);
    JsonUtil.writeCollection(json, JsonUtil.COVERED_TECH_TILES, coveredTechTiles, TechTile::name);
    JsonUtil.writeCollection(json, JsonUtil.ADV_TECH_TILES, advTechTiles, AdvancedTechTile::name);
    json.name(JsonUtil.SPECIAL_ACTIONS).beginObject();
    for (Entry<Enum<?>, BooleanProperty> entry : specialActions.entrySet()) {
      json.name(entry.getKey().name()).value(entry.getValue().get());
    }
    json.endObject();

    // Federations
    json.name(JsonUtil.FEDERATIONS).beginArray();
    for (Set<String> federation : federations) {
      json.beginObject();
      JsonUtil.writeCoordsCollection(json, JsonUtil.FED, federation);
      json.endObject();
    }
    json.endArray();
    json.name(JsonUtil.FED_TILES).beginArray();
    for (FedToken fedToken : federationTiles) {
      json.beginObject().name(fedToken.federationTile.name()).value(fedToken.flippable.get()).endObject();
    }
    json.endArray();
    JsonUtil.writeCoordsCollection(json, JsonUtil.SATS, satellites);

    // Buildings
    JsonUtil.writeCoordsCollection(json, JsonUtil.MINES, mines);
    JsonUtil.writeCoordsCollection(json, JsonUtil.TPS, tradingPosts);
    JsonUtil.writeCoordsCollection(json, JsonUtil.RLS, researchLabs);
    JsonUtil.writeCoordsCollection(json, JsonUtil.PI, pi);
    JsonUtil.writeCoordsCollection(json, JsonUtil.KA, ka);
    JsonUtil.writeCoordsCollection(json, JsonUtil.QA, qa);
    JsonUtil.writeCoordsCollection(json, JsonUtil.GFS, gaiaformers);
    JsonUtil.writeCoordsCollection(json, JsonUtil.LP, lostPlanet);

    // End scoring
    JsonUtil.writeCollection(json, JsonUtil.SECTORS, sectors, String::valueOf);
    json.name(JsonUtil.BUILDINGS).value(totalBuildings.get());
    json.name(JsonUtil.BLDGS_IN_FEDS).value(buildingsInFeds.get());
    json.name(JsonUtil.SCORE_LOG).beginObject();
    for (Entry<String, Integer> e : scoreLog.entrySet()) {
      json.name(e.getKey()).value(e.getValue());
    }
    json.endObject();

    writeExtraContent(json);

    json.endObject();
  }

  protected void writeExtraContent(JsonWriter json) throws IOException {
    // Does nothing by default
  }

  public static Player read(Player p, JsonReader json) throws IOException {
    json.beginObject();
    handleContent(json, p);
    json.endObject();

    return p;
  }

  protected static void handleContent(JsonReader json, Player p) throws IOException {
    while (json.hasNext()) {
      String name = json.nextName();
      switch (name) {
        case JsonUtil.RACE:
          p.race = Race.valueOf(json.nextString());
          break;
        case JsonUtil.PLAYER_ENUM:
          p.playerEnum = PlayerEnum.valueOf(json.nextString());
          break;
        case JsonUtil.GAIA_BIN:
          p.gaiaBin.setValue(json.nextInt());
          break;
        case JsonUtil.BIN1:
          p.bin1.setValue(json.nextInt());
          break;
        case JsonUtil.BIN2:
          p.bin2.setValue(json.nextInt());
          break;
        case JsonUtil.BIN3:
          p.bin3.setValue(json.nextInt());
          break;
        case JsonUtil.ORE:
          p.ore.setValue(json.nextInt());
          break;
        case JsonUtil.CREDITS:
          p.credits.setValue(json.nextInt());
          break;
        case JsonUtil.RESEARCH:
          p.research.setValue(json.nextInt());
          break;
        case JsonUtil.QIC:
          p.qic.setValue(json.nextInt());
          break;
        case JsonUtil.AVAILABLE_GAIAFORMERS:
          p.availableGaiaformers.setValue(json.nextInt());
          break;
        case JsonUtil.CURRENT_INCOME:
          p.currentIncome = Income.read(json);
          break;
        case JsonUtil.SCORE:
          p.score.setValue(json.nextInt());
          break;
        case JsonUtil.ROUND_BOOSTER:
          p.roundBooster.setValue(RoundBooster.valueOf(json.nextString()));
          break;
        case JsonUtil.ROUND_BOOSTER_USED:
          p.roundBoosterUsed = json.nextBoolean();
          break;
        case JsonUtil.TERRA_LEVEL:
          p.terraformingLevel.setValue(json.nextInt());
          break;
        case JsonUtil.NAV_LEVEL:
          p.navLevel.setValue(json.nextInt());
          break;
        case JsonUtil.AI_LEVEL:
          p.aiLevel.setValue(json.nextInt());
          break;
        case JsonUtil.GAIA_LEVEL:
          p.gaiaformingLevel.setValue(json.nextInt());
          break;
        case JsonUtil.ECON_LEVEL:
          p.econLevel.setValue(json.nextInt());
          break;
        case JsonUtil.K_LEVEL:
          p.knowledgeLevel.setValue(json.nextInt());
          break;
        case JsonUtil.TERRA_COST:
          p.terraCost.setValue(json.nextInt());
          break;
        case JsonUtil.NAV_RANGE:
          p.navRange.setValue(json.nextInt());
          break;
        case JsonUtil.GAIA_COST:
          p.gaiaformerCost.setValue(json.nextInt());
          break;
        case JsonUtil.BB_POWER:
          p.bigBuildingPower.setValue(json.nextInt());
          break;
        case JsonUtil.BUILT_ON:
          JsonUtil.readStringArray(p.builtOn, json, PlanetType::valueOf);
          break;
        case JsonUtil.GAIA_PLANETS:
          p.gaiaPlanets.setValue(json.nextInt());
          break;
        case JsonUtil.TECH_TILES:
          JsonUtil.readStringArray(p.techTiles, json, TechTile::valueOf);
          break;
        case JsonUtil.SPECIAL_ACTIONS:
          JsonUtil.readSpecialActions(p.specialActions, json);
          break;
        case JsonUtil.COVERED_TECH_TILES:
          JsonUtil.readStringArray(p.coveredTechTiles, json, TechTile::valueOf);
          break;
        case JsonUtil.ADV_TECH_TILES:
          JsonUtil.readStringArray(p.advTechTiles, json, AdvancedTechTile::valueOf);
          break;
        case JsonUtil.FEDERATIONS:
          JsonUtil.readSetOfSets(p.federations, json);
          break;
        case JsonUtil.FED_TILES:
          JsonUtil.readFedTiles(p.federationTiles, json);
          break;
        case JsonUtil.SATS:
          JsonUtil.readCoordsArray(p.satellites, json);
          break;
        case JsonUtil.MINES:
          JsonUtil.readCoordsArray(p.mines, json);
          break;
        case JsonUtil.TPS:
          JsonUtil.readCoordsArray(p.tradingPosts, json);
          break;
        case JsonUtil.RLS:
          JsonUtil.readCoordsArray(p.researchLabs, json);
          break;
        case JsonUtil.PI:
          JsonUtil.readCoordsArray(p.pi, json);
          break;
        case JsonUtil.KA:
          JsonUtil.readCoordsArray(p.ka, json);
          break;
        case JsonUtil.QA:
          JsonUtil.readCoordsArray(p.qa, json);
          break;
        case JsonUtil.GFS:
          JsonUtil.readCoordsArray(p.gaiaformers, json);
          break;
        case JsonUtil.LP:
          JsonUtil.readCoordsArray(p.lostPlanet, json);
          break;
        case JsonUtil.SECTORS:
          JsonUtil.readIntegerArray(p.sectors, json);
          break;
        case JsonUtil.BUILDINGS:
          p.totalBuildings.setValue(json.nextInt());
          break;
        case JsonUtil.BLDGS_IN_FEDS:
          p.buildingsInFeds.setValue(json.nextInt());
          break;
        case JsonUtil.SCORE_LOG:
          JsonUtil.readScoreLog(p.scoreLog, json);
          break;
        default:
          p.handleAdditionalContent(name, json);
          break;
      }
    }

    p.addAdditionalListeners();
    p.setupTechBonuses();
  }

  protected void handleAdditionalContent(String name, JsonReader json) throws IOException {
    // Override in subclasses to add new fields
    throw new IllegalStateException("Unsupported key: " + name);
  }

  public static class FedToken {
    private final FederationTile federationTile;
    private BooleanProperty flippable;

    public FedToken(FederationTile federationTile, boolean flippable) {
      this.federationTile = federationTile;
      this.flippable = new SimpleBooleanProperty(flippable);
    }

    public FederationTile getFederationTile() {
      return federationTile;
    }

    public BooleanProperty getFlippable() {
      return flippable;
    }
  }
}
