package gaia.project.game;

import javafx.application.Platform;

public final class AppUtil {
  private AppUtil() {}

  public static void guiThread(Runnable runnable) {
    if (Platform.isFxApplicationThread()) {
      runnable.run();
    } else {
      Platform.runLater(runnable);
    }
  }
}
