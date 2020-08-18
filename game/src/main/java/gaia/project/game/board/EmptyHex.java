package gaia.project.game.board;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import gaia.project.game.model.Coords;
import gaia.project.game.model.Player;
import gaia.project.game.model.PlayerEnum;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class EmptyHex extends Hex {
  private Set<PlayerEnum> satellites = new HashSet<>();
  private VBox satelliteBox;

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

  @Override
  public Planet getPlanet() {
    return null;
  }

  public boolean hasSatellite(PlayerEnum player) {
    return satellites.contains(player);
  }

  public static Predicate<EmptyHex> possibleSatellite(Player player) {
    return h -> Stream
        .of(
            player.getMines(),
            player.getTradingPosts(),
            player.getResearchLabs(),
            player.getPi(),
            player.getQa(),
            player.getKa())
        .flatMap(Set::stream)
        .anyMatch(c -> h.isWithinRangeOf(c, 1))
        && !h.hasSatellite(player.getPlayerEnum());

  }

  public void highlight(Player activePlayer, BiConsumer<EmptyHex, Player> toExecute, Consumer<EmptyHex> callBack) {
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

  public void addSatellite(Player activePlayer) {
    activePlayer.addSatellite(this);
    satellites.add(activePlayer.getPlayerEnum());
  }

  public void addSatelliteUI(Satellite satellite) {
    satelliteBox.getChildren().add(satellite);
  }
}
