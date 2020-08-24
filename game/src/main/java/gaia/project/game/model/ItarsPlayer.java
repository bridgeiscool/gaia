package gaia.project.game.model;

public class ItarsPlayer extends Player {
  private static final long serialVersionUID = 3937559742818721321L;

  public ItarsPlayer(PlayerEnum playerEnum) {
    super(Race.ITARS, playerEnum);
  }

  @Override
  public void sacPower(int numTimes) {
    super.sacPower(numTimes);
    Util.plus(getGaiaBin(), numTimes);
  }
}
