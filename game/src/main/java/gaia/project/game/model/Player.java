package gaia.project.game.model;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;

import gaia.project.game.PlanetType;
import gaia.project.game.board.Gaiaformer;
import gaia.project.game.board.Hex;
import gaia.project.game.board.KnowledgeAcademy;
import gaia.project.game.board.Mine;
import gaia.project.game.board.PlanetaryInstitute;
import gaia.project.game.board.QicAcademy;
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
import javafx.collections.ObservableSet;

// Backing bean to store player's information
public class Player {
  private final Race race;
  private final PlayerEnum playerEnum;
  private final IntegerProperty gaiaBin;
  private final IntegerProperty bin1;
  private final IntegerProperty bin2;
  private final IntegerProperty bin3;

  private final IntegerProperty ore;
  private final IntegerProperty credits;
  private final IntegerProperty research;
  private final IntegerProperty qic;

  private final IntegerProperty availableGaiaformers;

  private final Income currentIncome;

  private final IntegerProperty score;

  private final Set<FederationTile> federationTiles = EnumSet.noneOf(FederationTile.class);

  private final Property<RoundBooster> roundBooster = new SimpleObjectProperty<>();

  // Tech track related
  private final IntegerProperty terraformingLevel = new SimpleIntegerProperty(0);
  private final IntegerProperty navLevel = new SimpleIntegerProperty(0);
  private final IntegerProperty aiLevel = new SimpleIntegerProperty(0);
  private final IntegerProperty gaiaformingLevel = new SimpleIntegerProperty(0);
  private final IntegerProperty econLevel = new SimpleIntegerProperty(0);
  private final IntegerProperty knowledgeLevel = new SimpleIntegerProperty(0);
  private final IntegerProperty terraCost = new SimpleIntegerProperty(3);
  private final IntegerProperty navRange = new SimpleIntegerProperty(1);
  private final IntegerProperty gaiaformerCost = new SimpleIntegerProperty(50);

  // Tech tile related
  private final Set<TechTile> techTiles = EnumSet.noneOf(TechTile.class);
  private final IntegerProperty flippableTechTiles = new SimpleIntegerProperty(0);
  private final BooleanProperty gaiaBuildBonus = new SimpleBooleanProperty(false);
  private final IntegerProperty bigBuildingPower = new SimpleIntegerProperty(3);
  private final ObservableSet<PlanetType> builtOn = FXCollections.observableSet(EnumSet.noneOf(PlanetType.class));
  private final IntegerProperty gaiaPlanets = new SimpleIntegerProperty(0);

  // Buildings, etc
  private final ObservableSet<Mine> mines = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<TradingPost> tradingPosts = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<ResearchLab> researchLabs = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<PlanetaryInstitute> pi = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<KnowledgeAcademy> ka = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<QicAcademy> qa = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<Gaiaformer> gaiaformers = FXCollections.observableSet(new HashSet<>());

  // Scoring Related
  private final ObservableSet<Integer> sectors = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<Satellite> satellites = FXCollections.observableSet(new HashSet<>());
  private final IntegerProperty totalBuildings = new SimpleIntegerProperty(0);
  private final IntegerProperty buildingsInFeds = new SimpleIntegerProperty(0);
  private final IntegerProperty projectedTechScoring = new SimpleIntegerProperty(0);

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

  public Set<FederationTile> getFederationTiles() {
    return federationTiles;
  }

  public Set<TechTile> getTechTiles() {
    return techTiles;
  }

  public BooleanProperty getGaiaBuildBonus() {
    return gaiaBuildBonus;
  }

  public IntegerProperty getBigBuildingPower() {
    return bigBuildingPower;
  }

  public ObservableSet<PlanetType> getBuiltOn() {
    return builtOn;
  }

  public ObservableSet<Mine> getMines() {
    return mines;
  }

  public ObservableSet<TradingPost> getTradingPosts() {
    return tradingPosts;
  }

  public ObservableSet<ResearchLab> getResearchLabs() {
    return researchLabs;
  }

  public ObservableSet<PlanetaryInstitute> getPi() {
    return pi;
  }

  public ObservableSet<KnowledgeAcademy> getKa() {
    return ka;
  }

  public ObservableSet<QicAcademy> getQa() {
    return qa;
  }

  public IntegerProperty getAvailableGaiaformers() {
    return availableGaiaformers;
  }

  public ObservableSet<Gaiaformer> getGaiaformers() {
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

  public void setRoundBooster(RoundBooster roundBooster) {
    if (this.roundBooster.getValue() != null) {
      this.roundBooster.getValue().addVps(this);
      this.roundBooster.getValue().removeIncome(currentIncome);
    }

    // Add new income
    roundBooster.addIncome(currentIncome);
    this.roundBooster.setValue(roundBooster);
  }

  public ObservableSet<Integer> getSectors() {
    return sectors;
  }

  public ObservableSet<Satellite> getSatellites() {
    return satellites;
  }

  public IntegerProperty getTotalBuildings() {
    return totalBuildings;
  }

  public IntegerProperty getBuildingsInFeds() {
    return buildingsInFeds;
  }

  // Action methods
  public void buildMine(Hex hex) {
    buildMine(hex, false);
  }

  public void buildSetupMine(Hex hex) {
    buildMine(hex, true);
  }

  private void buildMine(Hex hex, boolean setup) {
    Mine mine = new Mine(hex, race.getColor());
    mines.add(mine);
    hex.addMine(mine);

    // Update income
    if (mines.size() != 3) {
      currentIncome.getOreIncome().setValue(currentIncome.getOreIncome().getValue() + 1);
    }

    // Update planet counts
    builtOn.add(hex.getPlanet().get().getPlanetType());
    if (hex.getPlanet().get().getPlanetType() == PlanetType.GAIA) {
      gaiaPlanets.setValue(gaiaPlanets.getValue() + 1);
    }
    totalBuildings.setValue(totalBuildings.getValue() + 1);
    sectors.add(hex.getSectorId());

    // TODO: Add logic to check if it adds onto a previous federation
  }

  public void advanceTech(IntegerProperty techTrack) {
    techTrack.setValue(techTrack.getValue() + 1);
    research.setValue(research.getValue() - 4);
    if (techTrack.getValue() > 2) {
      projectedTechScoring.setValue(projectedTechScoring.getValue() + 4);
    }
    techTrackBumped();
  }

  private void techTrackBumped() {
    // Hook to handle VPs
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
}
