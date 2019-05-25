package gaia.project.game.model;

public enum FederationTile implements UpdatePlayer {
  RESEARCH_1(EnumUtil.RESEARCH_BONUS, true),
  RESEARCH_2(EnumUtil.RESEARCH_BONUS, true),
  RESEARCH_3(EnumUtil.RESEARCH_BONUS, true),
  CREDITS_1(EnumUtil.CREDIT_BONUS, true),
  CREDITS_2(EnumUtil.CREDIT_BONUS, true),
  CREDITS_3(EnumUtil.CREDIT_BONUS, true),
  ORE_1(EnumUtil.ORE_BONUS, true),
  ORE_2(EnumUtil.ORE_BONUS, true),
  ORE_3(EnumUtil.ORE_BONUS, true),
  QIC_1(EnumUtil.QIC_BONUS, true),
  QIC_2(EnumUtil.QIC_BONUS, true),
  QIC_3(EnumUtil.QIC_BONUS, true),
  POWER_1(EnumUtil.POWER_BONUS, true),
  POWER_2(EnumUtil.POWER_BONUS, true),
  POWER_3(EnumUtil.POWER_BONUS, true),
  VP_1(EnumUtil.VP_BONUS, false),
  VP_2(EnumUtil.VP_BONUS, false),
  VP_3(EnumUtil.VP_BONUS, false);

  private final UpdatePlayer action;
  private final boolean flippable;

  FederationTile(UpdatePlayer action, boolean flippable) {
    this.action = action;
    this.flippable = flippable;
  }

  @Override
  public void updatePlayer(Player player) {
    action.updatePlayer(player);
  }

  public boolean isFlippable() {
    return flippable;
  }

  private static class EnumUtil {
    private static final UpdatePlayer RESEARCH_BONUS = p -> {
      Util.plus(p.getScore(), 6);
      Util.plus(p.getResearch(), 2);
    };

    private static final UpdatePlayer CREDIT_BONUS = p -> {
      Util.plus(p.getScore(), 7);
      Util.plus(p.getCredits(), 6);
    };

    private static final UpdatePlayer ORE_BONUS = p -> {
      Util.plus(p.getScore(), 7);
      Util.plus(p.getOre(), 2);
    };

    private static final UpdatePlayer QIC_BONUS = p -> {
      Util.plus(p.getScore(), 8);
      Util.plus(p.getQic(), 1);
    };

    private static final UpdatePlayer POWER_BONUS = p -> {
      Util.plus(p.getScore(), 8);
      Util.plus(p.getBin1(), 2);
    };

    private static final UpdatePlayer VP_BONUS = p -> Util.plus(p.getScore(), 12);
  }
}
