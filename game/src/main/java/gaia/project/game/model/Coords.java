package gaia.project.game.model;

import static gaia.project.game.board.BoardUtils.HEX_SIZE;
import static gaia.project.game.board.BoardUtils.TWO_ROOT_3;

import java.io.Serializable;
import java.util.Objects;

import com.google.common.math.DoubleMath;

public class Coords implements Serializable {
  private static final long serialVersionUID = 8966739407025893484L;
  private static final double THRESHOLD = .000001;

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

  public double distanceTo(Coords other) {
    return Math.sqrt(Math.pow(centerX - other.centerX, 2) + Math.pow(centerY - other.centerY, 2));
  }

  public boolean isWithinRangeOf(Coords coords, int range) {
    return distanceTo(coords) < TWO_ROOT_3 * HEX_SIZE * range + 1.0;
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
    return DoubleMath.fuzzyEquals(centerX, coords.centerX, THRESHOLD)
        && DoubleMath.fuzzyEquals(centerY, coords.centerY, THRESHOLD);
  }

  @Override
  public int hashCode() {
    return Objects.hash(centerX, centerY);
  }
}
