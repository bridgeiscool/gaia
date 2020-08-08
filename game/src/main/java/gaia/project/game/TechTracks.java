package gaia.project.game;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;

import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class TechTracks extends GridPane {
  // Terraforming
  @FXML
  private StackPane terra5StackPane;
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
  private final List<HBox> terraTrack;

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
  private final List<HBox> navTrack;

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
  private final List<HBox> aiTrack;

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
  private final List<HBox> gaiaTrack;

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
  private final List<HBox> econTrack;

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
  private final List<HBox> knowledgeTrack;

  @FXML
  private HBox wildTechTile1;
  @FXML
  private HBox wildTechTile2;
  @FXML
  private HBox wildTechTile3;

  public TechTracks(Game game) {
    Preconditions.checkArgument(game.getAdvancedTechTiles().size() == 6);

    FXMLLoader loader = new FXMLLoader(TechTracks.class.getResource("TechTracks.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    terraTrack = Arrays.asList(terra0, terra1, terra2, terra3, terra4, terra5);
    navTrack = Arrays.asList(nav0, nav1, nav2, nav3, nav4, nav5);
    aiTrack = Arrays.asList(ai0, ai1, ai2, ai3, ai4, ai5);
    gaiaTrack = Arrays.asList(gaia0, gaia1, gaia2, gaia3, gaia4, gaia5);
    econTrack = Arrays.asList(econ0, econ1, econ2, econ3, econ4, econ5);
    knowledgeTrack = Arrays.asList(knowledge0, knowledge1, knowledge2, knowledge3, knowledge4, knowledge5);

    for (Player player : game.getPlayers().values()) {
      terraTrack.get(player.getTerraformingLevel().get()).getChildren().add(new TechMarker(player));
      navTrack.get(player.getNavLevel().get()).getChildren().add(new TechMarker(player));
      aiTrack.get(player.getAiLevel().get()).getChildren().add(new TechMarker(player));
      gaiaTrack.get(player.getGaiaformingLevel().get()).getChildren().add(new TechMarker(player));
      econTrack.get(player.getEconLevel().get()).getChildren().add(new TechMarker(player));
      knowledgeTrack.get(player.getKnowledgeLevel().get()).getChildren().add(new TechMarker(player));

      // Add listeners to update UI from changes to underlying players
      player.getTerraformingLevel().addListener(new ProcessTrackBump(terraTrack, player));
      player.getNavLevel().addListener(new ProcessTrackBump(navTrack, player));
      player.getAiLevel().addListener(new ProcessTrackBump(aiTrack, player));
      player.getGaiaformingLevel().addListener(new ProcessTrackBump(gaiaTrack, player));
      player.getEconLevel().addListener(new ProcessTrackBump(econTrack, player));
      player.getKnowledgeLevel().addListener(new ProcessTrackBump(knowledgeTrack, player));
    }

    // Add the randomly chosen fed tile to terra track
    terra5StackPane.getChildren().add(0, new FederationTokenPane(game.getTerraBonus(), 1.0));

    // Initialize the tech tiles
    add(new TechTileHBox(game.getTechTiles().get(0)), 0, 8);
    add(new TechTileHBox(game.getTechTiles().get(1)), 1, 8);
    add(new TechTileHBox(game.getTechTiles().get(2)), 2, 8);
    add(new TechTileHBox(game.getTechTiles().get(3)), 3, 8);
    add(new TechTileHBox(game.getTechTiles().get(4)), 4, 8);
    add(new TechTileHBox(game.getTechTiles().get(5)), 5, 8);
    wildTechTile1.getChildren().add(new TechTileHBox(game.getTechTiles().get(6)));
    wildTechTile2.getChildren().add(new TechTileHBox(game.getTechTiles().get(7)));
    wildTechTile3.getChildren().add(new TechTileHBox(game.getTechTiles().get(8)));

    // Initialize the adv tech tiles
    terraAdvTech.getChildren().add(new AdvancedTechTileHBox(game.getAdvancedTechTiles().get(0)));
    navAdvTech.getChildren().add(new AdvancedTechTileHBox(game.getAdvancedTechTiles().get(1)));
    aiAdvTech.getChildren().add(new AdvancedTechTileHBox(game.getAdvancedTechTiles().get(2)));
    gaiaAdvTech.getChildren().add(new AdvancedTechTileHBox(game.getAdvancedTechTiles().get(3)));
    econAdvTech.getChildren().add(new AdvancedTechTileHBox(game.getAdvancedTechTiles().get(4)));
    knowledgeAdvTech.getChildren().add(new AdvancedTechTileHBox(game.getAdvancedTechTiles().get(5)));

  }

  void highlightTracks(Player activePlayer, CallBack callBack) {
    activateHBox(activePlayer, callBack, terraTrack, activePlayer.getTerraformingLevel());
    activateHBox(activePlayer, callBack, navTrack, activePlayer.getNavLevel());
    activateHBox(activePlayer, callBack, aiTrack, activePlayer.getAiLevel());
    activateHBox(activePlayer, callBack, gaiaTrack, activePlayer.getGaiaformingLevel());
    activateHBox(activePlayer, callBack, econTrack, activePlayer.getEconLevel());
    activateHBox(activePlayer, callBack, knowledgeTrack, activePlayer.getKnowledgeLevel());
  }

  private void activateHBox(Player activePlayer, CallBack callBack, List<HBox> track, IntegerProperty toUpdate) {
    for (HBox hbox : track) {
      if (hbox.getChildren().stream().anyMatch(n -> ((TechMarker) n).player == activePlayer.getPlayerEnum())) {
        int idx = track.indexOf(hbox);
        if (idx < 4
            || (idx == 4
                && track.get(5).getChildren().isEmpty()
                && activePlayer.getFlippableTechTiles().intValue() > 0)) {
          hbox.getStyleClass().add("highlightedHbox");
          hbox.setOnMouseClicked(me -> {
            activePlayer.advanceTech(toUpdate);
            clearActivation();
            callBack.call();
          });
        }
      }
    }
  }

  void clearActivation() {
    terraTrack.forEach(this::clearActivation);
    navTrack.forEach(this::clearActivation);
    aiTrack.forEach(this::clearActivation);
    gaiaTrack.forEach(this::clearActivation);
    econTrack.forEach(this::clearActivation);
    knowledgeTrack.forEach(this::clearActivation);
  }

  private void clearActivation(HBox hbox) {
    hbox.getStyleClass().clear();
    hbox.setOnMouseClicked(null);
  }

  private static class ProcessTrackBump implements ChangeListener<Number> {
    private final List<HBox> track;
    private final Player player;

    ProcessTrackBump(List<HBox> track, Player player) {
      this.track = track;
      this.player = player;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
      track.get(oldValue.intValue()).getChildren().removeIf(n -> ((TechMarker) n).player == player.getPlayerEnum());
      track.get(newValue.intValue()).getChildren().add(new TechMarker(player));
    }
  }

  private static class TechMarker extends Circle {
    private final PlayerEnum player;

    TechMarker(Player player) {
      super(10, player.getRace().getColor());
      this.player = player.getPlayerEnum();
    }
  }
}
