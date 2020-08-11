package gaia.project.game.model;

import javafx.beans.property.IntegerProperty;

public class Util {
  public static void plus(IntegerProperty addTo, int toAdd) {
    addTo.setValue(addTo.getValue() + toAdd);
  }

  public static void minus(IntegerProperty subtractFrom, int toSubtract) {
    subtractFrom.setValue(subtractFrom.getValue() - toSubtract);
  }
}
