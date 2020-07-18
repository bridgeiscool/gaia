package gaia.project.game.board;

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
    sectors.add(Sector1.getInstance(this, SectorLocation.LOC0));
    sectors.add(Sector2.getInstance(this, SectorLocation.LOC1));
    sectors.add(Sector3.getInstance(this, SectorLocation.LOC2));

    // Middle row
    sectors.add(Sector4.getInstance(this, SectorLocation.LOC3));
    sectors.add(Sector5.getInstance(this, SectorLocation.LOC4));
    sectors.add(Sector6.getInstance(this, SectorLocation.LOC5));
    sectors.add(Sector7.getInstance(this, SectorLocation.LOC6));

    // Bottom row
    sectors.add(Sector8.getInstance(this, SectorLocation.LOC7));
    sectors.add(Sector9.getInstance(this, SectorLocation.LOC8));
    sectors.add(Sector10.getInstance(this, SectorLocation.LOC9));
  }

  @Override
  public Iterator<Sector> iterator() {
    return sectors.iterator();
  }
}
