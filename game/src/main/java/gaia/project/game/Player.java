package gaia.project.game;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;

// Backing bean to store player's information
public class Player {
  private final Race race;
  private final Property<Number> gaia;
  private final Property<Number> bin1;
  private final Property<Number> bin2;
  private final Property<Number> bin3;

  private final Property<Number> ore;
  private final Property<Number> credits;
  private final Property<Number> research;
  private final Property<Number> qic;

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
  }

  public Race getRace() {
    return race;
  }

  public Property<Number> getGaia() {
    return gaia;
  }

  public Property<Number> getBin1() {
    return bin1;
  }

  public Property<Number> getBin2() {
    return bin2;
  }

  public Property<Number> getBin3() {
    return bin3;
  }

  public Property<Number> getOre() {
    return ore;
  }

  public Property<Number> getCredits() {
    return credits;
  }

  public Property<Number> getResearch() {
    return research;
  }

  public Property<Number> getQic() {
    return qic;
  }

}
