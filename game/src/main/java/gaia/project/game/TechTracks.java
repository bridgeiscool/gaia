package gaia.project.game;

import java.io.IOException;
import java.util.List;

import gaia.project.game.model.TechTile;
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
  @FXML
  private Label terraTechTileLabel;

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
  private HBox wildTechTile1;
  @FXML
  private HBox wildTechTile2;
  @FXML
  private HBox wildTechTile3;

  public TechTracks(List<TechTile> techTiles) {
    FXMLLoader loader = new FXMLLoader(TechTracks.class.getResource("TechTracks.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Initialize the tech tiles
    add(new TechTileRectangle(techTiles.get(0)), 0, 8);
    add(new TechTileRectangle(techTiles.get(1)), 1, 8);
    add(new TechTileRectangle(techTiles.get(2)), 2, 8);
    add(new TechTileRectangle(techTiles.get(3)), 3, 8);
    add(new TechTileRectangle(techTiles.get(4)), 4, 8);
    add(new TechTileRectangle(techTiles.get(5)), 5, 8);
    wildTechTile1.getChildren().add(new TechTileRectangle(techTiles.get(6)));
    wildTechTile2.getChildren().add(new TechTileRectangle(techTiles.get(7)));
    wildTechTile3.getChildren().add(new TechTileRectangle(techTiles.get(8)));

  }
}
