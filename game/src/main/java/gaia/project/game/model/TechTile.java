package gaia.project.game.model;

import javafx.beans.value.ChangeListener;

public enum TechTile {
  CHARGE_4() {
    @Override
    public String display() {
      return "+4p";
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      player.chargePower(4);
    }

    @Override
    public void addTo(Player player) {
      // Does nothing initially
    }
  },
  POWER_ORE() {
    @Override
    public String display() {
      return "(+1o, +1p)";
    }

    @Override
    public void addTo(Player player) {
      Util.plus(player.getCurrentIncome().getChargeIncome(), 1);
      Util.plus(player.getCurrentIncome().getOreIncome(), 1);
    }

    @Override
    public void removeFrom(Player player) {
      Util.minus(player.getCurrentIncome().getChargeIncome(), 1);
      Util.minus(player.getCurrentIncome().getOreIncome(), 1);
    }

  },
  RESEARCH_CREDIT() {
    @Override
    public String display() {
      return "(+1k, +1c)";
    }

    @Override
    public void addTo(Player player) {
      Util.plus(player.getCurrentIncome().getResearchIncome(), 1);
      Util.plus(player.getCurrentIncome().getCreditIncome(), 1);
    }

    @Override
    public void removeFrom(Player player) {
      Util.minus(player.getCurrentIncome().getResearchIncome(), 1);
      Util.minus(player.getCurrentIncome().getCreditIncome(), 1);
    }

  },
  QIC_ORE() {
    @Override
    public String display() {
      return "+1q +1o";
    }

    @Override
    public void addTo(Player player) {
      Util.plus(player.getQic(), 1);
      Util.plus(player.getOre(), 1);
    }
  },
  GAIA_VP() {
    private ChangeListener<Number> listener;

    @Override
    public String display() {
      return "GP -> 3VP";
    }

    @Override
    public void addTo(Player player) {
      listener = (o, oldValue, newValue) -> player.getScore().setValue(player.getScore().getValue() + 3);
      player.getGaiaPlanets().addListener(listener);
    }

    @Override
    public void removeFrom(Player player) {
      player.getGaiaPlanets().removeListener(listener);
    }

    @Override
    public boolean addsListener() {
      return true;
    }
  },
  CREDITS() {
    @Override
    public String display() {
      return "(+4c)";
    }

    @Override
    public void addTo(Player player) {
      Util.plus(player.getCurrentIncome().getCreditIncome(), 4);
    }

    @Override
    public void removeFrom(Player player) {
      Util.minus(player.getCurrentIncome().getCreditIncome(), 4);
    }
  },
  RESEARCH() {
    @Override
    public String display() {
      return "+1k/PT";
    }

    @Override
    public void addTo(Player player) {
      Util.plus(player.getResearch(), player.getBuiltOn().size());
    }

  },
  VP() {
    @Override
    public String display() {
      return "+7VP";
    }

    @Override
    public void addTo(Player player) {
      Util.plus(player.getScore(), 7);
    }
  },
  BUILDING_POWER() {
    @Override
    public String display() {
      return "PI/A=4p";
    }

    @Override
    public void addTo(Player player) {
      Util.plus(player.getBigBuildingPower(), 1);
    }

    @Override
    public void removeFrom(Player player) {
      Util.minus(player.getBigBuildingPower(), 1);
    }
  };

  public abstract void addTo(Player player);

  public void removeFrom(Player player) {
    // Does nothing by default
  }

  public abstract String display();

  public boolean isAction() {
    return false;
  }

  public void onAction(Player player) {
    // Does nothing by default
  }

  public boolean addsListener() {
    return false;
  }
}
