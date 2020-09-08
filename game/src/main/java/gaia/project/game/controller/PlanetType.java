package gaia.project.game.controller;

import javafx.scene.paint.Color;

public enum PlanetType {
  TRANSDIM(Color.PURPLE) {
    @Override
    public int numDigsTo(PlanetType other) {
      throw new IllegalStateException("Not in planet cycle");
    }
  },
  GAIA(Color.GREEN) {
    @Override
    public int numDigsTo(PlanetType other) {
      throw new IllegalStateException("Not in planet cycle");
    }
  },
  BLUE(Color.BLUE) {
    @Override
    public int numDigsTo(PlanetType other) {
      switch (other) {
        case BLUE:
          return 0;
        case WHITE:
        case RED:
          return 1;
        case GRAY:
        case ORANGE:
          return 2;
        case YELLOW:
        case BROWN:
          return 3;
        default:
          throw new IllegalStateException("Not in planet cycle");
      }
    }
  },
  WHITE(Color.WHITE) {
    @Override
    public int numDigsTo(PlanetType other) {
      switch (other) {
        case WHITE:
          return 0;
        case BLUE:
        case GRAY:
          return 1;
        case RED:
        case BROWN:
          return 2;
        case ORANGE:
        case YELLOW:
          return 3;
        default:
          throw new IllegalStateException("Not in planet cycle");
      }
    }
  },
  YELLOW(Color.YELLOW) {
    @Override
    public int numDigsTo(PlanetType other) {
      switch (other) {
        case YELLOW:
          return 0;
        case ORANGE:
        case BROWN:
          return 1;
        case RED:
        case GRAY:
          return 2;
        case WHITE:
        case BLUE:
          return 3;
        default:
          throw new IllegalStateException("Not in planet cycle");
      }
    }
  },
  ORANGE(Color.ORANGE) {
    @Override
    public int numDigsTo(PlanetType other) {
      switch (other) {
        case ORANGE:
          return 0;
        case YELLOW:
        case RED:
          return 1;
        case BROWN:
        case BLUE:
          return 2;
        case WHITE:
        case GRAY:
          return 3;
        default:
          throw new IllegalStateException("Not in planet cycle");
      }
    }
  },
  RED(Color.RED) {
    @Override
    public int numDigsTo(PlanetType other) {
      switch (other) {
        case RED:
          return 0;
        case BLUE:
        case ORANGE:
          return 1;
        case WHITE:
        case YELLOW:
          return 2;
        case GRAY:
        case BROWN:
          return 3;
        default:
          throw new IllegalStateException("Not in planet cycle");
      }
    }
  },
  GRAY(Color.GRAY) {
    @Override
    public int numDigsTo(PlanetType other) {
      switch (other) {
        case GRAY:
          return 0;
        case WHITE:
        case BROWN:
          return 1;
        case BLUE:
        case YELLOW:
          return 2;
        case ORANGE:
        case RED:
          return 3;
        default:
          throw new IllegalStateException("Not in planet cycle");
      }
    }
  },
  BROWN(Color.TAN) {
    @Override
    public int numDigsTo(PlanetType other) {
      switch (other) {
        case BROWN:
          return 0;
        case YELLOW:
        case GRAY:
          return 1;
        case WHITE:
        case ORANGE:
          return 2;
        case RED:
        case BLUE:
          return 3;
        default:
          throw new IllegalStateException("Not in planet cycle");
      }
    }
  },
  LOST(Color.PINK) {
    @Override
    public int numDigsTo(PlanetType other) {
      throw new IllegalStateException("Not in planet cycle");
    }
  },
  NONE(null) {
    @Override
    public int numDigsTo(PlanetType other) {
      throw new IllegalStateException("Not in planet cycle");
    }
  };

  private Color renderAs;

  PlanetType(Color renderAs) {
    this.renderAs = renderAs;
  }

  public Color getRenderAs() {
    return renderAs;
  }

  public abstract int numDigsTo(PlanetType other);
}
