package gaia.project.game.model;

public class TerranPlayer extends Player {
  private static final long serialVersionUID = 1588962295060407390L;

  public TerranPlayer(PlayerEnum playerEnum) {
    super(Race.TERRANS, playerEnum);
  }

  @Override
  public void gaiaPhase() {
    Util.plus(getBin2(), getGaiaBin().get());
    getGaiaBin().setValue(0);
  }
}
