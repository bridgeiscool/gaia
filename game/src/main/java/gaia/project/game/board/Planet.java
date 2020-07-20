package gaia.project.game.board;

import gaia.project.game.PlanetType;
import javafx.scene.shape.Circle;

public class Planet extends Circle {
  private final PlanetType planetType;

  public Planet(double centerX, double centerY, PlanetType planetType) {
    super(centerX, centerY, BoardUtils.PLANET_RADIUS);
    this.planetType = planetType;
    setFill(planetType.getRenderAs());
  }

  public PlanetType getPlanetType() {
    return planetType;
  }
}
