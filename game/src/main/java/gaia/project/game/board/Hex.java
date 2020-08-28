package gaia.project.game.board;

import static gaia.project.game.board.BoardUtils.HEX_SIZE;
import static gaia.project.game.board.BoardUtils.ROOT_3;
import static gaia.project.game.board.BoardUtils.TWO_ROOT_3;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import gaia.project.game.model.Coords;
import gaia.project.game.model.PlayerEnum;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;

public abstract class Hex extends StackPane {
  private final Coords coords;
  private final int sectorId;
  private final HexPolygon polygon;

  protected Hex(Coords coords, int sectorId) {
    this.coords = coords;
    this.sectorId = sectorId;
    setLayoutX(coords.getCenterX());
    setLayoutY(coords.getCenterY());
    this.setPrefSize(4.0 * HEX_SIZE, 2 * ROOT_3 * HEX_SIZE);
    this.setMinSize(4.0 * HEX_SIZE, 2 * ROOT_3 * HEX_SIZE);
    this.polygon = new HexPolygon(
        // TOP LEFT
        coords.getCenterX() - HEX_SIZE,
        coords.getCenterY() - HEX_SIZE * ROOT_3,
        // TOP RIGHT
        coords.getCenterX() + HEX_SIZE,
        coords.getCenterY() - HEX_SIZE * ROOT_3,
        // RIGHT
        coords.getCenterX() + HEX_SIZE * 2,
        coords.getCenterY(),
        // BOTTOM_RIGHT
        coords.getCenterX() + HEX_SIZE,
        coords.getCenterY() + HEX_SIZE * ROOT_3,
        // BOTTOM_LEFT
        coords.getCenterX() - HEX_SIZE,
        coords.getCenterY() + HEX_SIZE * ROOT_3,
        // LEFT
        coords.getCenterX() - HEX_SIZE * 2,
        coords.getCenterY());
    this.getChildren().add(polygon);
  }

  public abstract boolean isEmpty();

  public abstract boolean hasBuilding();

  public abstract Optional<PlayerEnum> getBuilder();

  @Nullable
  public abstract Planet getPlanet();

  public int getSectorId() {
    return sectorId;
  }

  public Coords getCoords() {
    return coords;
  }

  public Polygon getPolygon() {
    return polygon;
  }

  public Collection<Hex> getHexesWithinRange(List<Hex> hexes, int i) {
    return hexes.stream()
        .filter(h -> distanceTo(h.getCoords()) < TWO_ROOT_3 * HEX_SIZE * i + 1.0)
        .filter(h -> !h.equals(this))
        .collect(Collectors.toList());
  }

  private double distanceTo(Coords other) {
    return coords.distanceTo(other);
  }

  public boolean isWithinRangeOf(Coords coords, int range) {
    return distanceTo(coords) < TWO_ROOT_3 * HEX_SIZE * range + 1.0;
  }

  public static boolean isContiguous(Set<Coords> maybeContiguous) {
    for (Coords coords : maybeContiguous) {
      for (Coords maybeAdjacent : maybeContiguous) {
        if (coords.distanceTo(maybeAdjacent) < TWO_ROOT_3 * HEX_SIZE + 1.0) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean sharesPlanetType(Hex other) {
    return getPlanet() != null
        && other.getPlanet() != null
        && getPlanet().getPlanetType() == other.getPlanet().getPlanetType();
  }

  public void highlightCyan() {
    ObservableList<String> styleClass = getPolygon().getStyleClass();
    styleClass.clear();
    styleClass.add("highlightedCyanHex");
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
    return coords.equals(hex.getCoords()) && sectorId == hex.sectorId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(coords, sectorId);
  }

  static class HexPolygon extends Polygon {
    HexPolygon(double... points) {
      super(points);
      this.getStyleClass().add("hexStyle");
    }
  }
}
