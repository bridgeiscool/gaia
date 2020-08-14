package gaia.project.game.model;

public enum TechTile {
  CHARGE_4(p -> {}) {
    @Override
    public String display() {
      return "ACT: +4p";
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      player.chargePower(4);
    }
  },
  POWER_ORE(p -> {
    Util.plus(p.getCurrentIncome().getChargeIncome(), 1);
    Util.plus(p.getCurrentIncome().getOreIncome(), 1);
  }) {
    @Override
    public String display() {
      return "(+1o, +1p)";
    }
  },
  RESEARCH_CREDIT(p -> {
    Util.plus(p.getCurrentIncome().getResearchIncome(), 1);
    Util.plus(p.getCurrentIncome().getCreditIncome(), 1);
  }) {
    @Override
    public String display() {
      return "(+1k, +1c)";
    }
  },
  QIC_ORE(p -> {
    Util.plus(p.getQic(), 1);
    Util.plus(p.getOre(), 1);
  }) {
    @Override
    public String display() {
      return "+1q +1o";
    }
  },
  GAIA_VP(p -> p.getGaiaPlanets().addListener((o, oldValue, newValue) -> {
    p.getScore().setValue(p.getScore().getValue() + 3);
  })) {
    @Override
    public String display() {
      return "GP -> 3VP";
    }
  },
  CREDITS(p -> {
    Util.plus(p.getCurrentIncome().getCreditIncome(), 4);
  }) {
    @Override
    public String display() {
      return "(+4c)";
    }
  },
  RESEARCH(p -> Util.plus(p.getResearch(), p.getBuiltOn().size())) {
    @Override
    public String display() {
      return "+1k/PT";
    }
  },
  VP(p -> Util.plus(p.getScore(), 7)) {
    @Override
    public String display() {
      return "+7VP";
    }
  },
  BUILDING_POWER(p -> p.getBigBuildingPower().setValue(4)) {
    @Override
    public String display() {
      return "PI/A=4p";
    }
  };

  private UpdatePlayer update;

  TechTile(UpdatePlayer update) {
    this.update = update;
  }

  public void updatePlayer(Player player) {
    update.updatePlayer(player);
  }

  public abstract String display();

  public boolean isAction() {
    return false;
  }

  public void onAction(Player player) {
    // Does nothing by default
  }
}
