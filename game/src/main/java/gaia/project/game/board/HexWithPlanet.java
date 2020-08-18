package gaia.project.game.board;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import gaia.project.game.model.Coords;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class HexWithPlanet extends Hex {
  private final Planet planet;
  @Nullable
  private Building building;
  @Nullable
  private PlayerEnum builder;
  private boolean hasGaiaformer;

  public HexWithPlanet(Coords coords, int sectorId, Planet planet) {
    super(coords, sectorId);
    Objects.requireNonNull(planet);
    this.planet = planet;
    this.getChildren().add(planet);
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  public Planet getPlanet() {
    return planet;
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

  public boolean hasGaiaformer() {
    return hasGaiaformer;
  }

  public static Stream<HexWithPlanet> fromHexes(Stream<Hex> hexes) {
    return hexes.filter(HexWithPlanet.class::isInstance).map(HexWithPlanet.class::cast);
  }

  // Action methods
  public void highlight(
      Player activePlayer,
      BiConsumer<HexWithPlanet, Player> toExecute,
      Consumer<HexWithPlanet> callBack) {
    ObservableList<String> styleClass = getPolygon().getStyleClass();
    styleClass.clear();
    styleClass.add("highlightedHex");
    this.setOnMouseClicked(me -> {
      toExecute.accept(this, activePlayer);
      callBack.accept(this);
    });
  }

  public void clearHighlighting() {
    ObservableList<String> styleClass = getPolygon().getStyleClass();
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

  public void addGaiaformer(Gaiaformer gaiaformer) {
    getChildren().add(gaiaformer);
    hasGaiaformer = true;
    builder = gaiaformer.getPlayer();
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
    return getHexesWithinRange(gameBoard.hexes(), 2).stream()
        .filter(h -> !h.isEmpty())
        .map(HexWithPlanet.class::cast)
        .anyMatch(h -> h.builder != null && h.builder != builder);
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

  public void highlightGreen() {
    ObservableList<String> styleClass = getPolygon().getStyleClass();
    styleClass.clear();
    styleClass.add("highlightedGreenHex");
  }

  @Override
  public String toString() {
    return planet.getPlanetType().toString()
        + ": "
        + "("
        + getCoords().getCenterX()
        + ", "
        + getCoords().getCenterY()
        + "), Sector "
        + getSectorId();
  }
}
