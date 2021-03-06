package gaia.project.game.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.SetChangeListener;

public enum AdvancedTechTile {
  KNOWLEDGE_ACTION {
    @Override
    public String display() {
      return "+3k";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getSpecialActions().put(this, new SimpleBooleanProperty(false));
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getResearch(), 3);
      player.getSpecialActions().get(this).setValue(true);
    }
  },
  ORE_ACTION {
    @Override
    public String display() {
      return "+3o";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getSpecialActions().put(this, new SimpleBooleanProperty(false));
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getOre(), 3);
      player.getSpecialActions().get(this).setValue(true);
    }
  },
  Q_C_ACTION {
    @Override
    public String display() {
      return "+q5c";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getSpecialActions().put(this, new SimpleBooleanProperty(false));
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getQic(), 1);
      Util.plus(player.getCredits(), 5);
      player.getSpecialActions().get(this).setValue(true);
    }
  },
  SECTOR_SCORING {
    @Override
    public String display() {
      return "+2/SECT";
    }

    @Override
    public void updatePlayer(Player player) {
      player.updateScore(player.getSectors().size() * 2, "ATT " + display());
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
      player.updateScore(player.getTradingPosts().size() * 4, "ATT " + display());
    }
  },
  M_SCORING {
    @Override
    public String display() {
      return "+2/M";
    }

    @Override
    public void updatePlayer(Player player) {
      player.updateScore(player.getMines().size() * 2, "ATT " + display());
    }
  },
  GP_SCORING {
    @Override
    public String display() {
      return "+2/GP";
    }

    @Override
    public void updatePlayer(Player player) {
      player.updateScore(player.getGaiaPlanets().get() * 2, "ATT " + display());
    }
  },
  FED_SCORING {
    @Override
    public String display() {
      return "+5/FED";
    }

    @Override
    public void updatePlayer(Player player) {
      player.updateScore(player.getFederationTiles().size() * 5, "ATT " + display());
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
    public void endOfRound(Player player) {
      player.updateScore(player.getFederationTiles().size() * 3, "ATT " + display());
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
    public void endOfRound(Player player) {
      player.updateScore(player.getResearchLabs().size() * 3, "ATT " + display());
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
    public void endOfRound(Player player) {
      player.updateScore(player.getBuiltOn().size(), "ATT " + display());
    }
  },
  BUILD_MINE {
    @Override
    public String display() {
      return "M -> 3";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getMines().addListener((SetChangeListener<String>) change -> {
        if (change.wasAdded()) {
          player.updateScore(3, "ATT " + display());
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
      return "TP -> 3";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getTradingPosts().addListener((SetChangeListener<String>) change -> {
        if (change.wasAdded()) {
          player.updateScore(3, "ATT " + display());
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
      return "TECH -> 2";
    }

    @Override
    public void updatePlayer(Player player) {
      player.getTerraformingLevel().addListener((o, oldValue, newValue) -> {
        player.updateScore(2, "ATT " + display());
      });
      player.getNavLevel().addListener((o, oldValue, newValue) -> {
        player.updateScore(2, "ATT " + display());
      });
      player.getAiLevel().addListener((o, oldValue, newValue) -> {
        player.updateScore(2, "ATT " + display());
      });
      player.getGaiaformingLevel().addListener((o, oldValue, newValue) -> {
        player.updateScore(2, "ATT " + display());
      });
      player.getEconLevel().addListener((o, oldValue, newValue) -> {
        player.updateScore(2, "ATT " + display());
      });
      player.getKnowledgeLevel().addListener((o, oldValue, newValue) -> {
        player.updateScore(2, "ATT " + display());
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

  public void endOfRound(Player player) {
    // Does nothing by default - override for end of round scoring tiles
  }

  public boolean addsListener() {
    return false;
  }
}
