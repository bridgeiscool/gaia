package gaia.project.game.board;

import static gaia.project.game.board.BoardUtils.ROOT_3;
import static gaia.project.game.board.BoardUtils.TWO_ROOT_3;
import static gaia.project.game.board.BoardUtils.hexSize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;

import gaia.project.game.controller.PlanetType;
import gaia.project.game.model.Coords;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;

public class Sector extends Group implements Iterable<Hex> {
  public final List<Hex> containedHexes;
  public final SectorLocation location;
  public final int sectorId;

  public Sector(Parent parent, SectorLocation location, List<PlanetType> planetTypes, int sectorId, Rot rot) {
    containedHexes = new ArrayList<>();
    this.sectorId = sectorId;
    this.location = location;

    // Position all the hexes on the screen and populate containedHexes
    initHexes(location.getCenterX(), location.getCenterY(), planetTypes, rot);

    // Add the hexes
    ObservableList<Node> children = getChildren();
    children.addAll(containedHexes);
  }

  /**
   * Indices:
   * 
   * <pre>
   *          0
   *       1     2   
   *    3     4     5   
   *       6     7   
   *    8     9    10           
   *       11   12         
   *    13   14    15
   *       16   17
   *         18
   * </pre>
   */
  private void initHexes(double centerX, double centerY, List<PlanetType> planetTypes, Rot rot) {
    Preconditions.checkArgument(planetTypes.size() == 19);

    Coords sectorCenter = new Coords(centerX, centerY);

    // Center Hex never has a planet - always the label for the sector
    Preconditions.checkArgument(planetTypes.get(9) == PlanetType.NONE);
    containedHexes.add(getHex(sectorCenter, PlanetType.NONE, hexId(9)));

    // Inner Ring
    // Top hex
    containedHexes
        .add(getHex(sectorCenter.plus(rot.applyTo(0.0, -1.0 * TWO_ROOT_3 * hexSize())), planetTypes.get(4), hexId(4)));
    // Top right
    containedHexes.add(
        getHex(
            sectorCenter.plus(rot.applyTo(3.0 * hexSize(), -1.0 * ROOT_3 * hexSize())),
            planetTypes.get(7),
            hexId(7)));
    // Bottom right
    containedHexes.add(
        getHex(sectorCenter.plus(rot.applyTo(3.0 * hexSize(), ROOT_3 * hexSize())), planetTypes.get(12), hexId(12)));
    // Bottom
    containedHexes
        .add(getHex(sectorCenter.plus(rot.applyTo(0.0, TWO_ROOT_3 * hexSize())), planetTypes.get(14), hexId(14)));
    // Bottom left
    containedHexes.add(
        getHex(sectorCenter.plus(rot.applyTo(-3.0 * hexSize(), ROOT_3 * hexSize())), planetTypes.get(11), hexId(11)));
    // Top left
    containedHexes.add(
        getHex(
            sectorCenter.plus(rot.applyTo(-3.0 * hexSize(), -1.0 * ROOT_3 * hexSize())),
            planetTypes.get(6),
            hexId(6)));

    // Outer Ring
    // 12 o'clock
    containedHexes
        .add(getHex(sectorCenter.plus(rot.applyTo(0.0, -2.0 * TWO_ROOT_3 * hexSize())), planetTypes.get(0), hexId(0)));
    // 1 o'clock
    containedHexes.add(
        getHex(
            sectorCenter.plus(rot.applyTo(3.0 * hexSize(), -3.0 * ROOT_3 * hexSize())),
            planetTypes.get(2),
            hexId(2)));
    // 2 o'clock
    containedHexes.add(
        getHex(
            sectorCenter.plus(rot.applyTo(6.0 * hexSize(), -1.0 * TWO_ROOT_3 * hexSize())),
            planetTypes.get(5),
            hexId(5)));
    // 3 o'clock
    containedHexes.add(getHex(sectorCenter.plus(rot.applyTo(6.0 * hexSize(), 0.0)), planetTypes.get(10), hexId(10)));
    // 4 o'ckock
    containedHexes.add(
        getHex(
            sectorCenter.plus(rot.applyTo(6.0 * hexSize(), TWO_ROOT_3 * hexSize())),
            planetTypes.get(15),
            hexId(15)));
    // 5 o'clock
    containedHexes.add(
        getHex(
            sectorCenter.plus(rot.applyTo(3.0 * hexSize(), 3.0 * ROOT_3 * hexSize())),
            planetTypes.get(17),
            hexId(17)));
    // 6 o'clock
    containedHexes
        .add(getHex(sectorCenter.plus(rot.applyTo(0.0, 2.0 * TWO_ROOT_3 * hexSize())), planetTypes.get(18), hexId(18)));
    // 7 o'clock
    containedHexes.add(
        getHex(
            sectorCenter.plus(rot.applyTo(-3.0 * hexSize(), 3.0 * ROOT_3 * hexSize())),
            planetTypes.get(16),
            hexId(16)));
    // 8 o'clock
    containedHexes.add(
        getHex(
            sectorCenter.plus(rot.applyTo(-6.0 * hexSize(), TWO_ROOT_3 * hexSize())),
            planetTypes.get(13),
            hexId(13)));
    // 9 o'clock
    containedHexes.add(getHex(sectorCenter.plus(rot.applyTo(-6.0 * hexSize(), 0.0)), planetTypes.get(8), hexId(8)));
    // 10 o'clock
    containedHexes.add(
        getHex(
            sectorCenter.plus(rot.applyTo(-6.0 * hexSize(), -1.0 * TWO_ROOT_3 * hexSize())),
            planetTypes.get(3),
            hexId(3)));
    // 11 o'clock
    containedHexes.add(
        getHex(
            sectorCenter.plus(rot.applyTo(-3.0 * hexSize(), -3.0 * ROOT_3 * hexSize())),
            planetTypes.get(1),
            hexId(1)));
  }

  private Hex getHex(Coords center, PlanetType planetType, String hexId) {
    return planetType == PlanetType.NONE
        ? EmptyHex.normal(center, sectorId, hexId)
        : new HexWithPlanet(center, sectorId, new Planet(center.getCenterX(), center.getCenterY(), planetType), hexId);
  }

  private String hexId(int hexNumber) {
    return sectorId + "." + hexNumber;
  }

  @Override
  public Iterator<Hex> iterator() {
    return containedHexes.iterator();
  }

  public void addCenterLabel() {
    Coords coords = new Coords(location.getCenterX(), location.getCenterY());
    containedHexes.forEach(h -> {
      if (h.getCoords().equals(coords)) {
        ((EmptyHex) h).addCenterLabel(sectorId);
      }
    });
  }

  @Override
  public String toString() {
    return "Sector " + sectorId + "(" + location.getCenterX() + ", " + location.getCenterY() + ")";
  }
}
