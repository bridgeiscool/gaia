package gaia.project.game.controller;

import gaia.project.game.model.Round;
import javafx.beans.property.Property;
import javafx.util.StringConverter;

public final class StringConverters {
  private StringConverters() {}

  public static StringConverter<Number> numberWithPrefix(final String prefix) {
    return new StringConverter<Number>() {
      @Override
      public String toString(Number number) {
        return prefix + number;
      }

      @Override
      public Number fromString(String string) {
        return null;
      }
    };
  }

  public static StringConverter<Number> numberWithPostfix(final String postFix) {
    return new StringConverter<Number>() {
      @Override
      public String toString(Number number) {
        return number + postFix;
      }

      @Override
      public Number fromString(String string) {
        return null;
      }
    };
  }

  public static StringConverter<Number> income(Property<Round> round) {
    return new StringConverter<Number>() {
      @Override
      public String toString(Number number) {
        return round.getValue() == Round.ROUND6 ? "" : "(+" + number + ")";
      }

      @Override
      public Number fromString(String string) {
        return null;
      }
    };
  }
}
