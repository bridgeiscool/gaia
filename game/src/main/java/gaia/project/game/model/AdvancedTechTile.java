package gaia.project.game.model;

import javafx.collections.SetChangeListener;

public enum AdvancedTechTile {
  KNOWLEDGE_ACTION {
    @Override
    public String display() {
      return "+3k";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getSpecialActions().put(this, false);
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getResearch(), 3);
      player.getSpecialActions().put(this, true);
    }
  },
  ORE_ACTION {
    @Override
    public String display() {
      return "+3o";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getSpecialActions().put(this, false);
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getOre(), 3);
      player.getSpecialActions().put(this, true);
    }
  },
  Q_C_ACTION {
    @Override
    public String display() {
      return "+q+5c";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getSpecialActions().put(this, false);
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getQic(), 1);
      Util.plus(player.getCredits(), 5);
      player.getSpecialActions().put(this, true);
    }
  },
  SECTOR_SCORING {
    @Override
    public String display() {
      return "+2/SECT";
    }

    @Override
    public void updatePlayer(Player player) {
      Util.plus(player.getScore(), player.getSectors().size() * 2);
    }
  },
  SECTOR_ORE {
    @Override
    public String display() {
      return "+o/SECT";
    }

    @Override
    public void updatePlayer(Player player) {
      Util.plus(player.getOre(), player.getSectors().size());
    }
  },
  TP_SCORING {
    @Override
    public String display() {
      return "+4/TP";
    }

    @Override
    public void updatePlayer(Player player) {
      Util.plus(player.getScore(), player.getTradingPosts().size() * 4);
    }
  },
  M_SCORING {
    @Override
    public String display() {
      return "+2/M";
    }

    @Override
    public void updatePlayer(Player player) {
      Util.plus(player.getScore(), player.getMines().size() * 2);
    }
  },
  GP_SCORING {
    @Override
    public String display() {
      return "+2/GP";
    }

    @Override
    public void updatePlayer(Player player) {
      Util.plus(player.getScore(), player.getGaiaPlanets().get() * 2);
    }
  },
  FED_SCORING {
    @Override
    public String display() {
      return "+5/FED";
    }

    @Override
    public void updatePlayer(Player player) {
      Util.plus(player.getScore(), player.getFederationTiles().size() * 5);
    }
  },
  FED_EOT_SCORING {
    @Override
    public String display() {
      return "[]: +3/FED";
    }

    @Override
    public void updatePlayer(Player player) {
      // Do nothing...
    }

    @Override
    public void addVps(Player player) {
      Util.plus(player.getScore(), player.getFederationTiles().size() * 3);
    }
  },
  RL_EOT_SCORING {
    @Override
    public String display() {
      return "[]: +3/RL";
    }

    @Override
    public void updatePlayer(Player player) {
      // Do nothing...
    }

    @Override
    public void addVps(Player player) {
      Util.plus(player.getScore(), player.getResearchLabs().size() * 3);
    }
  },
  PLANETS_EOT_SCORING {
    @Override
    public String display() {
      return "[]: +1/PT";
    }

    @Override
    public void updatePlayer(Player player) {
      // Do nothing...
    }

    @Override
    public void addVps(Player player) {
      Util.plus(player.getScore(), player.getBuiltOn().size() * 3);
    }
  },
  BUILD_MINE {
    @Override
    public String display() {
      return "+M -> 3";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getMines().addListener((SetChangeListener<Coords>) change -> {
        if (change.wasAdded()) {
          Util.plus(player.getScore(), 3);
        }
      });
    }

    @Override
    public boolean addsListener() {
      return true;
    }

  },
  BUILD_TP {
    @Override
    public String display() {
      return "+TP -> 3";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getTradingPosts().addListener((SetChangeListener<Coords>) change -> {
        if (change.wasAdded()) {
          Util.plus(player.getScore(), 3);
        }
      });
    }

    @Override
    public boolean addsListener() {
      return true;
    }
  },
  TECH_STEP {
    @Override
    public String display() {
      return "+TECH -> 2";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getTerraformingLevel().addListener((o, oldValue, newValue) -> {
        Util.plus(player.getScore(), 2);
      });
      player.getNavLevel().addListener((o, oldValue, newValue) -> {
        Util.plus(player.getScore(), 2);
      });
      player.getAiLevel().addListener((o, oldValue, newValue) -> {
        Util.plus(player.getScore(), 2);
      });
      player.getGaiaformingLevel().addListener((o, oldValue, newValue) -> {
        Util.plus(player.getScore(), 2);
      });
      player.getEconLevel().addListener((o, oldValue, newValue) -> {
        Util.plus(player.getScore(), 2);
      });
      player.getKnowledgeLevel().addListener((o, oldValue, newValue) -> {
        Util.plus(player.getScore(), 2);
      });
    }

    @Override
    public boolean addsListener() {
      return true;
    }
  };

  public abstract String display();

  public abstract void updatePlayer(Player player);

  public boolean isAction() {
    return false;
  }

  public void onAction(Player player) {
    // Does nothing by default - override for actions
  }

  public void addVps(Player player) {
    // Does nothing by default - override for end of round scoring tiles
  }

  public boolean addsListener() {
    return false;
  }
}
