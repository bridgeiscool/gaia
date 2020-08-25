package gaia.project.game.model;

import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.TradingPost;
import javafx.beans.property.SimpleBooleanProperty;

public class FiraksPlayer extends Player {
  private static final long serialVersionUID = 3937559742818721321L;

  public FiraksPlayer(PlayerEnum playerEnum) {
    super(Race.FIRAKS, playerEnum);
  }

  public void piAction(HexWithPlanet hex) {
    TradingPost tp = new TradingPost(hex, getRace().getColor(), getPlayerEnum());
    hex.switchBuildingUI(tp);

    getResearchLabs().remove(hex.getCoords());
    getTradingPosts().add(hex.getCoords());

    // Update income
    getTpIncome().get(getTradingPosts().size() - 1).addTo(getCurrentIncome());
    getRlIncome().get(getResearchLabs().size()).removeFrom(getCurrentIncome());
  }

  @Override
  public void buildPI(HexWithPlanet hex) {
    super.buildPI(hex);
    getSpecialActions().put(PlayerBoardAction.RL_TO_TP, new SimpleBooleanProperty(false));
  }
}
