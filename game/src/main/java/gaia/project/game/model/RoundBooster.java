package gaia.project.game.model;

public enum RoundBooster {
  DIG("+1T", "(+2c)"),
  JUMP("+3 NAV", "(+2p)"),
  ORE_K("(+1o)", "(+1k)"),
  Q_C("(+1q)", "(+2c)"),
  ORE_PT("(+1o)", "(+2pt)"),
  GAIA_PLANETS("[]: +1/GP", "(+4c)"),
  MINES("[]: +1/M", "(+1o)"),
  TPS("[]: +1/TP", "(+1o)"),
  RLS("[]: +3/RL", "(+1k)"),
  BIGS("[]: +4/BB", "(+4p)");

  private final String topText;
  private final String bottomText;

  private RoundBooster(String topText, String bottomText) {
    this.topText = topText;
    this.bottomText = bottomText;
  }

  public String getTopText() {
    return topText;
  }

  public String getBottomText() {
    return bottomText;
  }
}
