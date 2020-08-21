package gaia.project.game.model;

import gaia.project.game.PlanetType;
import javafx.collections.SetChangeListener;

public class GeodensPlayer extends Player {
  private static final long serialVersionUID = -5023045450134206874L;

  public GeodensPlayer(PlayerEnum playerEnum) {
    super(Race.GEODENS, playerEnum);
  }

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
