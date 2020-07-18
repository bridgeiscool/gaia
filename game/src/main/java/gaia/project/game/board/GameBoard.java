package gaia.project.game.board;

import static gaia.project.game.board.BoardUtils.HEX_SIZE;
import static gaia.project.game.board.BoardUtils.ROOT_3;

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
    sectors.add(Sector1.getInstance(this, 160, 160));
    sectors.add(Sector2.getInstance(this, 160 + 15.0 * HEX_SIZE, 160 + ROOT_3 * HEX_SIZE));
    sectors.add(Sector3.getInstance(this, 160 + 30.0 * HEX_SIZE, 160 + 2.0 * ROOT_3 * HEX_SIZE));

    // Middle row
    sectors.add(Sector4.getInstance(this, 160 - 9.0 * HEX_SIZE, 160 + 7.0 * ROOT_3 * HEX_SIZE));
    sectors.add(Sector5.getInstance(this, 160 + 6.0 * HEX_SIZE, 160 + 8.0 * ROOT_3 * HEX_SIZE));
    sectors.add(Sector6.getInstance(this, 160 + 21.0 * HEX_SIZE, 160 + 9.0 * ROOT_3 * HEX_SIZE));
    sectors.add(Sector7.getInstance(this, 160 + 36.0 * HEX_SIZE, 160 + 10.0 * ROOT_3 * HEX_SIZE));

    // Bottom row
    sectors.add(Sector8.getInstance(this, 160 - 3.0 * HEX_SIZE, 160 + 15.0 * ROOT_3 * HEX_SIZE));
    sectors.add(Sector9.getInstance(this, 160 + 12.0 * HEX_SIZE, 160 + 16.0 * ROOT_3 * HEX_SIZE));
    sectors.add(Sector10.getInstance(this, 160 + 27.0 * HEX_SIZE, 160 + 17.0 * ROOT_3 * HEX_SIZE));
  }

  @Override
  public Iterator<Sector> iterator() {
    return sectors.iterator();
  }
}
