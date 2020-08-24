package gaia.project.game;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.function.Consumer;

import gaia.project.game.model.AdvancedTechTile;
import gaia.project.game.model.Coords;
import gaia.project.game.model.FederationTile;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerBoardAction;
import gaia.project.game.model.TechTile;
import javafx.beans.property.BooleanProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;

public class PlayerBoardController extends GridPane {
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
  private FlowPane tokenArea;

  public PlayerBoardController(Player player) {
    FXMLLoader loader = new FXMLLoader(PlayerBoardController.class.getResource("PlayerBoard.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.player = player;
    getStyleClass().add(player.getRace().getBoardStyle());

    raceName.setText(player.getRace().getRaceName());
    vps.textProperty().bindBidirectional(player.getScore(), new NumberStringConverter());

    creditsLabel.textProperty().bindBidirectional(player.getCredits(), StringConverters.numberWithPostfix("c"));
    creditIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getCreditIncome(), StringConverters.income());
    oreLabel.textProperty().bindBidirectional(player.getOre(), StringConverters.numberWithPostfix("o"));
    oreIncome.textProperty().bindBidirectional(player.getCurrentIncome().getOreIncome(), StringConverters.income());
    knowledgeLabel.textProperty().bindBidirectional(player.getResearch(), StringConverters.numberWithPostfix("k"));
    knowledgeIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getResearchIncome(), StringConverters.income());
    qicLabel.textProperty().bindBidirectional(player.getQic(), StringConverters.numberWithPostfix("q"));
    qicIncome.textProperty().bindBidirectional(player.getCurrentIncome().getQicIncome(), StringConverters.income());

    gaiaBin.textProperty().bindBidirectional(player.getGaiaBin(), StringConverters.numberWithPrefix("Gaia Bin: "));
    bin1.textProperty().bindBidirectional(player.getBin1(), StringConverters.numberWithPrefix("I: "));
    bin2.textProperty().bindBidirectional(player.getBin2(), StringConverters.numberWithPrefix("II: "));
    bin3.textProperty().bindBidirectional(player.getBin3(), StringConverters.numberWithPrefix("III: "));
    ptIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getPowerIncome(), StringConverters.numberWithPrefix("+PT: "));
    pIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getChargeIncome(), StringConverters.numberWithPrefix("+P: "));

    mines.setText(player.getMines().size() + " / 8");
    player.getMines()
        .addListener((SetChangeListener<Coords>) change -> mines.setText(player.getMines().size() + " / 8"));
    tradingPosts.setText(player.getTradingPosts().size() + " / 4");
    player.getTradingPosts()
        .addListener(
            (SetChangeListener<Coords>) change -> tradingPosts.setText(player.getTradingPosts().size() + " / 4"));
    researchLabs.setText(player.getResearchLabs().size() + " / 3");
    player.getResearchLabs()
        .addListener(
            (SetChangeListener<Coords>) change -> researchLabs.setText(player.getResearchLabs().size() + " / 3"));
    planetaryInstitute.setText(player.getPi().size() + " / 1");
    player.getPi()
        .addListener((SetChangeListener<Coords>) change -> planetaryInstitute.setText(player.getPi().size() + " / 1"));
    knowledgeAcademy.setText(player.getKa().size() + " / 1");
    player.getKa()
        .addListener((SetChangeListener<Coords>) change -> knowledgeAcademy.setText(player.getKa().size() + " / 1"));
    qicAcademy.setText(player.getQa().size() + " / 1");
    player.getQa()
        .addListener((SetChangeListener<Coords>) change -> qicAcademy.setText(player.getQa().size() + " / 1"));

    gaiaformers.setText(player.getGaiaformers().size() + " / ");
    player.getGaiaformers()
        .addListener((SetChangeListener<Coords>) change -> gaiaformers.setText(player.getGaiaformers().size() + " /"));
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

    for (Entry<FederationTile, BooleanProperty> entry : player.getFederationTiles().entrySet()) {
      tokenArea.getChildren().add(FederationTokenPane.mini(entry.getKey(), entry.getValue()));
    }

    player.getFederationTiles().addListener((MapChangeListener<FederationTile, BooleanProperty>) change -> {
      tokenArea.getChildren().add(FederationTokenPane.mini(change.getKey(), change.getValueAdded()));
    });

    // Special actions
    for (Entry<Serializable, BooleanProperty> entry : player.getSpecialActions().entrySet()) {
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
  }

  public void highlightActions(Consumer<Serializable> callback) {
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

  public void highlightTechTiles(Consumer<Serializable> callback) {
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
