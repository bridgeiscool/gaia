package gaia.project.game.model;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Income implements UpdatePlayer {
  private final IntegerProperty oreIncome;
  private final IntegerProperty creditIncome;
  private final IntegerProperty researchIncome;
  private final IntegerProperty qicIncome;
  private final IntegerProperty powerIncome;
  private final IntegerProperty chargeIncome;

  public static Income fromRace(Race race) {
    Income i = new Income();
    i.oreIncome.setValue(race.getStartingOreIncome());
    i.creditIncome.setValue(race.getStartingCreditIncome());
    i.researchIncome.setValue(race.getStartingResearchIncome());
    i.qicIncome.setValue(race.getStartingQicIncome());
    i.powerIncome.setValue(race.getStartingPtIncome());
    i.chargeIncome.setValue(0);
    return i;
  }

  private Income(
      int oreIncome,
      int creditIncome,
      int researchIncome,
      int qicIncome,
      int powerIncome,
      int chargeIncome) {
    this.oreIncome = new SimpleIntegerProperty(oreIncome);
    this.creditIncome = new SimpleIntegerProperty(creditIncome);
    this.researchIncome = new SimpleIntegerProperty(researchIncome);
    this.qicIncome = new SimpleIntegerProperty(qicIncome);
    this.powerIncome = new SimpleIntegerProperty(powerIncome);
    this.chargeIncome = new SimpleIntegerProperty(chargeIncome);
  }

  private Income() {
    this.oreIncome = new SimpleIntegerProperty();
    this.creditIncome = new SimpleIntegerProperty();
    this.researchIncome = new SimpleIntegerProperty();
    this.qicIncome = new SimpleIntegerProperty();
    this.powerIncome = new SimpleIntegerProperty();
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

  public void write(JsonWriter json) throws IOException {
    json.beginObject();

    json.name(JsonUtil.ORE).value(oreIncome.get());
    json.name(JsonUtil.CREDITS).value(creditIncome.get());
    json.name(JsonUtil.RESEARCH).value(researchIncome.get());
    json.name(JsonUtil.QIC).value(qicIncome.get());
    json.name(JsonUtil.CHARGE).value(chargeIncome.get());
    json.name(JsonUtil.POWER).value(powerIncome.get());

    json.endObject();
  }

  public static Income read(JsonReader reader) throws IOException {
    Income i = new Income();
    reader.beginObject();
    while (reader.hasNext()) {
      switch (reader.nextName()) {
        case JsonUtil.ORE:
          i.oreIncome.setValue(reader.nextInt());
          break;
        case JsonUtil.CREDITS:
          i.creditIncome.setValue(reader.nextInt());
          break;
        case JsonUtil.RESEARCH:
          i.researchIncome.setValue(reader.nextInt());
          break;
        case JsonUtil.QIC:
          i.qicIncome.setValue(reader.nextInt());
          break;
        case JsonUtil.POWER:
          i.powerIncome.setValue(reader.nextInt());
          break;
        case JsonUtil.CHARGE:
          i.chargeIncome.setValue(reader.nextInt());
          break;
        default:
          throw new IllegalStateException();
      }
    }

    reader.endObject();
    return i;
  }
}
