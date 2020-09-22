package gaia.project.game.controller;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.lang.Thread.UncaughtExceptionHandler;

import javafx.application.Application;
import javafx.stage.Stage;

public class GaiaProjectApp extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int width = gd.getDisplayMode().getWidth();
    int height = gd.getDisplayMode().getHeight();

    new GaiaProjectController(primaryStage, width, height);

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
