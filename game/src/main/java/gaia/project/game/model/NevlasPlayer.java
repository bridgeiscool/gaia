package gaia.project.game.model;

public class NevlasPlayer extends Player {
  private static final long serialVersionUID = -7064457047755128024L;

  public NevlasPlayer(PlayerEnum playerEnum) {
    super(Race.NEVLAS, playerEnum);
  }

  @Override
  public void convertResourcesToVps() {
    if (getBin2().intValue() > 1) {
      sacPower(getBin2().intValue() / 2);
    }

    updateScore(
        (getCredits().intValue()
            + getOre().intValue()
            + getResearch().intValue()
            + getQic().intValue()
            + getBin3().intValue() * 2) / 3,
        "Resources");
  }

  @Override
  public int spendablePower() {
    return getPi().isEmpty() ? getBin3().get() : getBin3().get() * 2;
  }
}
