package gaia.project.game.model;

// Handles race-specific special actions
public enum PlayerBoardAction implements UpdatePlayer {
  GAIN_QIC {
    @Override
    public String display() {
      return "+q";
    }

    @Override
    public void updatePlayer(Player player) {
      Util.plus(player.getQic(), 1);
    }
  },
  GAIN_4C {
    @Override
    public String display() {
      return "+4c";
    }

    @Override
    public void updatePlayer(Player player) {
      Util.plus(player.getCredits(), 4);
    }
  },

  RL_TO_TP {

    @Override
    public String display() {
      return "RL>TP";
    }

    @Override
    public void updatePlayer(Player player) {
      // Intentionally does nothing...
    }

  };

  public abstract String display();
}
