package gaia.project.game.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Income implements UpdatePlayer {
  private static final long serialVersionUID = 5922169349684403967L;

  private final IntegerProperty oreIncome;
  private final IntegerProperty creditIncome;
  private final IntegerProperty researchIncome;
  private final IntegerProperty qicIncome;
  private final IntegerProperty powerIncome;
  private final IntegerProperty chargeIncome;

  public Income(Race race) {
    this.oreIncome = new SimpleIntegerProperty(race.getStartingOreIncome());
    this.creditIncome = new SimpleIntegerProperty(race.getStartingCreditIncome());
    this.researchIncome = new SimpleIntegerProperty(race.getStartingResearchIncome());
    this.qicIncome = new SimpleIntegerProperty(0);
    this.powerIncome = new SimpleIntegerProperty(0);
    this.chargeIncome = new SimpleIntegerProperty(0);
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
    player.getOre().setValue(player.getOre().getValue().intValue() + oreIncome.getValue());
    player.getCredits().setValue(player.getCredits().getValue().intValue() + creditIncome.getValue());
    player.getResearch().setValue(player.getResearch().getValue().intValue() + researchIncome.getValue());
    player.getQic().setValue(player.getQic().getValue().intValue() + qicIncome.getValue());
    if (powerIncome.getValue() > 0 && chargeIncome.getValue() > 0) {
      // TODO: Prompt user for order

    } else {
      player.getBin1().setValue(player.getBin1().getValue().intValue() + powerIncome.getValue());
      if (chargeIncome.getValue() > 0) {
        player.chargePower(chargeIncome.getValue());
      }
    }
  }
}
