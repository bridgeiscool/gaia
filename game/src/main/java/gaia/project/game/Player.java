package gaia.project.game;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.base.Preconditions;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

// Backing bean to store player's information
public class Player {
  private final Race race;
  private final IntegerProperty gaia;
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

  public Player(Race race) {
    this.race = race;
    this.gaia = new SimpleIntegerProperty(0);
    this.bin1 = new SimpleIntegerProperty(race.getStartingBin1());
    this.bin2 = new SimpleIntegerProperty(race.getStartingBin2());
    this.bin3 = new SimpleIntegerProperty(race.getStartingBin3());
    this.ore = new SimpleIntegerProperty(race.getStartingOre());
    this.credits = new SimpleIntegerProperty(race.getStartingCredits());
    this.research = new SimpleIntegerProperty(race.getStartingResearch());
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
  public IntegerProperty getGaia() {
    return gaia;
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
}
