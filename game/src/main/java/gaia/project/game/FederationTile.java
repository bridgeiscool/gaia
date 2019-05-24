package gaia.project.game;

public enum FederationTile implements UpdatePlayer {
  RESEARCH_1(Util.RESEARCH_BONUS, true),
  RESEARCH_2(Util.RESEARCH_BONUS, true),
  RESEARCH_3(Util.RESEARCH_BONUS, true),
  CREDITS_1(Util.CREDIT_BONUS, true),
  CREDITS_2(Util.CREDIT_BONUS, true),
  CREDITS_3(Util.CREDIT_BONUS, true),
  ORE_1(Util.ORE_BONUS, true),
  ORE_2(Util.ORE_BONUS, true),
  ORE_3(Util.ORE_BONUS, true),
  QIC_1(Util.QIC_BONUS, true),
  QIC_2(Util.QIC_BONUS, true),
  QIC_3(Util.QIC_BONUS, true),
  POWER_1(Util.POWER_BONUS, true),
  POWER_2(Util.POWER_BONUS, true),
  POWER_3(Util.POWER_BONUS, true),
  VP_1(Util.VP_BONUS, false),
  VP_2(Util.VP_BONUS, false),
  VP_3(Util.VP_BONUS, false);

  private UpdatePlayer action;

  FederationTile(UpdatePlayer action, boolean flippable) {
    this.action = action;
  }

  @Override
  public void updatePlayer(Player player) {
    action.updatePlayer(player);
  }

  static class Util {
    private static final UpdatePlayer RESEARCH_BONUS = p -> {
      p.getScore().setValue(p.getScore().getValue().intValue() + 6);
      p.getResearch().setValue(p.getResearch().getValue().intValue() + 2);
    };

    private static final UpdatePlayer CREDIT_BONUS = p -> {
      p.getScore().setValue(p.getScore().getValue().intValue() + 7);
      p.getCredits().setValue(p.getCredits().getValue().intValue() + 6);
    };

    private static final UpdatePlayer ORE_BONUS = p -> {
      p.getScore().setValue(p.getScore().getValue().intValue() + 7);
      p.getOre().setValue(p.getOre().getValue().intValue() + 2);
    };

    private static final UpdatePlayer QIC_BONUS = p -> {
      p.getScore().setValue(p.getScore().getValue().intValue() + 8);
      p.getQic().setValue(p.getQic().getValue().intValue() + 1);
    };

    private static final UpdatePlayer POWER_BONUS = p -> {
      p.getScore().setValue(p.getScore().getValue().intValue() + 8);
      p.getBin1().setValue(p.getBin1().getValue().intValue() + 2);
    };

    private static final UpdatePlayer VP_BONUS = p -> {
      p.getScore().setValue(p.getScore().getValue().intValue() + 12);
    };
  }
}
