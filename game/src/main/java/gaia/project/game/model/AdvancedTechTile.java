package gaia.project.game.model;

public enum AdvancedTechTile {
  KNOWLEDGE_ACTION {
    @Override
    public String display() {
      return "ACT: +3k";
    }
  },
  ORE_ACTION {
    @Override
    public String display() {
      return "ACT: +3o";
    }
  },
  Q_C_ACTION {
    @Override
    public String display() {
      return "ACT: +q,5c";
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
}
