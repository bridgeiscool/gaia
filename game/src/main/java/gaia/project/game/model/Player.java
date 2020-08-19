package gaia.project.game.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import gaia.project.game.PlanetType;
import gaia.project.game.board.Academy;
import gaia.project.game.board.EmptyHex;
import gaia.project.game.board.Gaiaformer;
import gaia.project.game.board.Hex;
import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.Mine;
import gaia.project.game.board.PlanetaryInstitute;
import gaia.project.game.board.ResearchLab;
import gaia.project.game.board.Satellite;
import gaia.project.game.board.TradingPost;
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
public class Player implements Serializable {
  private static final long serialVersionUID = -5027584264184182306L;

  private final Race race;
  private final PlayerEnum playerEnum;
  private transient IntegerProperty gaiaBin;
  private transient IntegerProperty bin1;
  private transient IntegerProperty bin2;
  private transient IntegerProperty bin3;

  private transient IntegerProperty ore;
  private transient IntegerProperty credits;
  private transient IntegerProperty research;
  private transient IntegerProperty qic;

  private transient IntegerProperty availableGaiaformers;

  private final Income currentIncome;

  private transient IntegerProperty score;

  private transient Property<RoundBooster> roundBooster = new SimpleObjectProperty<>();
  private boolean roundBoosterUsed;

  // Tech track related
  private transient IntegerProperty terraformingLevel = new SimpleIntegerProperty(0);
  private transient IntegerProperty navLevel = new SimpleIntegerProperty(0);
  private transient IntegerProperty aiLevel = new SimpleIntegerProperty(0);
  private transient IntegerProperty gaiaformingLevel = new SimpleIntegerProperty(0);
  private transient IntegerProperty econLevel = new SimpleIntegerProperty(0);
  private transient IntegerProperty knowledgeLevel = new SimpleIntegerProperty(0);
  private transient IntegerProperty terraCost = new SimpleIntegerProperty(3);
  private transient IntegerProperty navRange = new SimpleIntegerProperty(1);
  private transient IntegerProperty gaiaformerCost = new SimpleIntegerProperty(50);
  private transient IntegerProperty tempNavRange = new SimpleIntegerProperty(0);
  private transient IntegerProperty currentDigs = new SimpleIntegerProperty(0);

  // Tech tile related
  private transient ObservableSet<TechTile> techTiles = FXCollections.observableSet();
  private transient IntegerProperty bigBuildingPower = new SimpleIntegerProperty(3);
  private transient ObservableSet<PlanetType> builtOn = FXCollections.observableSet();
  private transient IntegerProperty gaiaPlanets = new SimpleIntegerProperty(0);
  // Boolean indicates whether the action has been used this round
  private transient ObservableMap<Serializable, Boolean> specialActions = FXCollections.observableHashMap();

  // Federation related
  private transient ObservableSet<Set<Coords>> federations = FXCollections.observableSet(new HashSet<>());
  private transient ObservableSet<Coords> satellites = FXCollections.observableSet();
  // Map of tile and whether not it still can be flipped...
  private transient ObservableSet<FedToken> federationTiles = FXCollections.observableSet();
  private int fedPower = 7;

  // Buildings, etc
  private transient ObservableSet<Coords> mines = FXCollections.observableSet();
  private transient ObservableSet<Coords> tradingPosts = FXCollections.observableSet();
  private transient ObservableSet<Coords> researchLabs = FXCollections.observableSet();
  private transient ObservableSet<Coords> pi = FXCollections.observableSet();
  private transient ObservableSet<Coords> ka = FXCollections.observableSet();
  private transient ObservableSet<Coords> qa = FXCollections.observableSet();
  private transient ObservableSet<Coords> gaiaformers = FXCollections.observableSet();

  // Income related
  private final List<IncomeUpdater> tpIncome;
  private final List<IncomeUpdater> rlIncome;
  private final IncomeUpdater piIncome;
  private final int kaIncome;

  // Scoring Related
  private transient ObservableSet<Integer> sectors = FXCollections.observableSet();
  private transient IntegerProperty totalBuildings = new SimpleIntegerProperty(0);
  private transient IntegerProperty buildingsInFeds = new SimpleIntegerProperty(0);
  private transient IntegerProperty projectedTechScoring = new SimpleIntegerProperty(0);

  public Player(Race race, PlayerEnum playerEnum) {
    this.race = race;
    this.playerEnum = playerEnum;
    this.gaiaBin = new SimpleIntegerProperty(0);
    this.bin1 = new SimpleIntegerProperty(race.getStartingBin1());
    this.bin2 = new SimpleIntegerProperty(race.getStartingBin2());
    this.bin3 = new SimpleIntegerProperty(race.getStartingBin3());
    this.ore = new SimpleIntegerProperty(race.getStartingOre());
    this.ore.addListener((o, oldValue, newValue) -> {
      if (newValue.intValue() > 15) {
        this.ore.setValue(15);
      }
    });
    this.credits = new SimpleIntegerProperty(race.getStartingCredits());
    this.credits.addListener((o, oldValue, newValue) -> {
      if (newValue.intValue() > 30) {
        this.credits.setValue(30);
      }
    });
    this.research = new SimpleIntegerProperty(race.getStartingKnowledge());
    this.research.addListener((o, oldValue, newValue) -> {
      if (newValue.intValue() > 15) {
        this.research.setValue(15);
      }
    });
    this.qic = new SimpleIntegerProperty(race.getStartingQic());
    this.availableGaiaformers = new SimpleIntegerProperty(this.gaiaformingLevel.get());
    this.currentIncome = new Income(race);
    this.score = new SimpleIntegerProperty(10);
    this.tpIncome = race.getTpIncome();
    this.rlIncome = race.getRlIncome();
    this.piIncome = race.getPiIncome();
    this.kaIncome = race.getKaIncome();

    // We set up tech bonuses so that when we add race starting techs we get the bonus
    setupTechBonuses();
    this.terraformingLevel.setValue(race.getStartingTerraformingLevel());
    this.navLevel.setValue(race.getStartingNavLevel());
    this.aiLevel.setValue(race.getStartingAiLevel());
    this.gaiaformingLevel.setValue(race.getStartingGaiaformingLevel());
    this.econLevel.setValue(race.getStartingEconLevel());
    this.knowledgeLevel.setValue(race.getStartingKnowledgeLevel());
  }

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
          System.out.println("Implement gaining federationToken!");
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
          System.out.println("Implement gaining lonely planet!");
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
          score.setValue(score.getValue() + 4 + gaiaPlanets.getValue());
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
          Util.plus(research, 9);
          break;
      }
    });
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
    Util.minus(score, power - 1);
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

  public boolean hasFlippableFederationTile() {
    return federationTiles.stream().anyMatch(ft -> ft.flippable.get());
  }

  public Income getCurrentIncome() {
    return currentIncome;
  }

  public ObservableSet<FedToken> getFederationTiles() {
    return federationTiles;
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

  public ObservableSet<Coords> getMines() {
    return mines;
  }

  public ObservableSet<Coords> getTradingPosts() {
    return tradingPosts;
  }

  public ObservableSet<Coords> getResearchLabs() {
    return researchLabs;
  }

  public ObservableSet<Coords> getPi() {
    return pi;
  }

  public ObservableSet<Coords> getKa() {
    return ka;
  }

  public ObservableSet<Coords> getQa() {
    return qa;
  }

  public IntegerProperty getAvailableGaiaformers() {
    return availableGaiaformers;
  }

  public int getGaiaformerCost() {
    return gaiaformerCost.get();
  }

  public ObservableSet<Coords> getGaiaformers() {
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

  public IntegerProperty getProjectedTechScoring() {
    return projectedTechScoring;
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
    if (this.roundBooster.getValue() != null) {
      this.roundBooster.getValue().addVps(this);
      this.roundBooster.getValue().removeIncome(currentIncome);
    }

    // Add new income
    roundBooster.addIncome(currentIncome);
    this.roundBooster.setValue(roundBooster);
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

  public ObservableSet<Coords> getSatellites() {
    return satellites;
  }

  public IntegerProperty getTotalBuildings() {
    return totalBuildings;
  }

  public IntegerProperty getBuildingsInFeds() {
    return buildingsInFeds;
  }

  public ObservableMap<Serializable, Boolean> getSpecialActions() {
    return specialActions;
  }

  public int getFedPower() {
    return fedPower;
  }

  // Utility methods
  public Set<Coords> allBuildingLocations() {
    return ImmutableSet.<Coords> builder()
        .addAll(mines)
        .addAll(tradingPosts)
        .addAll(researchLabs)
        .addAll(pi)
        .addAll(ka)
        .addAll(qa)
        .build();
  }

  public boolean canDigTo(HexWithPlanet hex) {
    Preconditions.checkArgument(!hex.isEmpty());
    PlanetType planetType = hex.getPlanet().getPlanetType();
    if (planetType == PlanetType.TRANSDIM) {
      return false;
    }

    if (planetType == PlanetType.GAIA) {
      return qic.get() > 0;
    }

    return race.getHomePlanet().numDigsTo(planetType) - currentDigs.intValue() <= ore.intValue() / terraCost.intValue();
  }

  private boolean gaiaformed(Hex hex) {
    return gaiaformers.stream().anyMatch(c -> hex.getCoords().equals(c));
  }

  public void endTurn() {
    currentDigs.setValue(0);
    tempNavRange.setValue(0);
  }

  public void clearSpecialActions() {
    // Should set each value to false
    specialActions.keySet().forEach(k -> specialActions.put(k, false));
  }

  public int getPowerGain(HexWithPlanet hex) {
    Preconditions.checkArgument(hex.getPower() > 0);
    return Math.min(hex.getPower() == 3 ? bigBuildingPower.intValue() : hex.getPower(), canCharge());
  }

  public int getExcessBuildingPower() {
    int totalPower = 0;

    for (Coords coords : mines) {
      if (!inFederation(coords)) {
        totalPower += 1;
      }
    }

    for (Coords coords : Sets.union(tradingPosts, researchLabs)) {
      if (!inFederation(coords)) {
        totalPower += 2;
      }
    }

    for (Coords coords : Sets.union(Sets.union(pi, qa), ka)) {
      if (!inFederation(coords)) {
        totalPower += bigBuildingPower.intValue();
      }
    }

    return totalPower;
  }

  public boolean inFederation(Coords coords) {
    for (Set<Coords> federation : federations) {
      for (Coords inFed : federation) {
        if (inFed.equals(coords)) {
          return true;
        }
      }
    }

    return false;
  }

  public void exhaustFederationTile() {
    Preconditions.checkArgument(hasFlippableFederationTile());
    for (FedToken token : federationTiles) {
      if (token.flippable.get()) {
        token.flip();
        break;
      }
    }
  }

  // Action methods
  public void buildMine(HexWithPlanet hex) {
    buildMine(hex, false);
  }

  public void buildSetupMine(HexWithPlanet hex) {
    buildMine(hex, true);
  }

  private void buildMine(HexWithPlanet hex, boolean setup) {
    Mine mine = new Mine(hex, race.getColor(), playerEnum);
    mines.add(hex.getCoords());
    hex.addMine(mine);

    // Update income
    if (mines.size() != 3) {
      currentIncome.getOreIncome().setValue(currentIncome.getOreIncome().getValue() + 1);
    }

    // Update planet counts
    PlanetType planetType = hex.getPlanet().getPlanetType();
    builtOn.add(planetType);
    if (planetType == PlanetType.GAIA) {
      gaiaPlanets.setValue(gaiaPlanets.getValue() + 1);
    }
    totalBuildings.setValue(totalBuildings.getValue() + 1);
    sectors.add(hex.getSectorId());

    if (!setup) {
      // Handle cost for transformation
      if (planetType == PlanetType.GAIA) {
        if (gaiaformed(hex)) {
          gaiaformers.remove(hex.getCoords());
        } else {
          Util.minus(qic, 1);
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
    }

    // TODO: Add logic to check if it adds onto a previous federation
  }

  public void addGaiaformer(HexWithPlanet hex) {
    Preconditions.checkArgument(bin1.get() + bin2.get() + bin3.get() >= gaiaformerCost.get());
    Gaiaformer gaiaformer = new Gaiaformer(hex, race.getColor(), playerEnum);
    hex.addGaiaformer(gaiaformer);

    gaiaformers.add(hex.getCoords());

    if (bin1.get() >= gaiaformerCost.get()) {
      Util.minus(bin1, gaiaformerCost.get());
    } else {
      int remainingPower = gaiaformerCost.get() - bin1.get();
      bin1.setValue(0);
      if (bin2.get() >= remainingPower) {
        Util.minus(bin2, remainingPower);
      } else {
        remainingPower = remainingPower - bin2.get();
        bin3.setValue(0);
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
    if (techTrack.getValue() > 2) {
      projectedTechScoring.setValue(projectedTechScoring.getValue() + 4);
    }
  }

  public void buildTradingPost(HexWithPlanet hex, boolean cheap) {
    TradingPost tp = new TradingPost(hex, race.getColor(), playerEnum);
    hex.switchBuildingUI(tp);
    tradingPosts.add(hex.getCoords());
    mines.remove(hex.getCoords());

    Util.minus(ore, 2);
    Util.minus(credits, cheap ? 3 : 6);

    // Update Income
    if (mines.size() != 2) {
      Util.minus(currentIncome.getOreIncome(), 1);
    }
    tpIncome.get(tradingPosts.size() - 1).addTo(currentIncome);
  }

  public void buildResearchLab(HexWithPlanet hex) {
    ResearchLab rl = new ResearchLab(hex, race.getColor(), playerEnum);
    hex.switchBuildingUI(rl);
    researchLabs.add(hex.getCoords());
    tradingPosts.remove(hex.getCoords());

    Util.minus(ore, 3);
    Util.minus(credits, 5);

    // Update Income
    tpIncome.get(tradingPosts.size()).removeFrom(currentIncome);
    rlIncome.get(researchLabs.size() - 1).addTo(currentIncome);
  }

  public void buildPI(HexWithPlanet hex) {
    PlanetaryInstitute pi = new PlanetaryInstitute(hex, race.getColor(), playerEnum);
    hex.switchBuildingUI(pi);
    this.pi.add(hex.getCoords());
    tradingPosts.remove(hex.getCoords());

    Util.minus(ore, 4);
    Util.minus(credits, 6);

    // Update income
    tpIncome.get(tradingPosts.size()).removeFrom(currentIncome);
    piIncome.addTo(currentIncome);
  }

  public void buildAcademy(HexWithPlanet hex, boolean ka) {
    Academy academy = new Academy(hex, race.getColor(), playerEnum);
    hex.switchBuildingUI(academy);
    if (ka) {
      this.ka.add(hex.getCoords());
    } else {
      this.qa.add(hex.getCoords());
    }
    researchLabs.remove(hex.getCoords());

    Util.minus(ore, 6);
    Util.minus(credits, 6);

    // Update Income
    rlIncome.get(researchLabs.size()).removeFrom(currentIncome);
    if (ka) {
      Util.plus(currentIncome.getResearchIncome(), kaIncome);
    }
  }

  public void addSatellite(EmptyHex emptyHex) {
    satellites.add(emptyHex.getCoords());
    Satellite satellite = new Satellite(emptyHex, race.getColor(), playerEnum);
    emptyHex.addSatelliteUI(satellite);
  }

  // END GAME
  public void convertResourcesToVps() {
    if (bin2.intValue() > 1) {
      sacPower(bin2.intValue() / 2);
    }

    score.setValue(
        score.getValue()
            + (credits.intValue() + ore.intValue() + research.intValue() + qic.intValue() + bin3.intValue()) / 3);
  }

  // Serialization
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    oos.writeInt(gaiaBin.get());
    oos.writeInt(bin1.get());
    oos.writeInt(bin2.get());
    oos.writeInt(bin3.get());
    oos.writeInt(ore.get());
    oos.writeInt(credits.get());
    oos.writeInt(research.get());
    oos.writeInt(qic.get());
    oos.writeInt(availableGaiaformers.get());
    oos.writeInt(score.get());
    oos.writeUTF(roundBooster.getValue() == null ? "NONE" : roundBooster.getValue().name());

    // Tech track
    oos.writeInt(terraformingLevel.get());
    oos.writeInt(navLevel.get());
    oos.writeInt(aiLevel.get());
    oos.writeInt(gaiaformingLevel.get());
    oos.writeInt(econLevel.get());
    oos.writeInt(knowledgeLevel.get());
    oos.writeInt(terraCost.get());
    oos.writeInt(navRange.get());
    oos.writeInt(gaiaformerCost.get());

    // Tech tile
    oos.writeInt(bigBuildingPower.get());
    oos.writeObject(new HashSet<>(builtOn));
    oos.writeInt(gaiaPlanets.get());
    oos.writeObject(new HashSet<>(techTiles));
    oos.writeObject(new HashMap<>(specialActions));

    // Federations
    oos.writeObject(new HashSet<>(federations));

    // Buildings etc
    oos.writeObject(new HashSet<>(mines));
    oos.writeObject(new HashSet<>(tradingPosts));
    oos.writeObject(new HashSet<>(researchLabs));
    oos.writeObject(new HashSet<>(pi));
    oos.writeObject(new HashSet<>(ka));
    oos.writeObject(new HashSet<>(qa));
    oos.writeObject(new HashSet<>(gaiaformers));
    oos.writeObject(new HashSet<>(federationTiles));

    // Scoring related
    oos.writeObject(new HashSet<>(sectors));
    oos.writeObject(new HashSet<>(satellites));
    oos.writeInt(totalBuildings.get());
    oos.writeInt(buildingsInFeds.get());
    oos.writeInt(projectedTechScoring.get());
  }

  @SuppressWarnings("unchecked")
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
    gaiaBin = new SimpleIntegerProperty(ois.readInt());
    bin1 = new SimpleIntegerProperty(ois.readInt());
    bin2 = new SimpleIntegerProperty(ois.readInt());
    bin3 = new SimpleIntegerProperty(ois.readInt());
    ore = new SimpleIntegerProperty(ois.readInt());
    credits = new SimpleIntegerProperty(ois.readInt());
    research = new SimpleIntegerProperty(ois.readInt());
    qic = new SimpleIntegerProperty(ois.readInt());
    availableGaiaformers = new SimpleIntegerProperty(ois.readInt());
    score = new SimpleIntegerProperty(ois.readInt());
    String maybeBooster = ois.readUTF();
    roundBooster = "NONE".equals(maybeBooster)
        ? new SimpleObjectProperty<>()
        : new SimpleObjectProperty<>(RoundBooster.valueOf(maybeBooster));

    // Tech track
    terraformingLevel = new SimpleIntegerProperty(ois.readInt());
    navLevel = new SimpleIntegerProperty(ois.readInt());
    aiLevel = new SimpleIntegerProperty(ois.readInt());
    gaiaformingLevel = new SimpleIntegerProperty(ois.readInt());
    econLevel = new SimpleIntegerProperty(ois.readInt());
    knowledgeLevel = new SimpleIntegerProperty(ois.readInt());
    terraCost = new SimpleIntegerProperty(ois.readInt());
    navRange = new SimpleIntegerProperty(ois.readInt());
    gaiaformerCost = new SimpleIntegerProperty(ois.readInt());
    // These two are not written because they are ephemeral
    tempNavRange = new SimpleIntegerProperty(0);
    currentDigs = new SimpleIntegerProperty(0);

    // Tech tile
    bigBuildingPower = new SimpleIntegerProperty(ois.readInt());
    builtOn = FXCollections.observableSet((Set<PlanetType>) ois.readObject());
    gaiaPlanets = new SimpleIntegerProperty(ois.readInt());
    techTiles = FXCollections.observableSet((Set<TechTile>) ois.readObject());
    specialActions = FXCollections.observableMap((Map<Serializable, Boolean>) ois.readObject());

    federations = FXCollections.observableSet((Set<Set<Coords>>) ois.readObject());

    // Buildings
    mines = FXCollections.observableSet((Set<Coords>) ois.readObject());
    tradingPosts = FXCollections.observableSet((Set<Coords>) ois.readObject());
    researchLabs = FXCollections.observableSet((Set<Coords>) ois.readObject());
    pi = FXCollections.observableSet((Set<Coords>) ois.readObject());
    ka = FXCollections.observableSet((Set<Coords>) ois.readObject());
    qa = FXCollections.observableSet((Set<Coords>) ois.readObject());
    gaiaformers = FXCollections.observableSet((Set<Coords>) ois.readObject());
    federationTiles = FXCollections.observableSet((Set<FedToken>) ois.readObject());

    // Scoring related
    sectors = FXCollections.observableSet((Set<Integer>) ois.readObject());
    satellites = FXCollections.observableSet((Set<Coords>) ois.readObject());
    totalBuildings = new SimpleIntegerProperty(ois.readInt());
    buildingsInFeds = new SimpleIntegerProperty(ois.readInt());
    projectedTechScoring = new SimpleIntegerProperty(ois.readInt());

    fedPower = 7;

    setupTechBonuses();
  }

  public static class FedToken implements Serializable {
    private static final long serialVersionUID = 6669365166912310591L;
    private final FederationTile federationTile;
    private transient BooleanProperty flippable;

    public FedToken(FederationTile federationTile, boolean flippable) {
      this.federationTile = federationTile;
      this.flippable = new SimpleBooleanProperty(flippable);
    }

    public BooleanProperty getFlippable() {
      return flippable;
    }

    public void flip() {
      this.flippable.setValue(false);
    }

    public FederationTile getFederationTile() {
      return federationTile;
    }

    // Intentionally not implementing equals so no two instances are the same
    // Serialization
    private void writeObject(ObjectOutputStream oos) throws IOException {
      oos.defaultWriteObject();
      oos.writeBoolean(flippable.get());
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
      ois.defaultReadObject();
      flippable = new SimpleBooleanProperty(ois.readBoolean());
    }
  }
}
