package gaia.project.game;

import static gaia.project.game.GuiUtils.HEX_SIZE;
import static gaia.project.game.GuiUtils.ROOT_3;
import static gaia.project.game.GuiUtils.TWO_ROOT_3;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;

public class Sector extends Group {
  public List<Hex> containedHexes;

  public Sector(Parent parent, double sizeX, double sizeY, double centerX, double centerY) {
    containedHexes = new ArrayList<>();
    // Position all the hexes on the screen and populate containedHexes
    initHexes(centerX, centerY);

    // Add the hexes
    ObservableList<Node> children = getChildren();
    children.addAll(containedHexes);
  }

  private void initHexes(double centerX, double centerY) {
    // Center Hex
    containedHexes.add(Hex.withPlanet(centerX, centerY, PlanetType.BLUE));

    // Inner Ring
    // Top hex
    containedHexes.add(Hex.withPlanet(centerX, centerY - TWO_ROOT_3 * HEX_SIZE, PlanetType.YELLOW));
    // Top right
    containedHexes.add(Hex.emptyHex(centerX + 3.0 * HEX_SIZE, centerY - ROOT_3 * HEX_SIZE));
    // Bottom right
    containedHexes.add(Hex.emptyHex(centerX + 3.0 * HEX_SIZE, centerY + ROOT_3 * HEX_SIZE));
    // Bottom
    containedHexes.add(Hex.emptyHex(centerX, centerY + TWO_ROOT_3 * HEX_SIZE));
    // Bottom left
    containedHexes.add(Hex.emptyHex(centerX - 3.0 * HEX_SIZE, centerY + ROOT_3 * HEX_SIZE));
    // Top left
    containedHexes.add(Hex.emptyHex(centerX - 3.0 * HEX_SIZE, centerY - ROOT_3 * HEX_SIZE));

    // Outer Ring
    // 12 o'clock
    containedHexes.add(Hex.emptyHex(centerX, centerY - 2.0 * TWO_ROOT_3 * HEX_SIZE));
    // 1 o'clock
    containedHexes.add(Hex.emptyHex(centerX + 3.0 * HEX_SIZE, centerY - 3.0 * ROOT_3 * HEX_SIZE));
    // 2 o'clock
    containedHexes.add(Hex.emptyHex(centerX + 6.0 * HEX_SIZE, centerY - TWO_ROOT_3 * HEX_SIZE));
    // 3 o'clock
    containedHexes.add(Hex.emptyHex(centerX + 6.0 * HEX_SIZE, centerY));
    // 4 o'ckock
    containedHexes.add(Hex.emptyHex(centerX + 6.0 * HEX_SIZE, centerY + TWO_ROOT_3 * HEX_SIZE));
    // 5 o'clock
    containedHexes.add(Hex.emptyHex(centerX + 3.0 * HEX_SIZE, centerY + 3.0 * ROOT_3 * HEX_SIZE));
    // 6 o'clock
    containedHexes.add(Hex.emptyHex(centerX, centerY + 2.0 * TWO_ROOT_3 * HEX_SIZE));
    // 7 o'clock
    containedHexes.add(Hex.emptyHex(centerX - 3.0 * HEX_SIZE, centerY + 3.0 * ROOT_3 * HEX_SIZE));
    // 8 o'clock
    containedHexes.add(Hex.emptyHex(centerX - 6.0 * HEX_SIZE, centerY + TWO_ROOT_3 * HEX_SIZE));
    // 9 o'clock
    containedHexes.add(Hex.emptyHex(centerX - 6.0 * HEX_SIZE, centerY));
    // 10 o'clock
    containedHexes.add(Hex.emptyHex(centerX - 6.0 * HEX_SIZE, centerY - TWO_ROOT_3 * HEX_SIZE));
    // 11 o'clock
    containedHexes.add(Hex.emptyHex(centerX - 3.0 * HEX_SIZE, centerY - 3.0 * ROOT_3 * HEX_SIZE));
  }
}
