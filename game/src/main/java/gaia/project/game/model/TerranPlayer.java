package gaia.project.game.model;

import gaia.project.game.TerranPIDialog;

public class TerranPlayer extends Player {
  private static final long serialVersionUID = 1588962295060407390L;

  public TerranPlayer(PlayerEnum playerEnum) {
    super(Race.TERRANS, playerEnum);
  }

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
