package gaia.project.game.model;

import com.google.common.base.Preconditions;

import gaia.project.game.PlanetType;
import gaia.project.game.board.HexWithPlanet;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

public class GleensPlayer extends Player {
  private static final long serialVersionUID = -5023045450134206874L;

  public GleensPlayer(PlayerEnum playerEnum) {
    super(Race.GLEENS, playerEnum);
  }

  @Override
  protected void addAdditionalListeners() {
    super.addAdditionalListeners();
    getGaiaPlanets().addListener((o, oldValue, newValue) -> Util.plus(getScore(), 2));
    getQic().addListener((o, oldValue, newValue) -> {
      if (getQa().isEmpty()) {
        // If the QA hasn't been built, change the QIC into an ore
        Platform.runLater(() -> {
          Util.minus(getQic(), newValue.intValue());
          Util.plus(getOre(), newValue.intValue());
        });
      }
    });
  }

  public boolean canDigTo(HexWithPlanet hex) {
    Preconditions.checkArgument(!hex.isEmpty());
    PlanetType planetType = hex.getPlanet().getPlanetType();
    if (planetType == PlanetType.TRANSDIM) {
      return false;
    }

    if (planetType == PlanetType.GAIA) {
      return getOre().get() > 1;
    }

    return super.canDigTo(hex);
  }

  @Override
  public void buildPI(HexWithPlanet hex) {
    super.buildPI(hex);
    FederationTile.GLEENS.updatePlayer(this);
    getFederationTiles().put(FederationTile.GLEENS, new SimpleBooleanProperty(true));
  }
}
