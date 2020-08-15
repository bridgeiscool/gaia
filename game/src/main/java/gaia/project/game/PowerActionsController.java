package gaia.project.game;

import java.io.IOException;

import gaia.project.game.model.Game;
import gaia.project.game.model.Player;
import gaia.project.game.model.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class PowerActionsController extends GridPane {
  private final GameController gameController;

  @FXML
  private Action k3;
  @FXML
  private Action doubleTf;
  @FXML
  private Action ore;
  @FXML
  private Action credits;
  @FXML
  private Action k2;
  @FXML
  private Action tf;
  @FXML
  private Action pt;
  @FXML
  private Action q4;
  @FXML
  private Action q3;
  @FXML
  private Action q2;

  public PowerActionsController(GameController gameController) {
    FXMLLoader loader = new FXMLLoader(PlayerBoardController.class.getResource("PowerActions.fxml"));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.gameController = gameController;
    Game game = gameController.getGame();

    // Initialize action styles

    k3.setTaken(game.getK3ActionTaken().getValue());
    doubleTf.setTaken(game.getDoubleTfActionTaken().getValue());
    ore.setTaken(game.getOreActionTaken().getValue());
    credits.setTaken(game.getCreditsActionTaken().getValue());
    k2.setTaken(game.getK2ActionTaken().getValue());
    tf.setTaken(game.getTfActionTaken().getValue());
    pt.setTaken(game.getPtActionTaken().getValue());

    q4.setTaken(game.getQ4ActionTaken().getValue());
    q3.setTaken(game.getQ3ActionTaken().getValue());
    q2.setTaken(game.getQ2ActionTaken().getValue());

    game.getK3ActionTaken().addListener((o, oldValue, newValue) -> k3.setTaken(newValue));
    game.getDoubleTfActionTaken().addListener((o, oldValue, newValue) -> doubleTf.setTaken(newValue));
    game.getOreActionTaken().addListener((o, oldValue, newValue) -> ore.setTaken(newValue));
    game.getCreditsActionTaken().addListener((o, oldValue, newValue) -> credits.setTaken(newValue));
    game.getK2ActionTaken().addListener((o, oldValue, newValue) -> k2.setTaken(newValue));
    game.getTfActionTaken().addListener((o, oldValue, newValue) -> tf.setTaken(newValue));
    game.getPtActionTaken().addListener((o, oldValue, newValue) -> pt.setTaken(newValue));
    game.getQ4ActionTaken().addListener((o, oldValue, newValue) -> q4.setTaken(newValue));
    game.getQ3ActionTaken().addListener((o, oldValue, newValue) -> q3.setTaken(newValue));
    game.getQ2ActionTaken().addListener((o, oldValue, newValue) -> q2.setTaken(newValue));
  }

  public void highlightActions(Player activePlayer, CallBack callback) {
    if (activePlayer.getBin3().intValue() >= 7) {
      k3.tryHighlight(activePlayer, p -> {
        Util.plus(activePlayer.getResearch(), 3);
        p.spendPower(7);
        gameController.getGame().getK3ActionTaken().setValue(true);
      }, callback);
    }

    if (activePlayer.getBin3().intValue() >= 5
        && activePlayer.getMines().size() < 8
        && activePlayer.getOre().intValue() > 0
        && activePlayer.getCredits().intValue() > 1) {
      doubleTf.tryHighlight(activePlayer, p -> {
        Util.plus(activePlayer.getCurrentDigs(), 2);
        p.spendPower(5);
        gameController.getGame().getDoubleTfActionTaken().setValue(true);
      }, () -> {
        gameController.selectMineBuild();
        clearHighlighting();
      });
    }

    if (activePlayer.getBin3().intValue() >= 4) {
      ore.tryHighlight(activePlayer, p -> {
        Util.plus(activePlayer.getOre(), 2);
        p.spendPower(4);
        gameController.getGame().getOreActionTaken().setValue(true);
      }, callback);
    }

    if (activePlayer.getBin3().intValue() >= 4) {
      credits.tryHighlight(activePlayer, p -> {
        Util.plus(activePlayer.getCredits(), 7);
        p.spendPower(4);
        gameController.getGame().getCreditsActionTaken().setValue(true);
      }, callback);
    }

    if (activePlayer.getBin3().intValue() >= 4) {
      k2.tryHighlight(activePlayer, p -> {
        Util.plus(activePlayer.getResearch(), 2);
        p.spendPower(4);
        gameController.getGame().getK2ActionTaken().setValue(true);
      }, callback);
    }

    if (activePlayer.getBin3().intValue() >= 3
        && activePlayer.getMines().size() < 8
        && activePlayer.getOre().intValue() > 0
        && activePlayer.getCredits().intValue() > 1) {
      tf.tryHighlight(activePlayer, p -> {
        Util.plus(activePlayer.getCurrentDigs(), 1);
        p.spendPower(3);
        gameController.getGame().getTfActionTaken().setValue(true);
      }, () -> {
        gameController.selectMineBuild();
        clearHighlighting();
      });
    }

    if (activePlayer.getBin3().intValue() >= 3) {
      pt.tryHighlight(activePlayer, p -> {
        Util.plus(activePlayer.getBin1(), 2);
        gameController.getGame().getPtActionTaken().setValue(true);
        p.spendPower(3);
      }, callback);
    }

    if (activePlayer.getQic().intValue() >= 4) {
      q4.tryHighlight(activePlayer, p -> {
        // TODO: Implement gaining tech tiles!
        Util.minus(p.getQic(), 4);
        gameController.getGame().getQ4ActionTaken().setValue(true);
      }, callback);
    }

    if (activePlayer.getQic().intValue() >= 3) {
      q3.tryHighlight(activePlayer, p -> {
        // TODO: Implement federation tile selection
        Util.minus(p.getQic(), 3);
        gameController.getGame().getQ3ActionTaken().setValue(true);
      }, callback);
    }

    if (activePlayer.getQic().intValue() >= 2) {
      q2.tryHighlight(activePlayer, p -> {
        Util.plus(p.getScore(), 3 + p.getBuiltOn().size());
        Util.minus(p.getQic(), 2);
        gameController.getGame().getQ2ActionTaken().setValue(true);
      }, callback);
    }
  }

  public void clearHighlighting() {
    k3.clearHighlighting();
    doubleTf.clearHighlighting();
    ore.clearHighlighting();
    credits.clearHighlighting();
    k2.clearHighlighting();
    tf.clearHighlighting();
    pt.clearHighlighting();
    q4.clearHighlighting();
    q3.clearHighlighting();
    q2.clearHighlighting();
  }
}
