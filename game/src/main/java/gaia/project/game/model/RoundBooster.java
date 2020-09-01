package gaia.project.game.model;

public enum RoundBooster {
  DIG("TF", "(+2c)") {

    @Override
    public void addIncome(Income addTo) {
      Util.plus(addTo.getCreditIncome(), 2);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      Util.minus(removeFrom.getCreditIncome(), 2);
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getCurrentDigs(), 1);
    }
  },
  JUMP("NAV", "(+2p)") {
    @Override
    public void addIncome(Income addTo) {
      Util.plus(addTo.getChargeIncome(), 2);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      Util.minus(removeFrom.getChargeIncome(), 2);
    }

    @Override
    public boolean isAction() {
      return true;
    }

    @Override
    public void onAction(Player player) {
      Util.plus(player.getTempNavRange(), 3);
    }
  },
  ORE_K("(+1o)", "(+1k)") {

    @Override
    public void addIncome(Income addTo) {
      Util.plus(addTo.getOreIncome(), 1);
      Util.plus(addTo.getResearchIncome(), 1);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      Util.minus(removeFrom.getOreIncome(), 1);
      Util.minus(removeFrom.getResearchIncome(), 1);
    }

  },
  Q_C("(+1q)", "(+2c)") {

    @Override
    public void addIncome(Income addTo) {
      Util.plus(addTo.getQicIncome(), 1);
      Util.plus(addTo.getCreditIncome(), 2);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      Util.minus(removeFrom.getQicIncome(), 1);
      Util.minus(removeFrom.getCreditIncome(), 2);
    }

  },
  ORE_PT("(+1o)", "(+2pt)") {

    @Override
    public void addIncome(Income addTo) {
      Util.plus(addTo.getOreIncome(), 1);
      Util.plus(addTo.getPowerIncome(), 2);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      Util.minus(removeFrom.getOreIncome(), 1);
      Util.minus(removeFrom.getPowerIncome(), 2);
    }

  },
  GAIA_PLANETS("[]: GP", "(+4c)") {
    @Override
    public void addIncome(Income addTo) {
      Util.plus(addTo.getCreditIncome(), 4);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      Util.minus(removeFrom.getCreditIncome(), 4);
    }

    @Override
    public void addVps(Player player) {
      player.updateScore(player.getGaiaPlanets().getValue(), "RB " + getTopText());
    }
  },
  MINES("[]:M", "(+1o)") {

    @Override
    public void addIncome(Income addTo) {
      Util.plus(addTo.getOreIncome(), 1);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      Util.minus(removeFrom.getOreIncome(), 1);
    }

    @Override
    public void addVps(Player player) {
      player.updateScore(player.getMines().size(), "RB " + getTopText());
    }
  },
  TPS("[]:TP", "(+1o)") {
    @Override
    public void addIncome(Income addTo) {
      Util.plus(addTo.getOreIncome(), 1);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      Util.minus(removeFrom.getOreIncome(), 1);
    }

    @Override
    public void addVps(Player player) {
      player.updateScore(2 * player.getTradingPosts().size(), "RB " + getTopText());
    }
  },
  RLS("[]:RL", "(+1k)") {

    @Override
    public void addIncome(Income addTo) {
      Util.plus(addTo.getResearchIncome(), 1);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      Util.minus(removeFrom.getResearchIncome(), 1);
    }

    @Override
    public void addVps(Player player) {
      player.updateScore(3 * player.getResearchLabs().size(), "RB " + getTopText());
    }
  },
  BIGS("[]:BB", "(+4p)") {
    @Override
    public void addIncome(Income addTo) {
      Util.plus(addTo.getChargeIncome(), 4);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      Util.minus(removeFrom.getChargeIncome(), 4);
    }

    @Override
    public void addVps(Player player) {
      player.updateScore(
          4 * (player.getPi().size() + player.getKa().size() + player.getQa().size()),
          "RB " + getTopText());
    }
  };

  private final String topText;
  private final String bottomText;

  private RoundBooster(String topText, String bottomText) {
    this.topText = topText;
    this.bottomText = bottomText;
  }

  public String getTopText() {
    return topText;
  }

  public String getBottomText() {
    return bottomText;
  }

  public abstract void addIncome(Income addTo);

  public abstract void removeIncome(Income removeFrom);

  public void addVps(Player player) {
    // By default does nothing...
  }

  public boolean isAction() {
    return false;
  }

  public void onAction(Player player) {
    // Does nothing by default - override for actions
  }
}
