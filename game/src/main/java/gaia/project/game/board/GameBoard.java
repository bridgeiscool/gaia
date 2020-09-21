package gaia.project.game.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.annotations.VisibleForTesting;

import gaia.project.game.controller.PlanetType;
import gaia.project.game.model.Player;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

public class GameBoard extends Group implements Iterable<Sector> {
  private final LinkedHashMap<SectorLocation, Rot> sectorLocations;
  private final List<Sector> sectors;

  public static GameBoard originalBoard() {
    LinkedHashMap<SectorLocation, Rot> originalBoard = new LinkedHashMap<>();
    originalBoard.put(SectorLocation.LOC1, Rot.POS_1);
    originalBoard.put(SectorLocation.LOC4, Rot.POS_1);
    originalBoard.put(SectorLocation.LOC5, Rot.POS_1);
    originalBoard.put(SectorLocation.LOC8, Rot.POS_1);
    originalBoard.put(SectorLocation.LOC2, Rot.POS_1);
    originalBoard.put(SectorLocation.LOC6, Rot.POS_1);
    originalBoard.put(SectorLocation.LOC9, Rot.POS_1);
    originalBoard.put(SectorLocation.LOC7, Rot.POS_1);
    originalBoard.put(SectorLocation.LOC3, Rot.POS_1);
    originalBoard.put(SectorLocation.LOC0, Rot.POS_1);
    return new GameBoard(originalBoard);
  }

  public static GameBoard random(Random random) {
    List<SectorLocation> unshuffled = Arrays.asList(SectorLocation.values());

    LinkedHashMap<SectorLocation, Rot> asMap = new LinkedHashMap<>(10);
    GameBoard maybeValid;
    do {
      asMap.clear();
      System.out.println("Shuffling...");
      Collections.shuffle(unshuffled, random);
      unshuffled.forEach(sl -> asMap.put(sl, Rot.POS_1));
      maybeValid = new GameBoard(asMap);
    } while (!maybeValid.isValid());

    return maybeValid;
  }

  // This has to be done separately to prevent JavaFX calls during the GameBoard creation subroutine
  public void addCenterLabels() {
    sectors.forEach(Sector::addCenterLabel);
  }

  public List<Hex> hexes() {
    return sectors.stream().flatMap(s -> StreamSupport.stream(s.spliterator(), false)).collect(Collectors.toList());
  }

  public Stream<EmptyHex> emptyHexes() {
    return hexes().stream().filter(Hex::isEmpty).map(EmptyHex.class::cast);
  }

  public Stream<HexWithPlanet> planetaryHexes() {
    return hexes().stream().filter(h -> !h.isEmpty()).map(HexWithPlanet.class::cast);
  }

  @VisibleForTesting
  boolean isValid() {
    List<HexWithPlanet> hexes = planetaryHexes().collect(Collectors.toList());

    for (HexWithPlanet hex : hexes) {
      if (hex.getPlanet().getPlanetType() != PlanetType.TRANSDIM) {
        for (Hex adjacent : hex.getOtherHexesWithinRange(hexes(), 1)) {
          // Exclude adjacent planets that aren't purple
          if (hex.sharesPlanetType(adjacent)) {
            return false;
          }
        }
      }
    }

    return true;
  }

  public GameBoard(LinkedHashMap<SectorLocation, Rot> sectorPlacements) {
    this.sectorLocations = sectorPlacements;
    sectors = new ArrayList<>();

    // Position all sectors on the screen
    initSectors();

    // Add the sectors
    ObservableList<Node> children = getChildren();
    children.addAll(sectors);
  }

  private void initSectors() {
    Iterator<SectorLocation> iterator = sectorLocations.keySet().iterator();
    sectors.add(Sector1.getInstance(this, iterator.next()));
    sectors.add(Sector2.getInstance(this, iterator.next()));
    sectors.add(Sector3.getInstance(this, iterator.next()));
    sectors.add(Sector4.getInstance(this, iterator.next()));
    sectors.add(Sector5.getInstance(this, iterator.next()));
    sectors.add(Sector6.getInstance(this, iterator.next()));
    sectors.add(Sector7.getInstance(this, iterator.next()));
    sectors.add(Sector8.getInstance(this, iterator.next()));
    sectors.add(Sector9.getInstance(this, iterator.next()));
    sectors.add(Sector10.getInstance(this, iterator.next()));
  }

  public LinkedHashMap<SectorLocation, Rot> getSectorLocations() {
    return sectorLocations;
  }

  @Override
  public Iterator<Sector> iterator() {
    return sectors.iterator();
  }

  public void highlightPlanetaryHexes(
      Player activePlayer,
      Predicate<HexWithPlanet> filter,
      BiConsumer<HexWithPlanet, Player> toExecute,
      Consumer<HexWithPlanet> callBack) {
    planetaryHexes().filter(filter).forEach(h -> h.highlight(activePlayer, toExecute, callBack));
  }

  public void highlightEmptyHexes(
      Player activePlayer,
      Predicate<EmptyHex> filter,
      BiConsumer<EmptyHex, Player> toExecute,
      Consumer<EmptyHex> callBack) {
    emptyHexes().filter(filter).forEach(h -> h.highlight(activePlayer, toExecute, callBack));
  }

  public void clearHighlighting() {
    planetaryHexes().forEach(HexWithPlanet::clearHighlighting);
    emptyHexes().forEach(EmptyHex::clearHighlighting);
  }
}
