package gaia.project.game.model;

import com.google.common.base.Preconditions;

import gaia.project.game.board.HexWithPlanet;
import gaia.project.game.controller.PlanetType;
import javafx.application.Platform;

public final class GleensPlayer extends Player {
  public static GleensPlayer createNew(PlayerEnum playerEnum) {
    GleensPlayer p = new GleensPlayer();
    p.fromRace(Race.GLEENS, playerEnum);
    return p;
  }

  public static GleensPlayer empty() {
    return new GleensPlayer();
  }

  private GleensPlayer() {}

  @Override
  protected void addAdditionalListeners() {
    super.addAdditionalListeners();
    getGaiaPlanets().addListener((o, oldValue, newValue) -> updateScore(2, "Race ability"));
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
    getFederationTiles().add(new FedToken(FederationTile.GLEENS, true));
  }
}
