package gaia.project.game.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.annotations.VisibleForTesting;

import gaia.project.game.PlanetType;
import gaia.project.game.model.Player;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

public class GameBoard extends Group implements Iterable<Sector> {
  private final List<SectorLocation> sectorLocations;
  private final List<Sector> sectors;

  public static GameBoard originalBoard() {
    return new GameBoard(
        Arrays.asList(
            SectorLocation.LOC1,
            SectorLocation.LOC4,
            SectorLocation.LOC5,
            SectorLocation.LOC8,
            SectorLocation.LOC2,
            SectorLocation.LOC6,
            SectorLocation.LOC9,
            SectorLocation.LOC7,
            SectorLocation.LOC3,
            SectorLocation.LOC0));
  }

  public static GameBoard random(Random random) {
    List<SectorLocation> unshuffled = Arrays.asList(SectorLocation.values());

    GameBoard maybeValid;
    do {
      System.out.println("Shuffling...");
      Collections.shuffle(unshuffled, random);
      maybeValid = new GameBoard(unshuffled);
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
        for (Hex adjacent : hex.getHexesWithinRange(hexes(), 1)) {
          // Exclude adjacent planets that aren't purple
          if (hex.sharesPlanetType(adjacent)) {
            return false;
          }
        }
      }
    }

    return true;
  }

  public GameBoard(List<SectorLocation> sectorPlacements) {
    this.sectorLocations = sectorPlacements;
    sectors = new ArrayList<>();

    // Position all sectors on the screen
    initSectors(sectorPlacements);

    // Add the sectors
    ObservableList<Node> children = getChildren();
    children.addAll(sectors);
  }

  private void initSectors(List<SectorLocation> sectorPlacements) {
    sectors.add(Sector1.getInstance(this, sectorPlacements.get(0)));
    sectors.add(Sector2.getInstance(this, sectorPlacements.get(1)));
    sectors.add(Sector3.getInstance(this, sectorPlacements.get(2)));
    sectors.add(Sector4.getInstance(this, sectorPlacements.get(3)));
    sectors.add(Sector5.getInstance(this, sectorPlacements.get(4)));
    sectors.add(Sector6.getInstance(this, sectorPlacements.get(5)));
    sectors.add(Sector7.getInstance(this, sectorPlacements.get(6)));
    sectors.add(Sector8.getInstance(this, sectorPlacements.get(7)));
    sectors.add(Sector9.getInstance(this, sectorPlacements.get(8)));
    sectors.add(Sector10.getInstance(this, sectorPlacements.get(9)));
  }

  public List<SectorLocation> getSectorLocations() {
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
