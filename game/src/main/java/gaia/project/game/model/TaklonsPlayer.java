package gaia.project.game.model;

import java.io.IOException;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import gaia.project.game.board.EmptyHex;
import gaia.project.game.board.Satellite;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public final class TaklonsPlayer extends Player {
  private Property<Bin> brainStone = new SimpleObjectProperty<>(Bin.I);

  public static TaklonsPlayer createNew(PlayerEnum playerEnum) {
    TaklonsPlayer p = new TaklonsPlayer();
    p.fromRace(Race.TAKLONS, playerEnum);

    return p;
  }

  // Uninitialized instance for reading from JSON
  public static TaklonsPlayer empty() {
    return new TaklonsPlayer();
  }

  public static enum Bin {
    I, II, III, GAIA, REMOVED;
  }

  @Override
  public void leechPower(int power) {
    super.leechPower(power);

    // PI ability to gain power when you leech
    if (!getPi().isEmpty()) {
      Util.plus(getBin1(), 1);
    }
  }

  @Override
  public void chargePower(int toCharge) {
    int powerLeft = toCharge;

    if (brainStone.getValue() == Bin.I) {
      brainStone.setValue(Bin.II);
      powerLeft--;
    }

    if (powerLeft > 0) {
      if (getBin1().get() >= powerLeft) {
        Util.plus(getBin2(), powerLeft);
        Util.minus(getBin1(), powerLeft);
      } else {
        // Still more power to charge after bin1 is empty...
        powerLeft -= getBin1().get();
        Util.plus(getBin2(), getBin1().get());
        getBin1().setValue(0);

        if (brainStone.getValue() == Bin.II) {
          brainStone.setValue(Bin.III);
          powerLeft--;
        }

        if (powerLeft > 0) {
          if (getBin2().get() >= powerLeft) {
            Util.plus(getBin3(), powerLeft);
            Util.minus(getBin2(), powerLeft);
          } else {
            Util.plus(getBin3(), getBin2().get());
            getBin2().setValue(0);
          }
        }
      }
    }
  }

  @Override
  public void spendPower(int numPower) {
    if (numPower < 3 || brainStone.getValue() != Bin.III) {
      // Should never need to spend brainstone for < 3 power
      super.spendPower(numPower);
    } else if (getBin3().get() >= numPower) {
      Optional<ButtonType> response;
      do {
        response = new Alert(AlertType.CONFIRMATION, "Spend brainstone?", ButtonType.YES, ButtonType.NO).showAndWait();
      } while (response.isEmpty());
      if (response.get() == ButtonType.YES) {
        brainStone.setValue(Bin.I);
        if (numPower > 3) {
          super.spendPower(numPower - 3);
        }
      } else {
        super.spendPower(numPower);
      }
    } else {
      brainStone.setValue(Bin.I);
      if (numPower > 3) {
        super.spendPower(numPower - 3);
      }
    }
  }

  @Override
  public int canCharge() {
    int regular = super.canCharge();
    switch (brainStone.getValue()) {
      case I:
        return 2 + regular;
      case II:
        return 1 + regular;
      default:
        return regular;
    }

  }

  @Override
  public void sacPower(int numTimes) {
    Preconditions.checkArgument(
        getBin2().get() >= 2 * numTimes || (getBin2().get() == 2 * numTimes - 1 && brainStone.getValue() == Bin.II));

    if (brainStone.getValue() == Bin.II) {
      Util.minus(getBin2(), 1);
      brainStone.setValue(Bin.III);
      if (numTimes > 1) {
        super.sacPower(numTimes - 1);
      }
    } else {
      super.sacPower(numTimes);
    }
  }

  @Override
  public int spendablePower() {
    return getBin3().get() + (brainStone.getValue() == Bin.III ? 3 : 0);
  }

  @Override
  public boolean canSacPower() {
    return getBin2().get() > 1 || (getBin2().get() == 1 && brainStone.getValue() == Bin.II);
  }

  public Property<Bin> getBrainStone() {
    return brainStone;
  }

  @Override
  public boolean canBuildSatellite() {
    return super.canBuildSatellite() || (brainStone.getValue() != Bin.REMOVED && brainStone.getValue() != Bin.GAIA);
  }

  @Override
  public void addSatellite(EmptyHex emptyHex) {
    Preconditions.checkArgument(canBuildSatellite());
    getSatellites().add(emptyHex.getHexId());
    Satellite satellite = new Satellite(emptyHex, getRace().getColor(), getPlayerEnum());
    emptyHex.addSatelliteUI(satellite);

    if (getBin1().get() > 0) {
      Util.minus(getBin1(), 1);
    } else if (getBin2().get() > 0) {
      Util.minus(getBin2(), 1);
    } else if (getBin3().get() > 0) {
      Util.minus(getBin3(), 1);
    } else {
      // Brainstone being removed...
      brainStone.setValue(Bin.REMOVED);
    }
  }

  @Override
  public void convertResourcesToVps() {

    // Do one sac power to get the brain stone, then use the normal method
    if (brainStone.getValue() == Bin.II && getBin2().get() > 0) {
      sacPower(1);
    }

    super.convertResourcesToVps();

    // Then add in one more for the brainstone if it's in bin 3...
    if (brainStone.getValue() == Bin.III) {
      updateScore(1, "Resources");
    }
  }

  @Override
  public void writeExtraContent(JsonWriter json) throws IOException {
    json.name(JsonUtil.BRAINSTONE).value(brainStone.getValue().name());
  }

  @Override
  protected void handleAdditionalContent(String name, JsonReader json) throws IOException {
    Preconditions.checkArgument(JsonUtil.BRAINSTONE.equals(name));
    brainStone.setValue(Bin.valueOf(json.nextString()));
  }
}
