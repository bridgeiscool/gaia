package gaia.project.game.board;

import static gaia.project.game.board.BoardUtils.HEX_SIZE;
import static gaia.project.game.board.BoardUtils.ROOT_3;
import static gaia.project.game.board.BoardUtils.TWO_ROOT_3;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import gaia.project.game.PlanetType;
import gaia.project.game.model.Coords;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;

public final class Hex extends StackPane {
  private final Coords coords;
  private final int sectorId;
  private final HexPolygon polygon;
  @Nullable
  private final Planet planet;
  @Nullable
  private Building building;
  @Nullable
  private PlayerEnum builder;
  private boolean hasGaiaformer;

  public static Hex emptyHex(Coords coords, int sectorId) {
    return new Hex(coords, sectorId, null);
  }

  public static Hex withPlanet(Coords coords, int sectorId, PlanetType planetType) {
    return new Hex(coords, sectorId, new Planet(coords.getCenterX(), coords.getCenterY(), planetType));
  }

  // Planet is @Nullable
  private Hex(Coords coords, int sectorId, Planet planet) {
    this.coords = coords;
    this.sectorId = sectorId;
    this.planet = planet;
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

    if (planet != null) {
      this.getChildren().add(planet);
    }

  }

  public int getSectorId() {
    return sectorId;
  }

  public Coords getCoords() {
    return coords;
  }

  public Optional<Planet> getPlanet() {
    return Optional.ofNullable(planet);
  }

  public boolean hasBuilding() {
    return building != null;
  }

  public Optional<PlayerEnum> getBuilder() {
    return Optional.ofNullable(builder);
  }

  public int getPower() {
    return building == null ? 0 : building.getPower();
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

  public void highlight(Player activePlayer, BiConsumer<Hex, Player> toExecute, Consumer<Hex> callBack) {
    ObservableList<String> styleClass = polygon.getStyleClass();
    styleClass.clear();
    styleClass.add("highlightedHex");
    this.setOnMouseClicked(me -> {
      toExecute.accept(this, activePlayer);
      callBack.accept(this);
    });
  }

  public void clearHighlighting() {
    ObservableList<String> styleClass = polygon.getStyleClass();
    styleClass.clear();
    styleClass.add("hexStyle");
    setOnMouseClicked(null);
  }

  public void addMine(Mine mine) {
    // Remove a gaiaformer if it's there
    if (hasGaiaformer) {
      getChildren().remove(getChildren().size() - 1);
    }
    getChildren().add(mine);
    building = Building.MINE;
    builder = mine.getPlayer();
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

  @Override
  public String toString() {
    return (planet == null ? "Empty: " : planet.getPlanetType().toString() + ": ")
        + "("
        + coords.getCenterX()
        + ", "
        + coords.getCenterY()
        + "), Sector "
        + sectorId;
  }

  static class HexPolygon extends Polygon {
    HexPolygon(double... points) {
      super(points);
      this.getStyleClass().add("hexStyle");
    }
  }
}
