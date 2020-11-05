package gaia.project.game.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import gaia.project.game.board.BoardUtils;
import gaia.project.game.model.AdvancedTechTile;
import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import gaia.project.game.model.Player.FedToken;
import gaia.project.game.model.PlayerBoardAction;
import gaia.project.game.model.TaklonsPlayer;
import gaia.project.game.model.TaklonsPlayer.Bin;
import gaia.project.game.model.TechTile;
import javafx.beans.property.BooleanProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.converter.NumberStringConverter;

public class PlayerBoardController extends GridPane {
  private static final double BRAINSTONE_BASE_SIZE = 8.0;
  private static final double BASE_WIDTH = 600;
  private static final double BASE_HEIGHT = 200;

  private static final double BASE_COLUMN_WIDTH = 300;
  private static final double BASE_ROW_HEIGHT = 25;
  private static final double BASE_SPACING = 7;

  private static final double BASE_FONT_SIZE = 20;

  private final Player player;

  // Top Bar
  @FXML
  private Label raceName;
  @FXML
  private Label vps;

  // Resources Bar
  @FXML
  private Label creditsLabel;
  @FXML
  private Label creditIncome;
  @FXML
  private Label oreLabel;
  @FXML
  private Label oreIncome;
  @FXML
  private Label knowledgeLabel;
  @FXML
  private Label knowledgeIncome;
  @FXML
  private Label qicLabel;
  @FXML
  private Label qicIncome;

  // Power Cycle bar
  @FXML
  private Label gaiaBin;
  @FXML
  private Label bin1;
  @FXML
  private Label bin2;
  @FXML
  private Label bin3;
  @FXML
  private Label ptIncome;
  @FXML
  private Label pIncome;
  @FXML
  private HBox bin1Box;
  @FXML
  private HBox bin2Box;
  @FXML
  private HBox bin3Box;
  @FXML
  private HBox gaiaBox;

  // Buildings bar
  @FXML
  private Label mines;
  @FXML
  private Label tradingPosts;
  @FXML
  private Label researchLabs;
  @FXML
  private Label planetaryInstitute;
  @FXML
  private Label knowledgeAcademy;
  @FXML
  private Label qicAcademy;
  @FXML
  private Label gaiaformers;
  @FXML
  private Label availableGaiaformers;

  @FXML
  private HBox topLine;
  @FXML
  private HBox secondLine;
  @FXML
  private HBox thirdLine;
  @FXML
  private GridPane bottomLine;
  @FXML
  private FlowPane tokenArea;

  // Only used for Taklons
  private Circle brainstone = new Circle(BRAINSTONE_BASE_SIZE * BoardUtils.getScaling(), Color.PURPLE);

  public PlayerBoardController(Player player, Game game) {
    FXMLLoader loader = new FXMLLoader(PlayerBoardController.class.getResource("PlayerBoard.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.player = player;

    // Set sizing
    getStyleClass().add(player.getRace().getBoardStyle());
    setPrefWidth(BASE_WIDTH * BoardUtils.getScaling());
    setPrefHeight(BASE_HEIGHT * BoardUtils.getScaling());
    topLine.setSpacing(BASE_SPACING * BoardUtils.getScaling());
    topLine.setPrefWidth(BASE_COLUMN_WIDTH * BoardUtils.getScaling());
    topLine.setPrefHeight(BASE_ROW_HEIGHT * BoardUtils.getScaling());
    secondLine.setSpacing(BASE_SPACING * BoardUtils.getScaling());
    secondLine.setPrefWidth(BASE_COLUMN_WIDTH * BoardUtils.getScaling());
    secondLine.setPrefHeight(BASE_ROW_HEIGHT * BoardUtils.getScaling());
    // This uses different spacing since it's
    thirdLine.setSpacing(45 * BoardUtils.getScaling());
    thirdLine.setPrefWidth(BASE_COLUMN_WIDTH * BoardUtils.getScaling());
    thirdLine.setPrefHeight(BASE_ROW_HEIGHT * BoardUtils.getScaling());
    bottomLine.setHgap(10 * BoardUtils.getScaling());
    bin3Box.setPadding(new Insets(0, 0, 0, 25 * BoardUtils.getScaling()));
    gaiaBox.setPadding(new Insets(0, 25 * BoardUtils.getScaling(), 0, 0));
    // Font sizing
    for (Node node : lookupAll(".label")) {
      ((Label) node).setFont(new Font(BASE_FONT_SIZE * BoardUtils.getScaling()));
    }

    // Set up listeners
    raceName.setText(player.getRace().getRaceName());
    vps.textProperty().bindBidirectional(player.getScore(), new NumberStringConverter());

    creditsLabel.textProperty().bindBidirectional(player.getCredits(), StringConverters.numberWithPostfix("c"));
    creditIncome.textProperty()
        .bindBidirectional(
            player.getCurrentIncome().getCreditIncome(),
            StringConverters.income(game.getCurrentRound()));
    oreLabel.textProperty().bindBidirectional(player.getOre(), StringConverters.numberWithPostfix("o"));
    oreIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getOreIncome(), StringConverters.income(game.getCurrentRound()));
    knowledgeLabel.textProperty().bindBidirectional(player.getResearch(), StringConverters.numberWithPostfix("k"));
    knowledgeIncome.textProperty()
        .bindBidirectional(
            player.getCurrentIncome().getResearchIncome(),
            StringConverters.income(game.getCurrentRound()));
    qicLabel.textProperty().bindBidirectional(player.getQic(), StringConverters.numberWithPostfix("q"));
    qicIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getQicIncome(), StringConverters.income(game.getCurrentRound()));

    gaiaBin.textProperty().bindBidirectional(player.getGaiaBin(), StringConverters.numberWithPrefix("G: "));
    bin1.textProperty().bindBidirectional(player.getBin1(), StringConverters.numberWithPrefix("I: "));
    bin2.textProperty().bindBidirectional(player.getBin2(), StringConverters.numberWithPrefix("II: "));
    ptIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getPowerIncome(), StringConverters.income(game.getCurrentRound()));
    bin3.textProperty().bindBidirectional(player.getBin3(), StringConverters.numberWithPrefix("III: "));
    pIncome.textProperty()
        .bindBidirectional(
            player.getCurrentIncome().getChargeIncome(),
            StringConverters.income(game.getCurrentRound()));

    mines.setText(player.getMines().size() + " / 8");
    player.getMines()
        .addListener((SetChangeListener<String>) change -> mines.setText(player.getMines().size() + " / 8"));
    tradingPosts.setText(player.getTradingPosts().size() + " / 4");
    player.getTradingPosts()
        .addListener(
            (SetChangeListener<String>) change -> tradingPosts.setText(player.getTradingPosts().size() + " / 4"));
    researchLabs.setText(player.getResearchLabs().size() + " / 3");
    player.getResearchLabs()
        .addListener(
            (SetChangeListener<String>) change -> researchLabs.setText(player.getResearchLabs().size() + " / 3"));
    planetaryInstitute.setText(player.getPi().size() + " / 1");
    player.getPi()
        .addListener((SetChangeListener<String>) change -> planetaryInstitute.setText(player.getPi().size() + " / 1"));
    knowledgeAcademy.setText(player.getKa().size() + " / 1");
    player.getKa()
        .addListener((SetChangeListener<String>) change -> knowledgeAcademy.setText(player.getKa().size() + " / 1"));
    qicAcademy.setText(player.getQa().size() + " / 1");
    player.getQa()
        .addListener((SetChangeListener<String>) change -> qicAcademy.setText(player.getQa().size() + " / 1"));

    gaiaformers.setText(player.getGaiaformers().size() + " / ");
    player.getGaiaformers()
        .addListener((SetChangeListener<String>) change -> gaiaformers.setText(player.getGaiaformers().size() + " /"));
    availableGaiaformers.textProperty()
        .bindBidirectional(player.getAvailableGaiaformers(), new NumberStringConverter());

    // Tech Tiles
    player.getTechTiles().forEach(tt -> tokenArea.getChildren().add(new MiniTechTile(player, tt)));
    player.getTechTiles().addListener((SetChangeListener<TechTile>) change -> {
      if (change.wasAdded()) {
        tokenArea.getChildren().add(new MiniTechTile(player, change.getElementAdded()));
      } else {
        tokenArea.getChildren()
            .removeIf(
                node -> node instanceof MiniTechTile
                    && ((MiniTechTile) node).getTechTile() == change.getElementRemoved());
      }
    });

    // Advanced tech tiles
    player.getAdvTechTiles().forEach(tt -> tokenArea.getChildren().add(new MiniAdvancedTechTile(player, tt)));
    player.getAdvTechTiles().addListener((SetChangeListener<AdvancedTechTile>) change -> {
      if (change.wasAdded()) {
        tokenArea.getChildren().add(new MiniAdvancedTechTile(player, change.getElementAdded()));
      }
    });

    player.getFederationTiles().forEach(ft -> tokenArea.getChildren().add(FederationTokenPane.mini(ft)));

    player.getFederationTiles().addListener((SetChangeListener<FedToken>) change -> {
      tokenArea.getChildren().add(FederationTokenPane.mini(change.getElementAdded()));
    });

    // Special actions
    for (Entry<Enum<?>, BooleanProperty> entry : player.getSpecialActions().entrySet()) {
      if (entry.getKey() instanceof PlayerBoardAction) {
        PlayerBoardAction key = (PlayerBoardAction) entry.getKey();
        tokenArea.getChildren().add(new SpecialAction(key, key.display(), entry.getValue()));
      }
    }

    player.getSpecialActions().addListener((MapChangeListener<Serializable, BooleanProperty>) change -> {
      if (change.getKey() instanceof PlayerBoardAction) {
        PlayerBoardAction key = (PlayerBoardAction) change.getKey();
        tokenArea.getChildren().add(new SpecialAction(key, key.display(), change.getValueAdded()));
      }
    });

    if (player instanceof TaklonsPlayer) {
      TaklonsPlayer taklons = (TaklonsPlayer) player;
      getBox(taklons.getBrainStone().getValue()).getChildren().add(1, brainstone);
      taklons.getBrainStone().addListener((o, oldValue, newValue) -> {
        getBox(oldValue).getChildren().remove(brainstone);
        HBox newBox = getBox(newValue);
        if (newBox != null) {
          newBox.getChildren().add(1, brainstone);
        }
      });
    }

  }

  @Nullable
  private HBox getBox(Bin bin) {
    switch (bin) {
      case I:
        return bin1Box;
      case II:
        return bin2Box;
      case III:
        return bin3Box;
      case GAIA:
        return gaiaBox;
      case REMOVED:
        // Returning null should remove the brainstone from the UI...
        return null;
    }

    throw new IllegalStateException("No such bin!");
  }

  public void highlightActions(Consumer<Enum<?>> callback) {
    tokenArea.getChildren()
        .stream()
        .filter(MiniTechTile.class::isInstance)
        .map(MiniTechTile.class::cast)
        .filter(MiniTechTile::isAction)
        .filter(tt -> !tt.isTaken())
        .forEach(tt -> tt.highlight(player, callback, true));

    tokenArea.getChildren()
        .stream()
        .filter(MiniAdvancedTechTile.class::isInstance)
        .map(MiniAdvancedTechTile.class::cast)
        .filter(MiniAdvancedTechTile::isAction)
        .filter(tt -> !tt.isTaken())
        .forEach(tt -> tt.highlight(player, callback));

    tokenArea.getChildren()
        .stream()
        .filter(SpecialAction.class::isInstance)
        .map(SpecialAction.class::cast)
        .forEach(a -> a.tryHighlight(player, callback));
  }

  public void highlightFederations(Player activePlayer, CallBack callback) {
    tokenArea.getChildren()
        .stream()
        .filter(FederationTokenPane.class::isInstance)
        .map(FederationTokenPane.class::cast)
        .forEach(ft -> ft.highlightForCopy(activePlayer, callback));
  }

  public void highlightTechTiles(Consumer<Enum<?>> callback) {
    tokenArea.getChildren()
        .stream()
        .filter(MiniTechTile.class::isInstance)
        .map(MiniTechTile.class::cast)
        .forEach(tt -> tt.highlight(player, callback, false));
  }

  public void clearHighlighting() {
    tokenArea.getChildren()
        .stream()
        .filter(MiniTechTile.class::isInstance)
        .map(MiniTechTile.class::cast)
        .forEach(MiniTechTile::clearHighlighting);

    tokenArea.getChildren()
        .stream()
        .filter(MiniAdvancedTechTile.class::isInstance)
        .map(MiniAdvancedTechTile.class::cast)
        .forEach(MiniAdvancedTechTile::clearHighlighting);

    tokenArea.getChildren()
        .stream()
        .filter(SpecialAction.class::isInstance)
        .map(SpecialAction.class::cast)
        .forEach(SpecialAction::clearHighlighting);
  }

  public void clearFederationHighlighting() {
    tokenArea.getChildren()
        .stream()
        .filter(FederationTokenPane.class::isInstance)
        .map(FederationTokenPane.class::cast)
        .forEach(FederationTokenPane::clearHighlighting);
  }
}
