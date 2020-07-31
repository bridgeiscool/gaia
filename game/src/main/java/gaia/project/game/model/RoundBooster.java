package gaia.project.game.model;

public enum RoundBooster {
  DIG("+1T", "(+2c)"),
  JUMP("+3 NAV", "(+2p)"),
  ORE_K("(+1o)", "(+1k)"),
  Q_C("(+1q)", "(+2c)"),
  ORE_PT("(+1o)", "(+2pt)"),
  GAIA_PLANETS("[]: GP", "(+4c)"),
  MINES("[]:M", "(+1o)"),
  TPS("[]:TP", "(+1o)"),
  RLS("[]:RL", "(+1k)"),
  BIGS("[]:BB", "(+4p)");

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
