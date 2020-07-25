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
      return "ACT: +q +5c";
    }
  },
  SECTOR_SCORING {
    @Override
    public String display() {
      return "+2VP/SECTOR";
    }
  },
  TP_SCORING {
    @Override
    public String display() {
      return "+4VP/TP";
    }
  },
  M_SCORING {
    @Override
    public String display() {
      return "+2VP/M";
    }
  },
  FED_EOT_SCORING {
    @Override
    public String display() {
      return "[]: +3VP/FED";
    }
  },
  BUILD_MINE {
    @Override
    public String display() {
      return "+M -> 3VP";
    }
  },
  TECH_STEP {
    @Override
    public String display() {
      return "+TECH -> 2VP";
    }
  };

  public abstract String display();
}
