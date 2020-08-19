package gaia.project.game.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Income implements UpdatePlayer {
  private static final long serialVersionUID = 5922169349684403967L;

  private transient IntegerProperty oreIncome;
  private transient IntegerProperty creditIncome;
  private transient IntegerProperty researchIncome;
  private transient IntegerProperty qicIncome;
  private transient IntegerProperty powerIncome;
  private transient IntegerProperty chargeIncome;

  public Income(Race race) {
    this.oreIncome = new SimpleIntegerProperty(race.getStartingOreIncome());
    this.creditIncome = new SimpleIntegerProperty(race.getStartingCreditIncome());
    this.researchIncome = new SimpleIntegerProperty(race.getStartingResearchIncome());
    this.qicIncome = new SimpleIntegerProperty(race.getStartingQicIncome());
    this.powerIncome = new SimpleIntegerProperty(race.getStartingPtIncome());
    this.chargeIncome = new SimpleIntegerProperty();
  }

  public IntegerProperty getOreIncome() {
    return oreIncome;
  }

  public IntegerProperty getCreditIncome() {
    return creditIncome;
  }

  public IntegerProperty getResearchIncome() {
    return researchIncome;
  }

  public IntegerProperty getQicIncome() {
    return qicIncome;
  }

  public IntegerProperty getPowerIncome() {
    return powerIncome;
  }

  public IntegerProperty getChargeIncome() {
    return chargeIncome;
  }

  @Override
  public void updatePlayer(Player player) {
    Util.plus(player.getOre(), oreIncome.get());
    Util.plus(player.getCredits(), creditIncome.get());
    Util.plus(player.getResearch(), researchIncome.get());
    Util.plus(player.getQic(), qicIncome.get());
    if (powerIncome.getValue() > 0 && chargeIncome.getValue() > 0) {
      if (player.canCharge() >= chargeIncome.get()) {
        player.chargePower(chargeIncome.get());
        Util.plus(player.getBin1(), powerIncome.get());
      } else {
        Util.plus(player.getBin1(), powerIncome.get());
        player.chargePower(chargeIncome.get());
      }
    } else {
      Util.plus(player.getBin1(), powerIncome.get());
      if (chargeIncome.getValue() > 0) {
        player.chargePower(chargeIncome.getValue());
      }
    }
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.writeInt(oreIncome.get());
    oos.writeInt(creditIncome.get());
    oos.writeInt(researchIncome.get());
    oos.writeInt(qicIncome.get());
    oos.writeInt(powerIncome.get());
    oos.writeInt(chargeIncome.get());
  }

  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    oreIncome = new SimpleIntegerProperty(ois.readInt());
    creditIncome = new SimpleIntegerProperty(ois.readInt());
    researchIncome = new SimpleIntegerProperty(ois.readInt());
    qicIncome = new SimpleIntegerProperty(ois.readInt());
    powerIncome = new SimpleIntegerProperty(ois.readInt());
    chargeIncome = new SimpleIntegerProperty(ois.readInt());
  }
}
