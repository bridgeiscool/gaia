package gaia.project.game.model;

import gaia.project.game.board.HexWithPlanet;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;

public final class AmbasPlayer extends Player {
  private transient boolean ignorePiAndMineBonuses;

  public static AmbasPlayer createNew(PlayerEnum playerEnum) {
    AmbasPlayer p = new AmbasPlayer();
    p.fromRace(Race.AMBAS, playerEnum);
    return p;
  }

  public static AmbasPlayer empty() {
    return new AmbasPlayer();
  }

  private AmbasPlayer() {}

  public void piAction(HexWithPlanet piDestination, HexWithPlanet pi) {

    Node mine = piDestination.getBuildingNode();
    piDestination.switchBuildingUI(pi.getBuildingNode());
    pi.switchBuildingUI(mine);

    ignorePiAndMineBonuses = true;
    getPi().remove(pi.getCoords());
    getMines().remove(piDestination.getCoords());
    getPi().add(piDestination.getCoords());
    getMines().add(pi.getCoords());
    ignorePiAndMineBonuses = false;
  }

  @Override
  public void buildPI(HexWithPlanet hex) {
    super.buildPI(hex);
    getSpecialActions().put(PlayerBoardAction.MOVE_PI, new SimpleBooleanProperty(false));
  }

  @Override
  public boolean ignorePiAndMineBonuses() {
    return ignorePiAndMineBonuses;
  }
}
