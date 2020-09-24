package gaia.project.game.board;

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
  private final String hexId;

  protected Hex(Coords coords, int sectorId, String hexId) {
    this.coords = coords;
    this.sectorId = sectorId;
    this.hexId = hexId;
    setLayoutX(coords.getCenterX());
    setLayoutY(coords.getCenterY());
    this.setPrefSize(4.0 * BoardUtils.hexSize(), 2 * ROOT_3 * BoardUtils.hexSize());
    this.setMinSize(4.0 * BoardUtils.hexSize(), 2 * ROOT_3 * BoardUtils.hexSize());
    this.polygon = new HexPolygon(
        // TOP LEFT
        coords.getCenterX() - BoardUtils.hexSize(),
        coords.getCenterY() - BoardUtils.hexSize() * ROOT_3,
        // TOP RIGHT
        coords.getCenterX() + BoardUtils.hexSize(),
        coords.getCenterY() - BoardUtils.hexSize() * ROOT_3,
        // RIGHT
        coords.getCenterX() + BoardUtils.hexSize() * 2,
        coords.getCenterY(),
        // BOTTOM_RIGHT
        coords.getCenterX() + BoardUtils.hexSize(),
        coords.getCenterY() + BoardUtils.hexSize() * ROOT_3,
        // BOTTOM_LEFT
        coords.getCenterX() - BoardUtils.hexSize(),
        coords.getCenterY() + BoardUtils.hexSize() * ROOT_3,
        // LEFT
        coords.getCenterX() - BoardUtils.hexSize() * 2,
        coords.getCenterY());
    getChildren().add(polygon);
  }

  public String getHexId() {
    return hexId;
  }

  public abstract boolean isEmpty();

  /**
   * This means it has a building or a the mine on the lost planet. Does not count space stations.
   * 
   * @return
   */
  public abstract boolean hasBuilding();

  public abstract Optional<PlayerEnum> getBuilder();

  public abstract boolean hasSpaceStation();

  @Nullable
  public abstract Mine getLeechMine();

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

  public Collection<Hex> getAllHexesWithinRange(List<Hex> hexes, int i) {
    return getHexesWithinRange(hexes, i, true);
  }

  public Collection<Hex> getOtherHexesWithinRange(List<Hex> hexes, int i) {
    return getHexesWithinRange(hexes, i, false);
  }

  private Collection<Hex> getHexesWithinRange(List<Hex> hexes, int i, boolean includeThis) {
    return hexes.stream()
        .filter(h -> distanceTo(h.getCoords()) < TWO_ROOT_3 * BoardUtils.hexSize() * i + 1.0)
        .filter(h -> includeThis || !h.equals(this))
        .collect(Collectors.toList());
  }

  private double distanceTo(Coords other) {
    return coords.distanceTo(other);
  }

  public boolean isWithinRangeOf(Coords coords, int range) {
    return distanceTo(coords) < TWO_ROOT_3 * BoardUtils.hexSize() * range + 1.0;
  }

  public boolean isWithinRangeOf(Hex hex, int range) {
    return isWithinRangeOf(hex.getCoords(), range);
  }

  public static boolean isContiguous(Set<Coords> maybeContiguous) {
    for (Coords coords : maybeContiguous) {
      for (Coords maybeAdjacent : maybeContiguous) {
        if (coords.distanceTo(maybeAdjacent) < TWO_ROOT_3 * BoardUtils.hexSize() + 1.0) {
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
    return hex.hexId.equals(this.hexId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hexId);
  }

  static class HexPolygon extends Polygon {
    HexPolygon(double... points) {
      super(points);
      this.getStyleClass().add("hexStyle");
    }
  }
}
