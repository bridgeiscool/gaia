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

import com.google.common.base.Preconditions;

import gaia.project.game.PlanetType;
import gaia.project.game.model.Coords;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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

  public boolean checkTechTile() {
    return building != null && (building == Building.ACADEMY || building == Building.RESEARCH_LAB);
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

  // For reloading game
  public void addTradingPost(TradingPost tradingPost) {
    getChildren().add(tradingPost);
    building = Building.TRADING_POST;
    builder = tradingPost.getPlayer();
  }

  public void addResearchLab(ResearchLab researchLab) {
    getChildren().add(researchLab);
    building = Building.RESEARCH_LAB;
    builder = researchLab.getPlayer();
  }

  public void addPi(PlanetaryInstitute pi) {
    getChildren().add(pi);
    building = Building.PLANETARY_INSTITUTE;
    builder = pi.getPlayer();
  }

  public void addAcademy(Academy academy) {
    getChildren().add(academy);
    building = Building.ACADEMY;
    builder = academy.getPlayer();
  }

  public boolean canUpgrade(Player activePlayer, GameBoard gameBoard) {
    if (building == null || builder != activePlayer.getPlayerEnum()) {
      return false;
    }

    switch (building) {
      case PLANETARY_INSTITUTE:
      case ACADEMY:
        return false;
      case MINE:
        return activePlayer.getTradingPosts().size() < 4
            && activePlayer.getOre().intValue() >= 2
            && activePlayer.getCredits().intValue() >= (hasNeighbor(gameBoard) ? 3 : 6);
      case TRADING_POST:
        return (activePlayer.getResearchLabs().size() < 3
            && activePlayer.getOre().intValue() >= 3
            && activePlayer.getCredits().intValue() >= 5)
            || (activePlayer.getPi().isEmpty()
                && activePlayer.getOre().intValue() >= 4
                && activePlayer.getCredits().intValue() >= 6);
      case RESEARCH_LAB:
        return (activePlayer.getKa().isEmpty() || activePlayer.getQa().isEmpty())
            && activePlayer.getOre().intValue() >= 6
            && activePlayer.getCredits().intValue() >= 6;
      default:
        throw new IllegalStateException("whaaaa");
    }
  }

  private boolean hasNeighbor(GameBoard gameBoard) {
    return getHexesWithinRange(gameBoard.hexes(), 2).stream().anyMatch(h -> h.builder != null && h.builder != builder);
  }

  public void upgradeBuilding(Player player, GameBoard gameBoard) {
    Preconditions.checkArgument(building != null);
    switch (building) {
      case MINE:
        player.buildTradingPost(this, hasNeighbor(gameBoard));
        building = Building.TRADING_POST;
        break;
      case TRADING_POST:
        building = getTpUpgradeTo(player);
        if (building == Building.RESEARCH_LAB) {
          player.buildResearchLab(this);
        } else {
          // PI
          player.buildPI(this);
        }
        break;
      case RESEARCH_LAB:
        building = Building.ACADEMY;
        player.buildAcademy(this, knowledgeAcademy(player));
        break;
      default:
        throw new IllegalStateException("Can't upgrade building: " + building);
    }
  }

  private Building getTpUpgradeTo(Player player) {
    Preconditions.checkArgument(player.getResearchLabs().size() < 3 || player.getPi().isEmpty());
    if (player.getResearchLabs().size() == 3) {
      return Building.PLANETARY_INSTITUTE;
    }

    if (!player.getPi().isEmpty()) {
      return Building.RESEARCH_LAB;
    }

    Optional<ButtonType> response;
    ButtonType rl = new ButtonType("Research Lab", ButtonData.LEFT);
    ButtonType pi = new ButtonType("PI", ButtonData.RIGHT);
    do {
      response = new Alert(AlertType.CONFIRMATION, "Research lab or PI?", rl, pi).showAndWait();
    } while (response.isEmpty());

    return response.get().getButtonData().equals(ButtonData.LEFT)
        ? Building.RESEARCH_LAB
        : Building.PLANETARY_INSTITUTE;
  }

  private boolean knowledgeAcademy(Player player) {
    Preconditions.checkArgument(player.getKa().isEmpty() || player.getQa().isEmpty());
    if (!player.getKa().isEmpty()) {
      return false;
    }

    if (!player.getQa().isEmpty()) {
      return true;
    }

    Optional<ButtonType> response;
    ButtonType ka = new ButtonType("Knowldge Academy", ButtonData.LEFT);
    ButtonType qa = new ButtonType("QIC Academy", ButtonData.RIGHT);
    do {
      response = new Alert(AlertType.CONFIRMATION, "Which academy?", ka, qa).showAndWait();
    } while (response.isEmpty());

    return response.get().getButtonData().equals(ButtonData.LEFT);
  }

  public void switchBuildingUI(Node newBuilding) {
    getChildren().remove(getChildren().size() - 1);
    getChildren().add(newBuilding);
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
