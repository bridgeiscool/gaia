package gaia.project.game;

import java.io.IOException;

import gaia.project.game.model.FederationTile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FederationTokens extends HBox {
  @FXML
  private VBox knowledgeBox;
  @FXML
  private Label knowledgeCount;
  @FXML
  private VBox coinsBox;
  @FXML
  private Label coinsCount;
  @FXML
  private VBox oreBox;
  @FXML
  private Label oreCount;
  @FXML
  private VBox ptBox;
  @FXML
  private Label ptCount;
  @FXML
  private VBox qicBox;
  @FXML
  private Label qicCount;
  @FXML
  private VBox vpBox;
  @FXML
  private Label vpCount;

  public FederationTokens(Game game) {
    FXMLLoader loader = new FXMLLoader(TechTracks.class.getResource("FederationTokens.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Add tokens to UI
    knowledgeBox.getChildren().add(new FederationTokenPane(FederationTile.RESEARCH, 1.0));
    knowledgeCount.setText(game.getTerraBonus() == FederationTile.RESEARCH ? "2" : "3");
    coinsBox.getChildren().add(new FederationTokenPane(FederationTile.CREDITS, 1.0));
    coinsCount.setText(game.getTerraBonus() == FederationTile.CREDITS ? "2" : "3");
    oreBox.getChildren().add(new FederationTokenPane(FederationTile.ORE, 1.0));
    oreCount.setText(game.getTerraBonus() == FederationTile.ORE ? "2" : "3");
    ptBox.getChildren().add(new FederationTokenPane(FederationTile.POWER, 1.0));
    ptCount.setText(game.getTerraBonus() == FederationTile.POWER ? "2" : "3");
    qicBox.getChildren().add(new FederationTokenPane(FederationTile.QIC, 1.0));
    qicCount.setText(game.getTerraBonus() == FederationTile.QIC ? "2" : "3");
    vpBox.getChildren().add(new FederationTokenPane(FederationTile.VP, 1.0));
    vpCount.setText(game.getTerraBonus() == FederationTile.VP ? "2" : "3");
  }
}
