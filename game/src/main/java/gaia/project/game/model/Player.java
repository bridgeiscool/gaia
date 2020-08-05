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
  private final IntegerProperty terraformingLevel;
  private final IntegerProperty navLevel;
  private final IntegerProperty aiLevel;
  private final IntegerProperty gaiaformingLevel;
  private final IntegerProperty econLevel;
  private final IntegerProperty knowledgeLevel;

  // Tech tile related
  private final Set<TechTile> techTiles = EnumSet.noneOf(TechTile.class);
  private final IntegerProperty flippableTechTiles = new SimpleIntegerProperty(0);
  private final BooleanProperty gaiaBuildBonus = new SimpleBooleanProperty(false);
  private final IntegerProperty bigBuildingPower = new SimpleIntegerProperty(3);
  private final Set<PlanetType> builtOn = EnumSet.noneOf(PlanetType.class);
  private final IntegerProperty gaiaPlanets = new SimpleIntegerProperty(0);

  // Buildings, etc
  private final ObservableSet<Mine> mines = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<TradingPost> tradingPosts = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<ResearchLab> researchLabs = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<PlanetaryInstitute> pi = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<KnowledgeAcademy> ka = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<QicAcademy> qa = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<Gaiaformer> gaiaformers = FXCollections.observableSet(new HashSet<>());

  public Player(Race race, PlayerEnum playerEnum) {
    this.race = race;
    this.playerEnum = playerEnum;
    this.gaiaBin = new SimpleIntegerProperty(0);
    this.bin1 = new SimpleIntegerProperty(race.getStartingBin1());
    this.bin2 = new SimpleIntegerProperty(race.getStartingBin2());
    this.bin3 = new SimpleIntegerProperty(race.getStartingBin3());
    this.ore = new SimpleIntegerProperty(race.getStartingOre());
    this.credits = new SimpleIntegerProperty(race.getStartingCredits());
    this.research = new SimpleIntegerProperty(race.getStartingKnowledge());
    this.qic = new SimpleIntegerProperty(race.getStartingQic());

    this.terraformingLevel = new SimpleIntegerProperty(race.getStartingTerraformingLevel());
    this.navLevel = new SimpleIntegerProperty(race.getStartingNavLevel());
    this.aiLevel = new SimpleIntegerProperty(race.getStartingAiLevel());
    this.gaiaformingLevel = new SimpleIntegerProperty(race.getStartingGaiaformingLevel());
    this.econLevel = new SimpleIntegerProperty(race.getStartingEconLevel());
    this.knowledgeLevel = new SimpleIntegerProperty(race.getStartingKnowledgeLevel());

    this.availableGaiaformers = new SimpleIntegerProperty(this.gaiaformingLevel.get());

    this.currentIncome = new Income(race);

    this.score = new SimpleIntegerProperty(10);
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

  public Set<PlanetType> getBuiltOn() {
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

  public void setRoundBooster(RoundBooster roundBooster) {
    if (this.roundBooster.getValue() != null) {
      this.roundBooster.getValue().addVps(this);
      this.roundBooster.getValue().removeIncome(currentIncome);
    }

    // Add new income
    roundBooster.addIncome(currentIncome);
    this.roundBooster.setValue(roundBooster);
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
  }
}
