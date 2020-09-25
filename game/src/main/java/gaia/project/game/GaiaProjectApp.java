package gaia.project.game;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.lang.Thread.UncaughtExceptionHandler;

import gaia.project.game.board.BoardUtils;
import gaia.project.game.controller.GaiaProjectController;
import javafx.application.Application;
import javafx.stage.Stage;

public class GaiaProjectApp extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int width = gd.getDisplayMode().getWidth();
    int height = gd.getDisplayMode().getHeight();

    // Uncomment to test resizing...
    // BoardUtils.setScaling(1440, 900);
    BoardUtils.setScaling(width, height);
    new GaiaProjectController(primaryStage);

    primaryStage.setTitle("Gaia Project");
    primaryStage.setResizable(false);

    Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread t, Throwable e) {
        GuiUtils.exceptionDialog(e);
      }
    });

    // Display the app
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
