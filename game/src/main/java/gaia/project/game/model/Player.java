package gaia.project.game.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import gaia.project.game.PlanetType;
import gaia.project.game.board.Hex;
import gaia.project.game.board.Mine;
import gaia.project.game.board.TradingPost;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
  private transient ObservableSet<TechTile> techTiles = FXCollections.observableSet(new HashSet<>());
  private transient IntegerProperty flippableTechTiles = new SimpleIntegerProperty(0);
  private transient IntegerProperty bigBuildingPower = new SimpleIntegerProperty(3);
  private transient ObservableSet<PlanetType> builtOn = FXCollections.observableSet(new HashSet<>());
  private transient IntegerProperty gaiaPlanets = new SimpleIntegerProperty(0);

  // Buildings, etc
  private transient ObservableSet<Coords> mines = FXCollections.observableSet(new HashSet<>());
  private transient ObservableSet<Coords> tradingPosts = FXCollections.observableSet(new HashSet<>());
  private transient ObservableSet<Coords> researchLabs = FXCollections.observableSet(new HashSet<>());
  private transient ObservableSet<Coords> pi = FXCollections.observableSet(new HashSet<>());
  private transient ObservableSet<Coords> ka = FXCollections.observableSet(new HashSet<>());
  private transient ObservableSet<Coords> qa = FXCollections.observableSet(new HashSet<>());
  private transient ObservableSet<Coords> gaiaformers = FXCollections.observableSet(new HashSet<>());
  private transient ObservableList<FederationTile> federationTiles = FXCollections.observableList(new ArrayList<>());

  // Income related
  private final List<IncomeUpdater> tpIncome;

  // Scoring Related
  private transient ObservableSet<Integer> sectors = FXCollections.observableSet(new HashSet<>());
  private transient ObservableSet<Coords> satellites = FXCollections.observableSet(new HashSet<>());
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
          ore.setValue(ore.getValue() + 2);
          break;
        case 3:
          chargePower(3);
          // Fall through
        case 2:
          terraCost.setValue(4 - newValue.intValue());
          break;
        case 5:
          flippableTechTiles.setValue(flippableTechTiles.getValue() - 1);
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
          qic.setValue(qic.getValue() + 1);
          break;
        case 2:
          navRange.setValue(2);
          break;
        case 4:
          navRange.setValue(3);
          break;
        case 5:
          flippableTechTiles.setValue(flippableTechTiles.getValue() - 1);
          navRange.setValue(4);
          System.out.println("Implement gaining lonely planet!");
          break;
      }
    });

    aiLevel.addListener((o, oldValue, newValue) -> {
      switch (newValue.intValue()) {
        case 1:
        case 2:
          qic.setValue(qic.getValue() + 1);
          break;
        case 3:
          chargePower(3);
          // Fall through
        case 4:
          qic.setValue(qic.getValue() + 2);
          break;
        case 5:
          flippableTechTiles.setValue(flippableTechTiles.getValue() - 1);
          qic.setValue(qic.getValue() + 4);
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
          bin1.setValue(bin1.getValue() + 3);
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
          flippableTechTiles.setValue(flippableTechTiles.getValue() - 1);
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
          flippableTechTiles.setValue(flippableTechTiles.getValue() - 1);
          currentIncome.getCreditIncome().setValue(currentIncome.getCreditIncome().getValue() - 4);
          currentIncome.getOreIncome().setValue(currentIncome.getOreIncome().getValue() - 2);
          currentIncome.getChargeIncome().setValue(currentIncome.getChargeIncome().getValue() - 4);
          ore.setValue(ore.getValue() + 3);
          credits.setValue(credits.getValue() + 6);
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
          flippableTechTiles.setValue(flippableTechTiles.getValue() - 1);
          currentIncome.getResearchIncome().setValue(currentIncome.getResearchIncome().getValue() - 4);
          research.setValue(research.getValue() + 9);
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
    federationTiles.add(federationTile);
    if (federationTile.isFlippable()) {
      Util.plus(flippableTechTiles, 1);
    }
  }

  public void leechPower(int power) {
    chargePower(power);
    Util.minus(score, power - 1);
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

  public IntegerProperty getFlippableTechTiles() {
    return flippableTechTiles;
  }

  public Income getCurrentIncome() {
    return currentIncome;
  }

  public ObservableList<FederationTile> getFederationTiles() {
    return federationTiles;
  }

  public Set<TechTile> getTechTiles() {
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

  public boolean canDigTo(Hex hex) {
    Preconditions.checkArgument(hex.getPlanet().isPresent());
    PlanetType planetType = hex.getPlanet().get().getPlanetType();
    if (planetType == PlanetType.TRANSDIM) {
      return false;
    }

    if (planetType == PlanetType.GAIA) {
      return gaiaformed(hex) || qic.get() > 0;
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

  public int getPowerGain(Hex hex) {
    Preconditions.checkArgument(hex.getPower() > 0);
    return hex.getPower() == 3 ? bigBuildingPower.intValue() : hex.getPower();
  }

  // Action methods
  public void buildMine(Hex hex) {
    buildMine(hex, false);
  }

  public void buildSetupMine(Hex hex) {
    buildMine(hex, true);
  }

  private void buildMine(Hex hex, boolean setup) {
    Mine mine = new Mine(hex, race.getColor(), playerEnum);
    mines.add(hex.getCoords());
    hex.addMine(mine);

    // Update income
    if (mines.size() != 3) {
      currentIncome.getOreIncome().setValue(currentIncome.getOreIncome().getValue() + 1);
    }

    // Update planet counts
    PlanetType planetType = hex.getPlanet().get().getPlanetType();
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

  public void advanceTech(IntegerProperty techTrack) {
    techTrack.setValue(techTrack.getValue() + 1);
    research.setValue(research.getValue() - 4);
    if (techTrack.getValue() > 2) {
      projectedTechScoring.setValue(projectedTechScoring.getValue() + 4);
    }
  }

  public void buildTradingPost(Hex hex, boolean cheap) {
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
    tpIncome.get(tradingPosts.size() - 1).update(currentIncome);
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
    oos.writeInt(flippableTechTiles.get());
    oos.writeInt(bigBuildingPower.get());
    oos.writeObject(new HashSet<>(builtOn));
    oos.writeInt(gaiaPlanets.get());

    // Buildings etc
    oos.writeObject(new HashSet<>(mines));
    oos.writeObject(new HashSet<>(tradingPosts));
    oos.writeObject(new HashSet<>(researchLabs));
    oos.writeObject(new HashSet<>(pi));
    oos.writeObject(new HashSet<>(ka));
    oos.writeObject(new HashSet<>(qa));
    oos.writeObject(new HashSet<>(gaiaformers));
    oos.writeObject(new ArrayList<>(federationTiles));

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
    flippableTechTiles = new SimpleIntegerProperty(ois.readInt());
    bigBuildingPower = new SimpleIntegerProperty(ois.readInt());
    builtOn = FXCollections.observableSet((Set<PlanetType>) ois.readObject());
    gaiaPlanets = new SimpleIntegerProperty(ois.readInt());

    // Buildings
    mines = FXCollections.observableSet((Set<Coords>) ois.readObject());
    tradingPosts = FXCollections.observableSet((Set<Coords>) ois.readObject());
    researchLabs = FXCollections.observableSet((Set<Coords>) ois.readObject());
    pi = FXCollections.observableSet((Set<Coords>) ois.readObject());
    ka = FXCollections.observableSet((Set<Coords>) ois.readObject());
    qa = FXCollections.observableSet((Set<Coords>) ois.readObject());
    gaiaformers = FXCollections.observableSet((Set<Coords>) ois.readObject());
    federationTiles = FXCollections.observableList((List<FederationTile>) ois.readObject());

    // Scoring related
    sectors = FXCollections.observableSet((Set<Integer>) ois.readObject());
    satellites = FXCollections.observableSet((Set<Coords>) ois.readObject());
    totalBuildings = new SimpleIntegerProperty(ois.readInt());
    buildingsInFeds = new SimpleIntegerProperty(ois.readInt());
    projectedTechScoring = new SimpleIntegerProperty(ois.readInt());
  }
}
