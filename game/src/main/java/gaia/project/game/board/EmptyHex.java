package gaia.project.game.board;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import gaia.project.game.PlanetType;
import gaia.project.game.model.Coords;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class EmptyHex extends Hex {
  private Set<PlayerEnum> satellites = new HashSet<>();
  private VBox satelliteBox;
  private boolean hasLostPlanet;
  @Nullable
  private PlayerEnum builder;

  public EmptyHex(Coords coords, int sectorId) {
    super(coords, sectorId);
    satelliteBox = new VBox(2.0);
    satelliteBox.setAlignment(Pos.CENTER);
    getChildren().add(satelliteBox);
  }

  @Override
  public boolean isEmpty() {
    return true;
  }

  // Methods that are only necessary for lost planet...
  @Override
  public boolean hasBuilding() {
    return hasLostPlanet;
  }

  @Override
  public Optional<PlayerEnum> getBuilder() {
    return Optional.ofNullable(builder);
  }

  @Override
  public int getPower() {
    return hasLostPlanet ? 1 : 0;
  }

  @Override
  public Planet getPlanet() {
    return null;
  }

  public boolean hasSatellite(PlayerEnum player) {
    return satellites.contains(player);
  }

  public static Predicate<EmptyHex> possibleSatellite(Player player, Set<Coords> currentBuildings) {
    return h -> currentBuildings.stream().anyMatch(c -> h.isWithinRangeOf(c, 1))
        && !h.hasSatellite(player.getPlayerEnum());
  }

  public void highlight(Player activePlayer, BiConsumer<EmptyHex, Player> toExecute, Consumer<EmptyHex> callBack) {
    if (!hasLostPlanet) {
      ObservableList<String> styleClass = getPolygon().getStyleClass();
      styleClass.clear();
      styleClass.add("highlightedHex");
      this.setOnMouseClicked(me -> {
        toExecute.accept(this, activePlayer);
        callBack.accept(this);
      });
    }
  }

  public void clearHighlighting() {
    ObservableList<String> styleClass = getPolygon().getStyleClass();
    styleClass.clear();
    styleClass.add("hexStyle");
    setOnMouseClicked(null);
  }

  public void addSatellite(Player activePlayer) {
    activePlayer.addSatellite(this);
    satellites.add(activePlayer.getPlayerEnum());
  }

  public void addSatelliteUI(Satellite satellite) {
    satelliteBox.getChildren().add(satellite);
  }

  public void addLostPlanet(Player player) {
    this.builder = player.getPlayerEnum();
    hasLostPlanet = true;
    getChildren().remove(satelliteBox);
    getChildren().add(new Planet(getCoords().getCenterX(), getCoords().getCenterY(), PlanetType.LOST));
    getChildren().add(new Mine(this, player.getRace().getColor(), player.getPlayerEnum()));
  }
}
