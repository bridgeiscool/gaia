package gaia.project.game.controller;

import java.lang.Thread.UncaughtExceptionHandler;

import javafx.application.Application;
import javafx.stage.Stage;

public class GaiaProjectApp extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
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
