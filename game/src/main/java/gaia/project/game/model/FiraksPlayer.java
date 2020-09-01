package gaia.project.game.model;

import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.TradingPost;
import javafx.beans.property.SimpleBooleanProperty;

public class FiraksPlayer extends Player {
  private static final long serialVersionUID = 3937559742818721321L;
  private transient boolean ignoreTpRoundBonus;

  public FiraksPlayer(PlayerEnum playerEnum) {
    super(Race.FIRAKS, playerEnum);
  }

  public void piAction(HexWithPlanet hex) {
    TradingPost tp = new TradingPost(hex, Race.FIRAKS.getColor(), getPlayerEnum());
    hex.switchBuildingUI(tp);

    ignoreTpRoundBonus = true;
    getResearchLabs().remove(hex.getCoords());
    getTradingPosts().add(hex.getCoords());
    ignoreTpRoundBonus = false;

    // Update income
    Race.FIRAKS.getTpIncome().get(getTradingPosts().size() - 1).addTo(getCurrentIncome());
    Race.FIRAKS.getRlIncome().get(getResearchLabs().size()).removeFrom(getCurrentIncome());
  }

  @Override
  public void buildPI(HexWithPlanet hex) {
    super.buildPI(hex);
    getSpecialActions().put(PlayerBoardAction.RL_TO_TP, new SimpleBooleanProperty(false));
  }

  @Override
  public boolean ignoreTpRoundBonus() {
    return ignoreTpRoundBonus;
  }
}
