package gaia.project.game;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import gaia.project.game.model.AdvancedTechTile;
import gaia.project.game.model.FederationTile;
import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import gaia.project.game.model.Util;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class TechTracks extends GridPane {
  public static final int TERRA = 0;
  public static final int NAV = 1;
  public static final int AI = 2;
  public static final int GAIA = 3;
  public static final int ECON = 4;
  public static final int KNOWLEDGE = 5;

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

  private List<TechTileHBox> techTiles;

  // Advanced tech tiles
  private AdvancedTechTileHBox terraTech;
  private AdvancedTechTileHBox navTech;
  private AdvancedTechTileHBox aiTech;
  private AdvancedTechTileHBox gaiaTech;
  private AdvancedTechTileHBox econTech;
  private AdvancedTechTileHBox knowledgeTech;

  private Consumer<Player> lostPlanetCallback;

  public TechTracks(Game game, Consumer<Player> lostPlanetCallback) {
    Preconditions.checkArgument(game.getAdvancedTechTiles().size() == 6);

    FXMLLoader loader = new FXMLLoader(TechTracks.class.getResource("TechTracks.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.lostPlanetCallback = lostPlanetCallback;

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
    if (!game.getPlayers().values().stream().anyMatch(p -> p.getTerraformingLevel().get() == 5)) {
      terra5StackPane.getChildren().add(0, FederationTokenPane.regular(game.getTerraBonus()));
    }

    // Initialize the tech tiles
    techTiles = game.getTechTiles().stream().map(TechTileHBox::new).collect(Collectors.toList());
    add(techTiles.get(TERRA), 0, 8);
    add(techTiles.get(NAV), 1, 8);
    add(techTiles.get(AI), 2, 8);
    add(techTiles.get(GAIA), 3, 8);
    add(techTiles.get(ECON), 4, 8);
    add(techTiles.get(KNOWLEDGE), 5, 8);
    wildTechTile1.getChildren().add(techTiles.get(6));
    wildTechTile2.getChildren().add(techTiles.get(7));
    wildTechTile3.getChildren().add(techTiles.get(8));

    // Initialize the adv tech tiles - map is linked hash map so
    Iterator<Entry<AdvancedTechTile, Boolean>> iterator = game.getAdvancedTechTiles().entrySet().iterator();
    this.terraTech = new AdvancedTechTileHBox(iterator.next());
    this.navTech = new AdvancedTechTileHBox(iterator.next());
    this.aiTech = new AdvancedTechTileHBox(iterator.next());
    this.gaiaTech = new AdvancedTechTileHBox(iterator.next());
    this.econTech = new AdvancedTechTileHBox(iterator.next());
    this.knowledgeTech = new AdvancedTechTileHBox(iterator.next());

    // Add listeners to update the game when these are taken
    terraTech.getTaken().addListener((o, oldValue, newValue) -> {
      game.getAdvancedTechTiles().put(terraTech.getTechTile(), Boolean.TRUE);
    });
    navTech.getTaken().addListener((o, oldValue, newValue) -> {
      game.getAdvancedTechTiles().put(navTech.getTechTile(), Boolean.TRUE);
    });
    aiTech.getTaken().addListener((o, oldValue, newValue) -> {
      game.getAdvancedTechTiles().put(aiTech.getTechTile(), Boolean.TRUE);
    });
    gaiaTech.getTaken().addListener((o, oldValue, newValue) -> {
      game.getAdvancedTechTiles().put(gaiaTech.getTechTile(), Boolean.TRUE);
    });
    econTech.getTaken().addListener((o, oldValue, newValue) -> {
      game.getAdvancedTechTiles().put(econTech.getTechTile(), Boolean.TRUE);
    });
    knowledgeTech.getTaken().addListener((o, oldValue, newValue) -> {
      game.getAdvancedTechTiles().put(knowledgeTech.getTechTile(), Boolean.TRUE);
    });

    terraAdvTech.getChildren().add(terraTech);
    navAdvTech.getChildren().add(navTech);
    aiAdvTech.getChildren().add(aiTech);
    gaiaAdvTech.getChildren().add(gaiaTech);
    econAdvTech.getChildren().add(econTech);
    knowledgeAdvTech.getChildren().add(knowledgeTech);

  }

  void highlightTracks(Player activePlayer, CallBack callBack, boolean free) {
    activateHBox(activePlayer, callBack, terraTrack, activePlayer.getTerraformingLevel(), free, true, false);
    activateHBox(activePlayer, callBack, navTrack, activePlayer.getNavLevel(), free, false, true);
    activateHBox(activePlayer, callBack, aiTrack, activePlayer.getAiLevel(), free, false, false);
    activateHBox(activePlayer, callBack, gaiaTrack, activePlayer.getGaiaformingLevel(), free, false, false);
    activateHBox(activePlayer, callBack, econTrack, activePlayer.getEconLevel(), free, false, false);
    activateHBox(activePlayer, callBack, knowledgeTrack, activePlayer.getKnowledgeLevel(), free, false, false);
  }

  private void activateHBox(
      Player activePlayer,
      CallBack callBack,
      List<HBox> track,
      IntegerProperty toUpdate,
      boolean free,
      boolean terra,
      boolean nav) {
    for (HBox hbox : track) {
      if (hbox.getChildren().stream().anyMatch(n -> ((TechMarker) n).player == activePlayer.getPlayerEnum())) {
        int idx = track.indexOf(hbox);
        if (idx < 4
            || (idx == 4 && track.get(5).getChildren().isEmpty() && activePlayer.hasFlippableFederationTile())) {
          hbox.getStyleClass().add("highlightedHbox");
          hbox.setOnMouseClicked(me -> {
            activePlayer.advanceTech(toUpdate, free);
            clearActivation();
            if (terra && idx == 4) {
              // Federation token is on the bottom...
              FederationTile federationTile =
                  ((FederationTokenPane) terra5StackPane.getChildren().remove(0)).getFederationTile();
              federationTile.updatePlayer(activePlayer);
              activePlayer.getFederationTiles()
                  .put(federationTile, new SimpleBooleanProperty(federationTile.isFlippable()));
            }
            if (nav && idx == 4) {
              lostPlanetCallback.accept(activePlayer);
            } else {
              callBack.call();
            }
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

  void highlightTechTiles(Player activePlayer, CallBack callback, CallBack wildCallback, CallBack advCallback) {
    techTiles.get(TERRA).highlight(activePlayer, callback, Optional.of(p -> {
      if (p.getTerraformingLevel().getValue() < 4
          || (p.getTerraformingLevel().getValue() == 4
              && terra5.getChildren().isEmpty()
              && p.hasFlippableFederationTile())) {
        if (p.getTerraformingLevel().get() == 4) {
          // Federation token is on the bottom...
          FederationTile federationTile =
              ((FederationTokenPane) terra5StackPane.getChildren().remove(0)).getFederationTile();
          federationTile.updatePlayer(activePlayer);
          activePlayer.getFederationTiles()
              .put(federationTile, new SimpleBooleanProperty(federationTile.isFlippable()));
        }
        Util.plus(p.getTerraformingLevel(), 1);
      }
    }));
    techTiles.get(NAV).highlight(activePlayer, callback, Optional.of(p -> {
      if (p.getNavLevel().getValue() < 4
          || (p.getNavLevel().getValue() == 4 && nav5.getChildren().isEmpty() && p.hasFlippableFederationTile())) {
        Util.plus(p.getNavLevel(), 1);
        if (p.getNavLevel().get() == 5) {
          lostPlanetCallback.accept(p);
        }
      }
    }));
    techTiles.get(AI).highlight(activePlayer, callback, Optional.of(p -> {
      if (p.getAiLevel().getValue() < 4
          || (p.getAiLevel().getValue() == 4 && ai5.getChildren().isEmpty() && p.hasFlippableFederationTile())) {
        Util.plus(p.getAiLevel(), 1);
      }
    }));
    techTiles.get(GAIA).highlight(activePlayer, callback, Optional.of(p -> {
      if (p.getGaiaformingLevel().getValue() < 4
          || (p.getGaiaformingLevel().getValue() == 4
              && gaia5.getChildren().isEmpty()
              && p.hasFlippableFederationTile())) {
        Util.plus(p.getGaiaformingLevel(), 1);
      }
    }));
    techTiles.get(ECON).highlight(activePlayer, callback, Optional.of(p -> {
      if (p.getEconLevel().getValue() < 4
          || (p.getEconLevel().getValue() == 4 && econ5.getChildren().isEmpty() && p.hasFlippableFederationTile())) {
        Util.plus(p.getEconLevel(), 1);
      }
    }));
    techTiles.get(KNOWLEDGE).highlight(activePlayer, callback, Optional.of(p -> {
      if (p.getKnowledgeLevel().getValue() < 4
          || (p.getKnowledgeLevel().getValue() == 4
              && knowledge5.getChildren().isEmpty()
              && p.hasFlippableFederationTile())) {
        Util.plus(p.getKnowledgeLevel(), 1);
      }
    }));

    // Wild ones get a callback from the main controller to pick the tech track
    techTiles.subList(6, 9).stream().forEach(tt -> {
      tt.highlight(activePlayer, wildCallback, Optional.empty());
    });

    // Advanced tech tiles
    if (activePlayer.hasFlippableFederationTile()) {
      if (!terraTech.isTaken() && activePlayer.getTerraformingLevel().get() > 3) {
        terraTech.highlight(activePlayer, advCallback);
      }

      if (!navTech.isTaken() && activePlayer.getNavLevel().get() > 3) {
        navTech.highlight(activePlayer, advCallback);
      }

      if (!aiTech.isTaken() && activePlayer.getAiLevel().get() > 3) {
        aiTech.highlight(activePlayer, advCallback);
      }

      if (!gaiaTech.isTaken() && activePlayer.getGaiaformingLevel().get() > 3) {
        gaiaTech.highlight(activePlayer, advCallback);
      }

      if (!econTech.isTaken() && activePlayer.getEconLevel().get() > 3) {
        econTech.highlight(activePlayer, advCallback);
      }

      if (!knowledgeTech.isTaken() && activePlayer.getKnowledgeLevel().get() > 3) {
        knowledgeTech.highlight(activePlayer, advCallback);
      }
    }
  }

  void clearTechtileHighlighting() {
    techTiles.forEach(tt -> tt.clearHighlighting());
    terraTech.clearHighlighting();
    navTech.clearHighlighting();
    aiTech.clearHighlighting();
    gaiaTech.clearHighlighting();
    econTech.clearHighlighting();
    knowledgeTech.clearHighlighting();
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
