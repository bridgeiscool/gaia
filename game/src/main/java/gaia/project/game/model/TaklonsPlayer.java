package gaia.project.game.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import com.google.common.base.Preconditions;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class TaklonsPlayer extends Player {
  private static final long serialVersionUID = -7064457047755128024L;

  private Bin brainStoneValue;
  private transient Property<Bin> brainStone;

  public TaklonsPlayer(PlayerEnum playerEnum) {
    super(Race.TAKLONS, playerEnum);
    brainStoneValue = Bin.I;
    brainStone = new SimpleObjectProperty<>(brainStoneValue);
    addBrainstoneListener();
  }

  private void addBrainstoneListener() {
    brainStone.addListener((o, oldValue, newValue) -> brainStoneValue = newValue);
  }

  public static enum Bin {
    I, II, III, GAIA;
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

  public Property<Bin> getBrainStone() {
    return brainStone;
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
  protected void writeObject(ObjectOutputStream oos) throws IOException {
    super.writeObject(oos);
  }

  @Override
  protected void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    super.readObject(ois);
    brainStone = new SimpleObjectProperty<>(brainStoneValue);
    addBrainstoneListener();
  }
}
