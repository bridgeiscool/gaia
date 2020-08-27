package gaia.project.game.model;

import java.util.Arrays;
import java.util.List;

public enum FederationTile implements UpdatePlayer {
  RESEARCH(EnumUtil.RESEARCH_BONUS, true, "6 2k"),
  CREDITS(EnumUtil.CREDIT_BONUS, true, "7 6c"),
  ORE(EnumUtil.ORE_BONUS, true, "7 2o"),
  QIC(EnumUtil.QIC_BONUS, true, "8 1q"),
  POWER(EnumUtil.POWER_BONUS, true, "8 2pt"),
  VP(EnumUtil.VP_BONUS, false, "12"),
  GLEENS(EnumUtil.GLEENS_BONUS, true, "1o2c1k");

  private final UpdatePlayer action;
  private final boolean flippable;
  private final String text;

  FederationTile(UpdatePlayer action, boolean flippable, String text) {
    this.action = action;
    this.flippable = flippable;
    this.text = text;
  }

  public String getText() {
    return text;
  }

  @Override
  public void updatePlayer(Player player) {
    action.updatePlayer(player);
  }

  public boolean isFlippable() {
    return flippable;
  }

  public static List<FederationTile> normalFederations() {
    return Arrays.asList(values()).subList(0, 6);
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

    private static final UpdatePlayer GLEENS_BONUS = p -> {
      Util.plus(p.getOre(), 1);
      Util.plus(p.getCredits(), 2);
      Util.plus(p.getResearch(), 1);
    };
  }
}
