package gaia.project.game.board;

import static gaia.project.game.board.BoardUtils.HEX_SIZE;
import static gaia.project.game.board.BoardUtils.ROOT_3;
import static gaia.project.game.board.BoardUtils.TWO_ROOT_3;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import gaia.project.game.PlanetType;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;

public final class Hex extends StackPane {
  private final double centerX;
  private final double centerY;
  private final int sectorId;

  @Nullable
  private final Planet planet;

  public static Hex emptyHex(double centerX, double centerY, int sectorId) {
    return new Hex(centerX, centerY, sectorId, null);
  }

  public static Hex withPlanet(double centerX, double centerY, int sectorId, PlanetType planetType) {
    return new Hex(centerX, centerY, sectorId, new Planet(centerX, centerY, planetType));
  }

  // Planet is @Nullable
  private Hex(double centerX, double centerY, int sectorId, Planet planet) {
    this.centerX = centerX;
    this.centerY = centerY;
    this.sectorId = sectorId;
    this.planet = planet;
    setLayoutX(centerX);
    setLayoutY(centerY);
    this.setPrefSize(4.0 * HEX_SIZE, 2 * ROOT_3 * HEX_SIZE);
    this.setMinSize(4.0 * HEX_SIZE, 2 * ROOT_3 * HEX_SIZE);
    this.getChildren()
        .add(
            new HexPolygon(
                // TOP LEFT
                centerX - HEX_SIZE,
                centerY - HEX_SIZE * ROOT_3,
                // TOP RIGHT
                centerX + HEX_SIZE,
                centerY - HEX_SIZE * ROOT_3,
                // RIGHT
                centerX + HEX_SIZE * 2,
                centerY,
                // BOTTOM_RIGHT
                centerX + HEX_SIZE,
                centerY + HEX_SIZE * ROOT_3,
                // BOTTOM_LEFT
                centerX - HEX_SIZE,
                centerY + HEX_SIZE * ROOT_3,
                // LEFT
                centerX - HEX_SIZE * 2,
                centerY));

    if (planet != null) {
      this.getChildren().add(planet);
    }

  }

  public Optional<Planet> getPlanet() {
    return Optional.ofNullable(planet);
  }

  static class HexPolygon extends Polygon {
    HexPolygon(double... points) {
      super(points);
      this.getStyleClass().add("hexStyle");
    }
  }

  public Collection<Hex> getHexesWithinRange(List<Hex> hexes, int i) {
    return hexes.stream()
        .filter(h -> distanceTo(h) < TWO_ROOT_3 * HEX_SIZE * i + 1.0)
        .filter(h -> !h.equals(this))
        .collect(Collectors.toList());
  }

  private double distanceTo(Hex other) {
    return Math.sqrt(Math.pow(centerX - other.centerX, 2) + Math.pow(centerY - other.centerY, 2));
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
    Hex hex = (Hex) obj;
    // field comparison
    return centerX == hex.centerX && centerY == hex.centerY && sectorId == hex.sectorId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(centerX, centerY);
  }

  @Override
  public String toString() {
    return (planet == null ? "Empty: " : planet.getPlanetType().toString() + ": ")
        + "("
        + centerX
        + ", "
        + centerY
        + "), Sector "
        + sectorId;
  }
}
