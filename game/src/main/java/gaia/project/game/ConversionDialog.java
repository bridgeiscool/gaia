package gaia.project.game;

import java.io.IOException;

import gaia.project.game.model.BalTaksPlayer;
import gaia.project.game.model.Player;
import gaia.project.game.model.Util;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ConversionDialog extends Dialog<Void> {
  private static final ButtonType DONE = new ButtonType("Done", ButtonData.OK_DONE);
  protected final Player activePlayer;

  public static ConversionDialog create(Player activePlayer) {
    switch (activePlayer.getRace()) {
      case HADSCH_HALLAS:
        return new HadschHallasDialog(activePlayer);
      case BALTAKS:
        return new BalTaksDialog(activePlayer);
      default:
        return new ConversionDialog(activePlayer);
    }
  }

  @FXML
  protected GridPane gridPane;
  @FXML
  private Button qicToNav;
  @FXML
  private Button powerToQic;
  @FXML
  private Button qicToOre;
  @FXML
  private Button powerToOre;
  @FXML
  private Button powerToKnowledge;
  @FXML
  private Button knowledgeToCredits;
  @FXML
  private Button powerToCredits;
  @FXML
  private Button oreToCredits;
  @FXML
  private Button oreToPower;
  @FXML
  private Button sacPower;

  private ConversionDialog(Player activePlayer) {
    this.activePlayer = activePlayer;

    try {
      FXMLLoader loader = new FXMLLoader(ConversionDialog.class.getResource("Conversions.fxml"));
      loader.setController(this);
      getDialogPane().setContent((Node) loader.load());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    setResultConverter(button -> null);
    getDialogPane().getButtonTypes().add(DONE);

    // Disable buttons when they can't be used...
    qicToNav.disableProperty().bind(Bindings.lessThan(activePlayer.getQic(), 1));
    powerToQic.disableProperty().bind(Bindings.lessThan(activePlayer.getBin3(), 1));
    qicToOre.disableProperty().bind(Bindings.lessThan(activePlayer.getQic(), 1));
    powerToOre.disableProperty().bind(Bindings.lessThan(activePlayer.getBin3(), 3));
    powerToKnowledge.disableProperty().bind(Bindings.lessThan(activePlayer.getBin3(), 4));
    knowledgeToCredits.disableProperty().bind(Bindings.lessThan(activePlayer.getResearch(), 1));
    powerToCredits.disableProperty().bind(Bindings.lessThan(activePlayer.getBin3(), 1));
    oreToCredits.disableProperty().bind(Bindings.lessThan(activePlayer.getOre(), 1));
    oreToPower.disableProperty().bind(Bindings.lessThan(activePlayer.getOre(), 1));
    sacPower.disableProperty().bind(Bindings.lessThan(activePlayer.getBin2(), 2));

    additionalContent();
  }

  protected void additionalContent() {
    // Does nothing by default
  }

  @FXML
  private void qicToNav() {
    if (activePlayer.getQic().getValue() > 0) {
      activePlayer.getQic().setValue(activePlayer.getQic().getValue() - 1);
      activePlayer.getTempNavRange().setValue(activePlayer.getTempNavRange().getValue() + 2);
    }
  }

  @FXML
  private void powerToQic() {
    if (activePlayer.getBin3().getValue() > 3) {
      activePlayer.getBin3().setValue(activePlayer.getBin3().getValue() - 4);
      Util.plus(activePlayer.getBin1(), 4);
      activePlayer.getQic().setValue(activePlayer.getQic().getValue() + 1);
    }
  }

  @FXML
  private void qicToOre() {
    if (activePlayer.getQic().getValue() > 0) {
      activePlayer.getQic().setValue(activePlayer.getQic().getValue() - 1);
      activePlayer.getOre().setValue(activePlayer.getOre().getValue() + 1);
    }
  }

  @FXML
  private void powerToOre() {
    if (activePlayer.getBin3().getValue() > 2) {
      activePlayer.getBin3().setValue(activePlayer.getBin3().getValue() - 3);
      Util.plus(activePlayer.getBin1(), 3);
      activePlayer.getOre().setValue(activePlayer.getOre().getValue() + 1);
    }
  }

  @FXML
  private void powerToKnowledge() {
    if (activePlayer.getBin3().getValue() > 3) {
      activePlayer.getBin3().setValue(activePlayer.getBin3().getValue() - 4);
      Util.plus(activePlayer.getBin1(), 4);
      activePlayer.getResearch().setValue(activePlayer.getResearch().getValue() + 1);
    }
  }

  @FXML
  private void knowledgeToCredits() {
    if (activePlayer.getResearch().getValue() > 0) {
      activePlayer.getResearch().setValue(activePlayer.getResearch().getValue() - 1);
      activePlayer.getCredits().setValue(activePlayer.getCredits().getValue() + 1);
    }
  }

  @FXML
  private void powerToCredits() {
    if (activePlayer.getBin3().getValue() > 0) {
      activePlayer.getBin3().setValue(activePlayer.getBin3().getValue() - 1);
      Util.plus(activePlayer.getBin1(), 1);
      activePlayer.getCredits().setValue(activePlayer.getCredits().getValue() + 1);
    }
  }

  @FXML
  private void oreToCredits() {
    if (activePlayer.getOre().intValue() > 0) {
      activePlayer.getOre().setValue(activePlayer.getOre().getValue() - 1);
      activePlayer.getCredits().setValue(activePlayer.getCredits().getValue() + 1);
    }
  }

  @FXML
  private void oreToPower() {
    if (activePlayer.getOre().intValue() > 0) {
      activePlayer.getOre().setValue(activePlayer.getOre().getValue() - 1);
      activePlayer.getBin1().setValue(activePlayer.getBin1().getValue() + 1);
    }
  }

  @FXML
  private void sacPower() {
    if (activePlayer.getBin2().intValue() > 1) {
      activePlayer.sacPower(1);
    }
  }

  private static class HadschHallasDialog extends ConversionDialog {
    HadschHallasDialog(Player activePlayer) {
      super(activePlayer);
    }

    @Override
    protected void additionalContent() {
      if (!activePlayer.getPi().isEmpty()) {
        // Credits -> Qic
        gridPane.add(new Label("4c"), 0, 11);
        Button creditsToQic = new Button(">");
        creditsToQic.setOnAction(e -> {
          Util.minus(activePlayer.getCredits(), 4);
          Util.plus(activePlayer.getQic(), 1);
        });
        gridPane.add(creditsToQic, 1, 11);
        gridPane.add(new Label("q"), 2, 11);

        // Credits -> K
        gridPane.add(new Label("4c"), 0, 12);
        Button creditsToK = new Button(">");
        creditsToK.setOnAction(e -> {
          Util.minus(activePlayer.getCredits(), 4);
          Util.plus(activePlayer.getResearch(), 1);
        });
        gridPane.add(creditsToK, 1, 12);
        gridPane.add(new Label("k"), 2, 12);

        // Credits -> Ore
        gridPane.add(new Label("3c"), 0, 13);
        Button creditsToOre = new Button(">");
        creditsToOre.setOnAction(e -> {
          Util.minus(activePlayer.getCredits(), 3);
          Util.plus(activePlayer.getOre(), 1);
        });
        gridPane.add(creditsToOre, 1, 13);
        gridPane.add(new Label("o"), 2, 13);
      }
    }
  }

  private static class BalTaksDialog extends ConversionDialog {

    BalTaksDialog(Player activePlayer) {
      super(activePlayer);
    }

    @Override
    protected void additionalContent() {
      // Gaiaformer -> QIC
      gridPane.add(new Label("GF"), 0, 11);
      Button gfToQic = new Button(">");
      gfToQic.setOnAction(e -> {
        BalTaksPlayer baltaks = (BalTaksPlayer) activePlayer;
        Util.plus(baltaks.getQic(), 1);
        Util.minus(baltaks.getAvailableGaiaformers(), 1);
        baltaks.setGaiaformersInGaiaBin(baltaks.getGaiaformersInGaiaBin() + 1);
      });

      gfToQic.disableProperty().bind(Bindings.lessThan(activePlayer.getAvailableGaiaformers(), 1));
      gridPane.add(gfToQic, 1, 11);
      gridPane.add(new Label("q"), 2, 11);
    }
  }
}
