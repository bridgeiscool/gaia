package gaia.project.game;

import javafx.util.StringConverter;

public final class StringConverters {
  private StringConverters() {}

  public static StringConverter<Number> numberWithPrefix(final String prefix) {
    return new StringConverter<Number>() {
      @Override
      public String toString(Number object) {
        return prefix + object;
      }

      @Override
      public Number fromString(String string) {
        return null;
      }
    };
  }
}
