package gaia.project.game;

import javafx.scene.shape.Circle;

public class Planet extends Circle {
  private final PlanetType planetType;
  
  public Planet(double centerX, double centerY, PlanetType planetType) {
    super(centerX, centerY, GuiUtils.PLANET_RADIUS);
    this.planetType = planetType;
    setFill(planetType.getRenderAs());
  }
}
