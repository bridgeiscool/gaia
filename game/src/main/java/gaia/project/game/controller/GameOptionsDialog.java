package gaia.project.game.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import gaia.project.game.model.GameOpts;
import gaia.project.game.model.SetupEnum;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

public class GameOptionsDialog extends Dialog<GameOpts> {
  private static final String DIALOG_TITLE = "Select Game Options";
  private static final ButtonType OK = new ButtonType("Start Game", ButtonData.OK_DONE);
  private static final ButtonType CANCEL = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

  @FXML
  private ComboBox<Integer> numPlayers;
  @FXML
  private ComboBox<String> setup;
  @FXML
  private CheckBox originalBoard;
  @FXML
  private CheckBox baltaksBuff;
  @FXML
  private TextField seed;

  public GameOptionsDialog() {
    try {
      FXMLLoader loader = new FXMLLoader(GameOptionsDialog.class.getResource("GameOptions.fxml"));
      loader.setController(this);
      getDialogPane().setContent((Node) loader.load());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // UI Setup
    setup.setItems(
        Arrays.stream(SetupEnum.values())
            .map(SetupEnum::getDisplay)
            .collect(Collectors.toCollection(FXCollections::observableArrayList)));

    // Initialize defaults
    numPlayers.setValue(3);
    setup.setValue(SetupEnum.AUCTION_RACES.getDisplay());
    baltaksBuff.setSelected(true);
    seed.setText(String.valueOf(System.currentTimeMillis()));

    setTitle(DIALOG_TITLE);
    getDialogPane().getButtonTypes().addAll(OK, CANCEL);

    setResultConverter(bt -> {
      if (bt == OK) {
        return new GameOpts(
            baltaksBuff.isSelected(),
            numPlayers.getValue(),
            SetupEnum.fromDisplay(setup.getValue()),
            !originalBoard.isSelected(),
            (seed.getText().isEmpty() || seed.getText() == null) ? null : Long.valueOf(seed.getText()));
      }

      return null;
    });

  }
}
