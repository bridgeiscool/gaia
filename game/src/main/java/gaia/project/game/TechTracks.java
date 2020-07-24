package gaia.project.game;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class TechTracks extends GridPane {
  // Terraforming
  @FXML
  private Label terra5Label;
  @FXML
  private HBox terra5;
  @FXML
  private HBox terraAdvTech;
  @FXML
  private HBox terra4;
  @FXML
  private HBox terra3;
  @FXML
  private HBox terra2;
  @FXML
  private HBox terra1;
  @FXML
  private HBox terra0;
  @FXML
  private HBox terraTechTile;

  // Nav
  @FXML
  private HBox nav5;
  @FXML
  private HBox navAdvTech;
  @FXML
  private HBox nav4;
  @FXML
  private HBox nav3;
  @FXML
  private HBox nav2;
  @FXML
  private HBox nav1;
  @FXML
  private HBox nav0;
  @FXML
  private HBox navTechTile;

  // AI
  @FXML
  private HBox ai5;
  @FXML
  private HBox aiAdvTech;
  @FXML
  private HBox ai4;
  @FXML
  private HBox ai3;
  @FXML
  private HBox ai2;
  @FXML
  private HBox ai1;
  @FXML
  private HBox ai0;
  @FXML
  private HBox aiTechTile;

  // Gaiaforming
  @FXML
  private HBox gaia5;
  @FXML
  private HBox gaiaAdvTech;
  @FXML
  private HBox gaia4;
  @FXML
  private HBox gaia3;
  @FXML
  private HBox gaia2;
  @FXML
  private HBox gaia1;
  @FXML
  private HBox gaia0;
  @FXML
  private HBox gaiaTechTile;

  // Econ
  @FXML
  private HBox econ5;
  @FXML
  private HBox econAdvTech;
  @FXML
  private HBox econ4;
  @FXML
  private HBox econ3;
  @FXML
  private HBox econ2;
  @FXML
  private HBox econ1;
  @FXML
  private HBox econ0;
  @FXML
  private HBox econTechTile;

  // Knowledge
  @FXML
  private HBox knowledge5;
  @FXML
  private HBox knowledgeAdvTech;
  @FXML
  private HBox knowledge4;
  @FXML
  private HBox knowledge3;
  @FXML
  private HBox knowledge2;
  @FXML
  private HBox knowledge1;
  @FXML
  private HBox knowledge0;
  @FXML
  private HBox knowledgeTechTile;

  public TechTracks() {
    FXMLLoader loader = new FXMLLoader(TechTracks.class.getResource("TechTracks.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
