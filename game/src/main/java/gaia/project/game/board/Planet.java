package gaia.project.game.board;

import com.google.common.base.Preconditions;

import gaia.project.game.controller.PlanetType;
import javafx.scene.shape.Circle;

public class Planet extends Circle {
  private PlanetType planetType;

  public Planet(double centerX, double centerY, PlanetType planetType) {
    super(centerX, centerY, BoardUtils.planetRadius());
    this.planetType = planetType;
    setFill(planetType.getRenderAs());
  }

  public PlanetType getPlanetType() {
    return planetType;
  }

  public void transdimToGaia() {
    Preconditions.checkArgument(planetType == PlanetType.TRANSDIM);
    planetType = PlanetType.GAIA;
    setFill(planetType.getRenderAs());
  }
}
