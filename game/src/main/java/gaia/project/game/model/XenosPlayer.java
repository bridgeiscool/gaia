package gaia.project.game.model;

public final class XenosPlayer extends Player {

  public static XenosPlayer createNew(PlayerEnum playerEnum) {
    XenosPlayer p = new XenosPlayer();
    p.fromRace(Race.XENOS, playerEnum);
    return p;
  }

  public static XenosPlayer empty() {
    return new XenosPlayer();
  }

  private XenosPlayer() {}

  @Override
  public int getFedPower() {
    if (!getPi().isEmpty()) {
      return 6;
    } else {
      return super.getFedPower();
    }
  }
}
