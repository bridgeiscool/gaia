package gaia.project.game.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

final class GuiUtils {
  private GuiUtils() {}

  static final double POWERBINX = 40;
  static final double POWERBINY = 25;

  static void exceptionDialog(Throwable e) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Exception dialog");
    alert.setHeaderText("Game encountered an unexpected exception!");

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);

    Label label = new Label("Stack trace:");

    TextArea exceptionArea = new TextArea(sw.toString());
    exceptionArea.setEditable(false);
    exceptionArea.setWrapText(true);
    exceptionArea.setMaxWidth(Double.MAX_VALUE);
    exceptionArea.setMaxHeight(Double.MAX_VALUE);

    GridPane.setVgrow(exceptionArea, Priority.ALWAYS);
    GridPane.setHgrow(exceptionArea, Priority.ALWAYS);

    GridPane pane = new GridPane();
    pane.setMaxWidth(Double.MAX_VALUE);
    pane.add(label, 0, 0);
    pane.add(exceptionArea, 0, 1);

    alert.getDialogPane().setExpandableContent(pane);

    alert.showAndWait();
  }
}
