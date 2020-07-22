package gaia.project.game.model;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Preconditions;

import gaia.project.game.PlanetType;
import gaia.project.game.board.KnowledgeAcademy;
import gaia.project.game.board.Mine;
import gaia.project.game.board.PlanetaryInstitute;
import gaia.project.game.board.QicAcademy;
import gaia.project.game.board.ResearchLab;
import gaia.project.game.board.TradingPost;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

// Backing bean to store player's information
public class Player {
  private final Race race;
  private final IntegerProperty gaiaBin;
  private final IntegerProperty bin1;
  private final IntegerProperty bin2;
  private final IntegerProperty bin3;

  private final IntegerProperty ore;
  private final IntegerProperty credits;
  private final IntegerProperty research;
  private final IntegerProperty qic;

  private final Income currentIncome;

  private final IntegerProperty score;

  private final Set<FederationTile> federationTiles = EnumSet.noneOf(FederationTile.class);

  // Tech tile related
  private final Set<TechTile> techTiles = EnumSet.noneOf(TechTile.class);
  private final IntegerProperty flippableTechTiles = new SimpleIntegerProperty(0);
  private final BooleanProperty gaiaBuildBonus = new SimpleBooleanProperty(false);
  private final IntegerProperty bigBuildingPower = new SimpleIntegerProperty(3);
  private final Set<PlanetType> builtOn = EnumSet.noneOf(PlanetType.class);

  private final ObservableSet<Mine> mines = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<TradingPost> tradingPosts = FXCollections.observableSet(new HashSet<>());
  private final ObservableSet<ResearchLab> researchLabs = FXCollections.observableSet(new HashSet<>());
  private Optional<PlanetaryInstitute> pi = Optional.empty();
  private Optional<KnowledgeAcademy> ka = Optional.empty();
  private Optional<QicAcademy> qa = Optional.empty();

  public Player(Race race) {
    this.race = race;
    this.gaiaBin = new SimpleIntegerProperty(0);
    this.bin1 = new SimpleIntegerProperty(race.getStartingBin1());
    this.bin2 = new SimpleIntegerProperty(race.getStartingBin2());
    this.bin3 = new SimpleIntegerProperty(race.getStartingBin3());
    this.ore = new SimpleIntegerProperty(race.getStartingOre());
    this.credits = new SimpleIntegerProperty(race.getStartingCredits());
    this.research = new SimpleIntegerProperty(race.getStartingKnowledge());
    this.qic = new SimpleIntegerProperty(race.getStartingQic());

    this.currentIncome = new Income(race);

    this.score = new SimpleIntegerProperty(10);
  }

  public Race getRace() {
    return race;
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

  public Optional<PlanetaryInstitute> getPi() {
    return pi;
  }

  public Optional<KnowledgeAcademy> getKa() {
    return ka;
  }

  public Optional<QicAcademy> getQa() {
    return qa;
  }

}
