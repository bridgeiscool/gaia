package gaia.project.game.board;

import static gaia.project.game.board.BoardUtils.HEX_SIZE;
import static gaia.project.game.board.BoardUtils.ROOT_3;
import static gaia.project.game.board.BoardUtils.TWO_ROOT_3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Preconditions;

import gaia.project.game.PlanetType;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;

public class Sector extends Group implements Iterable<Hex> {
  public final List<Hex> containedHexes;
  public final SectorLocation location;
  public final int sectorId;

  public Sector(Parent parent, SectorLocation location, List<PlanetType> planetTypes, int sectorId) {
    containedHexes = new ArrayList<>();
    this.sectorId = sectorId;
    this.location = location;

    // Position all the hexes on the screen and populate containedHexes
    initHexes(location.getCenterX(), location.getCenterY(), planetTypes);

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
  private void initHexes(double centerX, double centerY, List<PlanetType> planetTypes) {
    Preconditions.checkArgument(planetTypes.size() == 19);

    // Center Hex
    containedHexes.add(getHex(centerX, centerY, planetTypes.get(9)));

    // Inner Ring
    // Top hex
    containedHexes.add(getHex(centerX, centerY - TWO_ROOT_3 * HEX_SIZE, planetTypes.get(4)));
    // Top right
    containedHexes.add(getHex(centerX + 3.0 * HEX_SIZE, centerY - ROOT_3 * HEX_SIZE, planetTypes.get(7)));
    // Bottom right
    containedHexes.add(getHex(centerX + 3.0 * HEX_SIZE, centerY + ROOT_3 * HEX_SIZE, planetTypes.get(12)));
    // Bottom
    containedHexes.add(getHex(centerX, centerY + TWO_ROOT_3 * HEX_SIZE, planetTypes.get(14)));
    // Bottom left
    containedHexes.add(getHex(centerX - 3.0 * HEX_SIZE, centerY + ROOT_3 * HEX_SIZE, planetTypes.get(11)));
    // Top left
    containedHexes.add(getHex(centerX - 3.0 * HEX_SIZE, centerY - ROOT_3 * HEX_SIZE, planetTypes.get(6)));

    // Outer Ring
    // 12 o'clock
    containedHexes.add(getHex(centerX, centerY - 2.0 * TWO_ROOT_3 * HEX_SIZE, planetTypes.get(0)));
    // 1 o'clock
    containedHexes.add(getHex(centerX + 3.0 * HEX_SIZE, centerY - 3.0 * ROOT_3 * HEX_SIZE, planetTypes.get(2)));
    // 2 o'clock
    containedHexes.add(getHex(centerX + 6.0 * HEX_SIZE, centerY - TWO_ROOT_3 * HEX_SIZE, planetTypes.get(5)));
    // 3 o'clock
    containedHexes.add(getHex(centerX + 6.0 * HEX_SIZE, centerY, planetTypes.get(10)));
    // 4 o'ckock
    containedHexes.add(getHex(centerX + 6.0 * HEX_SIZE, centerY + TWO_ROOT_3 * HEX_SIZE, planetTypes.get(15)));
    // 5 o'clock
    containedHexes.add(getHex(centerX + 3.0 * HEX_SIZE, centerY + 3.0 * ROOT_3 * HEX_SIZE, planetTypes.get(17)));
    // 6 o'clock
    containedHexes.add(getHex(centerX, centerY + 2.0 * TWO_ROOT_3 * HEX_SIZE, planetTypes.get(18)));
    // 7 o'clock
    containedHexes.add(getHex(centerX - 3.0 * HEX_SIZE, centerY + 3.0 * ROOT_3 * HEX_SIZE, planetTypes.get(16)));
    // 8 o'clock
    containedHexes.add(getHex(centerX - 6.0 * HEX_SIZE, centerY + TWO_ROOT_3 * HEX_SIZE, planetTypes.get(13)));
    // 9 o'clock
    containedHexes.add(getHex(centerX - 6.0 * HEX_SIZE, centerY, planetTypes.get(8)));
    // 10 o'clock
    containedHexes.add(getHex(centerX - 6.0 * HEX_SIZE, centerY - TWO_ROOT_3 * HEX_SIZE, planetTypes.get(3)));
    // 11 o'clock
    containedHexes.add(getHex(centerX - 3.0 * HEX_SIZE, centerY - 3.0 * ROOT_3 * HEX_SIZE, planetTypes.get(1)));
  }

  private Hex getHex(double centerX, double centerY, PlanetType planetType) {
    return planetType == PlanetType.NONE
        ? Hex.emptyHex(centerX, centerY, sectorId)
        : Hex.withPlanet(centerX, centerY, sectorId, planetType);
  }

  @Override
  public Iterator<Hex> iterator() {
    return containedHexes.iterator();
  }

  @Override
  public String toString() {
    return "Sector " + sectorId + "(" + location.getCenterX() + ", " + location.getCenterY() + ")";
  }
}
