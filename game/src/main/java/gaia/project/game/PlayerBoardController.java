package gaia.project.game;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.ImmutableList;

import gaia.project.game.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerBoardController extends VBox {
  private final Player player;

  @FXML
  private HBox resourceBar;
  @FXML
  private HBox powerCycleBar;
  @FXML
  private HBox buildingArea;

  // Research Bar
  private Ore ore;
  private Credits credits;
  private Research research;
  private Qic qic;

  List<GaiaformerSlot> avaiableGaiaformers;

  @FXML
  private PowerCycle powerCycle;

  @FXML
  private Label raceName;
  @FXML
  private Label raceSpecial;
  @FXML
  private HBox gaiaformers;

  public PlayerBoardController(Player player) {
    FXMLLoader loader = new FXMLLoader(PlayerBoardController.class.getResource("GameBoard.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.player = player;
    this.powerCycle = new PowerCycle(player.getGaia(), player.getBin1(), player.getBin2(), player.getBin3());
    powerCycleBar.getChildren().add(0, powerCycle);

    getStyleClass().add(player.getRace().getBoardStyle());

    // Resource bar
    ore = new Ore(player.getOre());
    credits = new Credits(player.getCredits());
    research = new Research(player.getResearch());
    qic = new Qic(player.getQic());
    resourceBar.getChildren().addAll(ore, credits, research, qic);

    raceName.setText(player.getRace().getRaceName());
    raceName.setTextFill(player.getRace().getTextColor());

    // Gaiaformer area
    avaiableGaiaformers = ImmutableList.of(new GaiaformerSlot(false, player.getRace().getColor()));
    gaiaformers.getChildren().addAll(avaiableGaiaformers);
  }
}
