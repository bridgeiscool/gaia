package gaia.project.game.model;

import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.board.TradingPost;
import javafx.beans.property.SimpleBooleanProperty;

public final class FiraksPlayer extends Player {
  private transient boolean ignoreTpRoundBonus;

  public static FiraksPlayer createNew(PlayerEnum playerEnum) {
    FiraksPlayer player = new FiraksPlayer();
    player.fromRace(Race.FIRAKS, playerEnum);

    return player;
  }

  public static FiraksPlayer empty() {
    return new FiraksPlayer();
  }

  private FiraksPlayer() {}

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
