package gaia.project.game.model;

public final class ItarsPlayer extends Player {
  public static ItarsPlayer createNew(PlayerEnum playerEnum) {
    ItarsPlayer p = new ItarsPlayer();
    p.fromRace(Race.ITARS, playerEnum);

    return p;
  }

  public static ItarsPlayer empty() {
    return new ItarsPlayer();
  }

  private ItarsPlayer() {}

  @Override
  public void sacPower(int numTimes) {
    super.sacPower(numTimes);
    Util.plus(getGaiaBin(), numTimes);
  }
}
