package gaia.project.game;

import java.io.IOException;

import gaia.project.game.board.Mine;
import gaia.project.game.board.ResearchLab;
import gaia.project.game.board.TradingPost;
import gaia.project.game.model.Player;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

public class PlayerBoardController extends VBox {
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
  private Label oreLabel;
  @FXML
  private Label knowledgeLabel;
  @FXML
  private Label qicLabel;
  @FXML
  private Label gaiaBin;
  @FXML
  private Label bin1;
  @FXML
  private Label bin2;
  @FXML
  private Label bin3;

  // Income bar
  @FXML
  private Label creditIncome;
  @FXML
  private Label oreIncome;
  @FXML
  private Label knowledgeIncome;
  @FXML
  private Label qicIncome;
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
  private Label gaiaformers;

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
    getStyleClass().add(player.getRace().getBoardStyle());

    raceName.setText(player.getRace().getRaceName());
    vps.textProperty().bindBidirectional(player.getScore(), new NumberStringConverter());

    creditsLabel.textProperty().bindBidirectional(player.getCredits(), StringConverters.numberWithPrefix("$: "));
    oreLabel.textProperty().bindBidirectional(player.getOre(), StringConverters.numberWithPrefix("O: "));
    knowledgeLabel.textProperty().bindBidirectional(player.getResearch(), StringConverters.numberWithPrefix("K: "));
    qicLabel.textProperty().bindBidirectional(player.getQic(), StringConverters.numberWithPrefix("Q: "));
    gaiaBin.textProperty().bindBidirectional(player.getGaiaBin(), StringConverters.numberWithPrefix("Gaia Bin: "));
    bin1.textProperty().bindBidirectional(player.getBin1(), StringConverters.numberWithPrefix("I: "));
    bin2.textProperty().bindBidirectional(player.getBin2(), StringConverters.numberWithPrefix("II: "));
    bin3.textProperty().bindBidirectional(player.getBin3(), StringConverters.numberWithPrefix("III: "));

    creditIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getCreditIncome(), StringConverters.numberWithPrefix("$: "));
    oreIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getOreIncome(), StringConverters.numberWithPrefix("O: "));
    knowledgeIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getResearchIncome(), StringConverters.numberWithPrefix("K: "));
    qicIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getQicIncome(), StringConverters.numberWithPrefix("Q: "));
    ptIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getPowerIncome(), StringConverters.numberWithPrefix("PT: "));
    pIncome.textProperty()
        .bindBidirectional(player.getCurrentIncome().getChargeIncome(), StringConverters.numberWithPrefix("P: "));

    mines.setText(player.getMines().size() + " / 8");
    player.getMines().addListener((SetChangeListener<Mine>) change -> {
      mines.setText(player.getMines().size() + " / 8");
    });
    tradingPosts.setText(player.getTradingPosts().size() + " / 4");
    player.getTradingPosts().addListener((SetChangeListener<TradingPost>) change -> {
      tradingPosts.setText(player.getTradingPosts() + " / 4");
    });
    researchLabs.setText(player.getResearchLabs().size() + " / 3");
    player.getResearchLabs().addListener((SetChangeListener<ResearchLab>) change -> {
      researchLabs.setText(player.getResearchLabs() + " / 3");
    });
  }
}
