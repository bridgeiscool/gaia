package gaia.project.game;

import static gaia.project.game.GuiUtils.HEX_SIZE;
import static gaia.project.game.GuiUtils.ROOT_3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;

public class GameBoard extends Group implements Iterable<Sector> {
  private final List<Sector> sectors;

  public GameBoard() {
    sectors = new ArrayList<>();

    // Position all sectors on the screen
    initSectors();

    // Add the sectors
    ObservableList<Node> children = getChildren();
    children.addAll(sectors);
  }

  private void initSectors() {
    // Top row
    sectors.add(Sector1.getInstance(this, 200, 200));
    sectors.add(Sector2.getInstance(this, 200 + 15.0 * HEX_SIZE, 200 + ROOT_3 * HEX_SIZE));
    sectors.add(Sector3.getInstance(this, 200 + 30.0 * HEX_SIZE, 200 + 2.0 * ROOT_3 * HEX_SIZE));

    // Middle row
    sectors.add(Sector4.getInstance(this, 200 - 9.0 * HEX_SIZE, 200 + 7.0 * ROOT_3 * HEX_SIZE));
    sectors.add(Sector5.getInstance(this, 200 + 6.0 * HEX_SIZE, 200 + 8.0 * ROOT_3 * HEX_SIZE));
    sectors.add(Sector6.getInstance(this, 200 + 21.0 * HEX_SIZE, 200 + 9.0 * ROOT_3 * HEX_SIZE));
    sectors.add(Sector7.getInstance(this, 200 + 36.0 * HEX_SIZE, 200 + 10.0 * ROOT_3 * HEX_SIZE));

    // Bottom row
    sectors.add(Sector8.getInstance(this, 200 - 3.0 * HEX_SIZE, 200 + 15.0 * ROOT_3 * HEX_SIZE));
    sectors.add(Sector9.getInstance(this, 200 + 12.0 * HEX_SIZE, 200 + 16.0 * ROOT_3 * HEX_SIZE));
    sectors.add(Sector10.getInstance(this, 200 + 27.0 * HEX_SIZE, 200 + 17.0 * ROOT_3 * HEX_SIZE));
  }

  @Override
  public Iterator<Sector> iterator() {
    return sectors.iterator();
  }
}
