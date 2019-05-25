package gaia.project.game.model;

public enum TechTile implements UpdatePlayer {
  CHARGE_4(p -> {}), POWER_ORE(p -> {
    Util.plus(p.getCurrentIncome().getChargeIncome(), 1);
    Util.plus(p.getCurrentIncome().getOreIncome(), 1);
  }), RESEARCH_CREDIT(p -> {
    Util.plus(p.getCurrentIncome().getResearchIncome(), 1);
    Util.plus(p.getCurrentIncome().getCreditIncome(), 1);
  }), QIC_ORE(p -> {
    Util.plus(p.getQic(), 1);
    Util.plus(p.getOre(), 1);
  }), GAIA_VP(p -> p.getGaiaBuildBonus().setValue(true)), CREDITS(p -> {
    Util.plus(p.getCurrentIncome().getCreditIncome(), 4);
  }),
  RESEARCH(p -> Util.plus(p.getResearch(), p.getBuiltOn().size())),
  VP(p -> Util.plus(p.getScore(), 7)),
  BUILDING_POWER(p -> p.getBigBuildingPower().setValue(4));

  private UpdatePlayer update;

  TechTile(UpdatePlayer update) {
    this.update = update;
  }

  @Override
  public void updatePlayer(Player player) {
    update.updatePlayer(player);
  }
}
