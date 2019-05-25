package gaia.project.game.model;

import javafx.beans.property.IntegerProperty;

public class Util {
  static void plus(IntegerProperty addTo, int toAdd) {
    addTo.setValue(addTo.getValue() + toAdd);
  }

  static void minus(IntegerProperty subtractFrom, int toSubtract) {
    subtractFrom.setValue(subtractFrom.getValue() - toSubtract);
  }
}
