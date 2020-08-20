package gaia.project.game.model;

// Handles race-specific special actions
public enum PlayerBoardAction {
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
  RL_TO_TP {

    @Override
    public String display() {
      return "RL>TP";
    }

    @Override
    public void updatePlayer(Player player) {
      // TODO: Implement for Firaks
    }

  };

  public abstract void updatePlayer(Player player);

  public abstract String display();
}
