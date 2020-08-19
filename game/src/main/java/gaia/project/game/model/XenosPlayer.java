package gaia.project.game.model;

public class XenosPlayer extends Player {
  private static final long serialVersionUID = 3937559742818721321L;

  public XenosPlayer(PlayerEnum playerEnum) {
    super(Race.XENOS, playerEnum);
  }

  @Override
  public int getFedPower() {
    if (!getPi().isEmpty()) {
      return 6;
    } else {
      return super.getFedPower();
    }
  }
}
