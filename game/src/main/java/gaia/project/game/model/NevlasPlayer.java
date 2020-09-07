package gaia.project.game.model;

public final class NevlasPlayer extends Player {

  public static NevlasPlayer createNew(PlayerEnum playerEnum) {
    NevlasPlayer p = new NevlasPlayer();
    p.fromRace(Race.NEVLAS, playerEnum);
    return p;
  }

  public static NevlasPlayer empty() {
    return new NevlasPlayer();
  }

  private NevlasPlayer() {}

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
