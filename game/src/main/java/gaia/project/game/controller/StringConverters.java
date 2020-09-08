package gaia.project.game.controller;

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

  public static StringConverter<Number> income() {
    return new StringConverter<Number>() {
      @Override
      public String toString(Number number) {
        return "(+" + number + ")";
      }

      @Override
      public Number fromString(String string) {
        return null;
      }
    };
  }
}
