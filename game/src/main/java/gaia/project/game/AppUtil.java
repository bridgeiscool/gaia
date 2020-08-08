package gaia.project.game;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class AppUtil {
  private AppUtil() {}

  public static void guiThread(Runnable runnable) {
    if (Platform.isFxApplicationThread()) {
      runnable.run();
    } else {
      Platform.runLater(runnable);
    }
  }

  public static Stage getNewStage(Parent parent, String title) {
    Stage newStage = new Stage();
    newStage.setTitle(title);
    newStage.setScene(new Scene(parent));
    newStage.setResizable(false);
    newStage.initModality(Modality.APPLICATION_MODAL);
    return newStage;
  }
}
