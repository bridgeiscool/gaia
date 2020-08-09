package gaia.project.game.model;

import java.io.Serializable;
import java.util.Objects;

public class Coords implements Serializable {
  private static final long serialVersionUID = 8966739407025893484L;

  private final double centerX;
  private final double centerY;

  public Coords(double centerX, double centerY) {
    this.centerX = centerX;
    this.centerY = centerY;
  }

  public double getCenterX() {
    return centerX;
  }

  public double getCenterY() {
    return centerY;
  }

  @Override
  public boolean equals(Object obj) {
    // self check
    if (this == obj)
      return true;
    // null check
    if (obj == null)
      return false;
    // type check and cast
    if (getClass() != obj.getClass())
      return false;
    Coords coords = (Coords) obj;
    // field comparison
    return centerX == coords.centerX && centerY == coords.centerY;
  }

  @Override
  public int hashCode() {
    return Objects.hash(centerX, centerY);
  }
}
