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
import gaia.project.game.model.Race;
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
  @Nullable
  private Node buildingUI;
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

  public boolean hasSpaceStation() {
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

  @Override
  public Optional<PlayerEnum> getBuilder() {
    return Optional.ofNullable(builder);
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
      getChildren().remove(buildingUI);
      hasGaiaformer = false;
    } else {
      builder = mine.getPlayer();
    }
    getChildren().add(mine);
    building = Building.MINE;
    buildingUI = mine;
  }

  // For reloading game
  public void addTradingPost(TradingPost tradingPost) {
    getChildren().add(tradingPost);
    building = Building.TRADING_POST;
    builder = tradingPost.getPlayer();
    buildingUI = tradingPost;
  }

  public void addResearchLab(ResearchLab researchLab) {
    getChildren().add(researchLab);
    building = Building.RESEARCH_LAB;
    builder = researchLab.getPlayer();
    buildingUI = researchLab;
  }

  public void addPi(PlanetaryInstitute pi) {
    getChildren().add(pi);
    building = Building.PLANETARY_INSTITUTE;
    builder = pi.getPlayer();
    buildingUI = pi;
  }

  public void addAcademy(Academy academy) {
    getChildren().add(academy);
    building = Building.ACADEMY;
    builder = academy.getPlayer();
    buildingUI = academy;
  }

  public void addGaiaformer(Gaiaformer gaiaformer) {
    getChildren().add(gaiaformer);
    hasGaiaformer = true;
    builder = gaiaformer.getPlayer();
    buildingUI = gaiaformer;
  }

  public boolean canUpgrade(Player activePlayer, GameBoard gameBoard) {
    if (building == null || builder != activePlayer.getPlayerEnum()) {
      return false;
    }

    if (activePlayer.getRace() == Race.BESCODS) {
      return canUpgradeBescods(activePlayer, gameBoard);
    }

    switch (building) {
      case PLANETARY_INSTITUTE:
      case ACADEMY:
        return false;
      case MINE:
        return activePlayer.getTradingPosts().size() < 4
            && activePlayer.getOre().get() >= 2
            && activePlayer.getCredits().get() >= (hasNeighbor(gameBoard) ? 3 : 6);
      case TRADING_POST:
        return (activePlayer.getResearchLabs().size() < 3
            && activePlayer.getOre().get() >= 3
            && activePlayer.getCredits().get() >= 5)
            || (activePlayer.getPi().isEmpty()
                && activePlayer.getOre().get() >= 4
                && activePlayer.getCredits().get() >= 6);
      case RESEARCH_LAB:
        return (activePlayer.getKa().isEmpty() || activePlayer.getQa().isEmpty())
            && activePlayer.getOre().get() >= 6
            && activePlayer.getCredits().get() >= 6;
      default:
        throw new IllegalStateException("whaaaa");
    }
  }

  public boolean canUpgradeBescods(Player activePlayer, GameBoard gameBoard) {
    switch (building) {
      case PLANETARY_INSTITUTE:
      case ACADEMY:
        return false;
      case MINE:
        return activePlayer.getTradingPosts().size() < 4
            && activePlayer.getOre().get() >= 2
            && activePlayer.getCredits().get() >= (hasNeighbor(gameBoard) ? 3 : 6);
      case TRADING_POST:
        return (activePlayer.getResearchLabs().size() < 3
            && activePlayer.getOre().get() >= 3
            && activePlayer.getCredits().get() >= 5)
            || ((activePlayer.getQa().isEmpty() || activePlayer.getKa().isEmpty())
                && activePlayer.getOre().get() >= 6
                && activePlayer.getCredits().get() >= 6);
      case RESEARCH_LAB:
        return activePlayer.getPi().isEmpty()
            && activePlayer.getOre().get() >= 4
            && activePlayer.getCredits().get() >= 6;
      default:
        throw new IllegalStateException("whaaaa");
    }
  }

  private boolean hasNeighbor(GameBoard gameBoard) {
    return getHexesWithinRange(gameBoard.hexes(), 2).stream()
        .anyMatch(h -> h.getBuilder().isPresent() && h.getBuilder().get() != builder);
  }

  public void upgradeBuilding(Player player, GameBoard gameBoard) {
    Preconditions.checkArgument(building != null);

    if (player.getRace() == Race.BESCODS) {
      upgradeBuildingBescods(player, gameBoard);
    } else {
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
  }

  public void upgradeBuildingBescods(Player player, GameBoard gameBoard) {
    switch (building) {
      case MINE:
        player.buildTradingPost(this, hasNeighbor(gameBoard));
        building = Building.TRADING_POST;
        break;
      case TRADING_POST:
        building = getTpUpgradeBescods(player);
        if (building == Building.RESEARCH_LAB) {
          player.buildResearchLab(this);
        } else {
          player.buildAcademy(this, knowledgeAcademy(player));
          // PI
        }
        break;
      case RESEARCH_LAB:
        building = Building.PLANETARY_INSTITUTE;
        player.buildPI(this);
        break;
      default:
        throw new IllegalStateException("Can't upgrade building: " + building);
    }
  }

  private Building getTpUpgradeTo(Player player) {
    if (player.getRace() == Race.BESCODS) {
      return getTpUpgradeBescods(player);
    }

    Preconditions.checkArgument(player.getResearchLabs().size() < 3 || player.getPi().isEmpty());
    if (player.getResearchLabs().size() == 3) {
      return Building.PLANETARY_INSTITUTE;
    }

    if (!player.getPi().isEmpty() || player.getOre().get() < 4 || player.getCredits().get() < 6) {
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

  private Building getTpUpgradeBescods(Player player) {
    Preconditions
        .checkArgument(player.getResearchLabs().size() < 3 || player.getQa().isEmpty() || player.getKa().isEmpty());
    if (player.getResearchLabs().size() == 3) {
      return Building.ACADEMY;
    }

    if ((!player.getKa().isEmpty() && !player.getQa().isEmpty())
        || player.getOre().get() < 6
        || player.getCredits().get() < 6) {
      return Building.RESEARCH_LAB;
    }

    Optional<ButtonType> response;
    ButtonType rl = new ButtonType("Research Lab", ButtonData.LEFT);
    ButtonType academy = new ButtonType("Academy", ButtonData.RIGHT);
    do {
      response = new Alert(AlertType.CONFIRMATION, "Research lab or PI?", rl, academy).showAndWait();
    } while (response.isEmpty());

    return response.get().getButtonData().equals(ButtonData.LEFT) ? Building.RESEARCH_LAB : Building.ACADEMY;
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

  @Nullable
  public Node getBuildingNode() {
    return buildingUI;
  }

  public void switchBuildingUI(Node newBuilding) {
    getChildren().remove(buildingUI);
    getChildren().add(newBuilding);
    buildingUI = newBuilding;
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
