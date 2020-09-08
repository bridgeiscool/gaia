package gaia.project.game.model;

import gaia.project.game.controller.PlanetType;
import javafx.collections.SetChangeListener;

public final class GeodensPlayer extends Player {
  public static GeodensPlayer createNew(PlayerEnum playerEnum) {
    GeodensPlayer player = new GeodensPlayer();
    player.fromRace(Race.GEODENS, playerEnum);
    return player;
  }

  public static GeodensPlayer empty() {
    return new GeodensPlayer();
  }

  private GeodensPlayer() {}

  @Override
  protected void addAdditionalListeners() {
    super.addAdditionalListeners();
    getBuiltOn().addListener((SetChangeListener<PlanetType>) change -> {
      if (!getPi().isEmpty()) {
        Util.plus(getResearch(), 3);
      }
    });
  }
}
