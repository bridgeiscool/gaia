package gaia.project.game.model;

import javafx.collections.SetChangeListener;

public class BalTaksPlayer extends Player {
  private static final long serialVersionUID = -5023045450134206874L;

  public BalTaksPlayer(PlayerEnum playerEnum) {
    super(Race.BALTAKS, playerEnum);
  }

  @Override
  protected void addAdditionalListeners() {
    super.addAdditionalListeners();
    getPi().addListener((SetChangeListener<Coords>) change -> Util.plus(getNavLevel(), 1));
  }
}
