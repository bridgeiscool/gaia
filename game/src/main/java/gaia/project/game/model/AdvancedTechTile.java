package gaia.project.game.model;

public enum AdvancedTechTile {
  KNOWLEDGE_ACTION {
    @Override
    public String display() {
      return "+3k";
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getResearch(), 3);
    }
  },
  ORE_ACTION {
    @Override
    public String display() {
      return "ACT: +3o";
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getOre(), 3);
    }
  },
  Q_C_ACTION {
    @Override
    public String display() {
      return "ACT: +q,5c";
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getQic(), 1);
      Util.plus(player.getCredits(), 5);
    }
  },
  SECTOR_SCORING {
    @Override
    public String display() {
      return "+2/SECT";
    }
  },
  SECTOR_ORE {
    @Override
    public String display() {
      return "+2o/SECT";
    }
  },
  TP_SCORING {
    @Override
    public String display() {
      return "+4/TP";
    }
  },
  M_SCORING {
    @Override
    public String display() {
      return "+2/M";
    }
  },
  GP_SCORING {
    @Override
    public String display() {
      return "+2/GP";
    }
  },
  FED_SCORING {
    @Override
    public String display() {
      return "+5/FED";
    }
  },
  FED_EOT_SCORING {
    @Override
    public String display() {
      return "[]: +3/FED";
    }
  },
  RL_EOT_SCORING {
    @Override
    public String display() {
      return "[]: +3/RL";
    }
  },
  PLANETS_EOT_SCORING {
    @Override
    public String display() {
      return "[]: +1/PT";
    }
  },
  BUILD_MINE {
    @Override
    public String display() {
      return "+M -> 3";
    }
  },
  BUILD_TP {
    @Override
    public String display() {
      return "+TP -> 3";
    }
  },
  TECH_STEP {
    @Override
    public String display() {
      return "+TECH -> 2";
    }
  };

  public abstract String display();

  public boolean isAction() {
    return false;
  }

  public void onAction(Player player) {
    // Does nothing by default - override for actions
  }
}
