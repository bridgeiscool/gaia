package gaia.project.game;

import javafx.scene.paint.Color;

public enum PlanetType {
  TRANSDIM(Color.PURPLE),
  GAIA(Color.GREEN),
  BLUE(Color.BLUE),
  WHITE(Color.WHITE),
  YELLOW(Color.YELLOW),
  ORANGE(Color.ORANGE),
  RED(Color.RED),
  GRAY(Color.GRAY),
  BROWN(Color.BROWN);

  private Color renderAs;

  PlanetType(Color renderAs) {
    this.renderAs = renderAs;
  }

  public Color getRenderAs() {
    return renderAs;
  }
}
