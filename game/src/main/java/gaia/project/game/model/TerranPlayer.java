package gaia.project.game.model;

import gaia.project.game.TerranPIDialog;

public final class TerranPlayer extends Player {

  public static TerranPlayer createNew(PlayerEnum playerEnum) {
    TerranPlayer p = new TerranPlayer();
    p.fromRace(Race.TERRANS, playerEnum);

    return p;
  }

  public static TerranPlayer empty() {
    return new TerranPlayer();
  }

  private TerranPlayer() {}

  @Override
  public void gaiaPhase() {
    if (!getPi().isEmpty() && getGaiaBin().get() > 0) {

      TerranPIDialog dialog = new TerranPIDialog(this);
      while (!dialog.isSaved()) {
        dialog.showAndWait();
      }

      Util.plus(getResearch(), dialog.getKnowledge().get());
      Util.plus(getQic(), dialog.getQic().get());
      Util.plus(getOre(), dialog.getOre().get());
      Util.plus(getCredits(), dialog.getCredits().get());
    }
    Util.plus(getBin2(), getGaiaBin().get());
    getGaiaBin().setValue(0);
  }
}
