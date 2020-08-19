package gaia.project.game;

import java.io.IOException;

import gaia.project.game.model.Player;
import gaia.project.game.model.Util;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;

public class TerranPIDialog extends Dialog<Void> {
  private static final ButtonType DONE = new ButtonType("OK", ButtonData.OK_DONE);
  private final IntegerProperty powerToSpend;

  private final IntegerProperty knowledge = new SimpleIntegerProperty(0);
  private final IntegerProperty qic = new SimpleIntegerProperty(0);
  private final IntegerProperty ore = new SimpleIntegerProperty(0);
  private final IntegerProperty credits = new SimpleIntegerProperty(0);

  private boolean saved = false;

  @FXML
  private Label leftToSpend;
  @FXML
  private Label addedKnowledge;
  @FXML
  private Label addedQic;
  @FXML
  private Label addedOre;
  @FXML
  private Label addedCredits;

  public TerranPIDialog(Player activePlayer) {
    try {
      FXMLLoader loader = new FXMLLoader(TerranPIDialog.class.getResource("TerranPI.fxml"));
      loader.setController(this);
      getDialogPane().setContent((Node) loader.load());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    powerToSpend = new SimpleIntegerProperty(activePlayer.getGaiaBin().get());

    setResultConverter(button -> {
      saved = true;
      return null;
    });

    setTitle("Terran PI Ability");
    getDialogPane().getButtonTypes().add(DONE);

    powerToSpend.addListener((o, oldValue, newValue) -> {
      getDialogPane().lookupButton(DONE).setDisable(newValue.intValue() != 0);
    });

    leftToSpend.textProperty().bindBidirectional(powerToSpend, new NumberStringConverter());
    addedKnowledge.textProperty().bindBidirectional(knowledge, new NumberStringConverter());
    addedQic.textProperty().bindBidirectional(qic, new NumberStringConverter());
    addedOre.textProperty().bindBidirectional(ore, new NumberStringConverter());
    addedCredits.textProperty().bindBidirectional(credits, new NumberStringConverter());
  }

  public IntegerProperty getKnowledge() {
    return knowledge;
  }

  public IntegerProperty getQic() {
    return qic;
  }

  public IntegerProperty getOre() {
    return ore;
  }

  public IntegerProperty getCredits() {
    return credits;
  }

  public boolean isSaved() {
    return saved;
  }

  public void setSaved(boolean saved) {
    this.saved = saved;
  }

  @FXML
  private void addKnowledge() {
    Util.plus(knowledge, 1);
    Util.minus(powerToSpend, 4);
  }

  @FXML
  private void subtractKnowledge() {
    Util.minus(knowledge, 1);
    Util.plus(powerToSpend, 4);
  }

  @FXML
  private void addQic() {
    Util.plus(qic, 1);
    Util.minus(powerToSpend, 4);
  }

  @FXML
  private void subtractQic() {
    Util.minus(qic, 1);
    Util.plus(powerToSpend, 4);
  }

  @FXML
  private void addOre() {
    Util.plus(ore, 1);
    Util.minus(powerToSpend, 3);
  }

  @FXML
  private void subtractOre() {
    Util.minus(ore, 1);
    Util.plus(powerToSpend, 3);
  }

  @FXML
  private void addCredits() {
    Util.plus(credits, 1);
    Util.minus(powerToSpend, 1);
  }

  @FXML
  private void subtractCredits() {
    Util.minus(credits, 1);
    Util.plus(powerToSpend, 1);
  }
}
