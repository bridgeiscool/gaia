package gaia.project.game.model;

public enum RoundBooster {
  DIG("+1T", "(+2c)") {

    @Override
    public void addIncome(Income addTo) {
      addTo.getCreditIncome().setValue(addTo.getCreditIncome().getValue() + 2);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      removeFrom.getCreditIncome().setValue(removeFrom.getCreditIncome().getValue() - 2);
    }

  },
  JUMP("+3 NAV", "(+2p)") {
    @Override
    public void addIncome(Income addTo) {
      addTo.getChargeIncome().setValue(addTo.getChargeIncome().getValue() + 2);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      removeFrom.getChargeIncome().setValue(removeFrom.getChargeIncome().getValue() - 2);
    }
  },
  ORE_K("(+1o)", "(+1k)") {

    @Override
    public void addIncome(Income addTo) {
      addTo.getOreIncome().setValue(addTo.getOreIncome().getValue() + 1);
      addTo.getResearchIncome().setValue(addTo.getResearchIncome().getValue() + 1);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      removeFrom.getOreIncome().setValue(removeFrom.getOreIncome().getValue() - 1);
      removeFrom.getResearchIncome().setValue(removeFrom.getResearchIncome().getValue() - 1);
    }

  },
  Q_C("(+1q)", "(+2c)") {

    @Override
    public void addIncome(Income addTo) {
      addTo.getQicIncome().setValue(addTo.getQicIncome().getValue() + 1);
      addTo.getCreditIncome().setValue(addTo.getCreditIncome().getValue() + 2);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      removeFrom.getQicIncome().setValue(removeFrom.getQicIncome().getValue() - 1);
      removeFrom.getCreditIncome().setValue(removeFrom.getCreditIncome().getValue() - 2);
    }

  },
  ORE_PT("(+1o)", "(+2pt)") {

    @Override
    public void addIncome(Income addTo) {
      addTo.getOreIncome().setValue(addTo.getOreIncome().getValue() + 1);
      addTo.getPowerIncome().setValue(addTo.getPowerIncome().getValue() + 2);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      removeFrom.getOreIncome().setValue(removeFrom.getOreIncome().getValue() - 1);
      removeFrom.getPowerIncome().setValue(removeFrom.getPowerIncome().getValue() - 2);
    }

  },
  GAIA_PLANETS("[]: GP", "(+4c)") {
    @Override
    public void addIncome(Income addTo) {
      addTo.getCreditIncome().setValue(addTo.getCreditIncome().getValue() + 4);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      removeFrom.getCreditIncome().setValue(removeFrom.getCreditIncome().getValue() - 4);
    }

    @Override
    public void addVps(Player player) {
      player.getScore().setValue(player.getScore().getValue() + player.getGaiaPlanets().getValue());
    }
  },
  MINES("[]:M", "(+1o)") {

    @Override
    public void addIncome(Income addTo) {
      addTo.getOreIncome().setValue(addTo.getOreIncome().getValue() + 1);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      removeFrom.getOreIncome().setValue(removeFrom.getOreIncome().getValue() - 1);
    }

    @Override
    public void addVps(Player player) {
      player.getScore().setValue(player.getScore().getValue() + player.getMines().size());
    }
  },
  TPS("[]:TP", "(+1o)") {
    @Override
    public void addIncome(Income addTo) {
      addTo.getOreIncome().setValue(addTo.getOreIncome().getValue() + 1);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      removeFrom.getOreIncome().setValue(removeFrom.getOreIncome().getValue() - 1);
    }

    @Override
    public void addVps(Player player) {
      player.getScore().setValue(player.getScore().getValue() + 2 * player.getTradingPosts().size());
    }
  },
  RLS("[]:RL", "(+1k)") {

    @Override
    public void addIncome(Income addTo) {
      addTo.getResearchIncome().setValue(addTo.getResearchIncome().getValue() + 1);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      removeFrom.getResearchIncome().setValue(removeFrom.getResearchIncome().getValue() - 1);
    }

    @Override
    public void addVps(Player player) {
      player.getScore().setValue(player.getScore().getValue() + 3 * player.getResearchLabs().size());
    }
  },
  BIGS("[]:BB", "(+4p)") {
    @Override
    public void addIncome(Income addTo) {
      addTo.getChargeIncome().setValue(addTo.getChargeIncome().getValue() + 4);
    }

    @Override
    public void removeIncome(Income removeFrom) {
      removeFrom.getChargeIncome().setValue(removeFrom.getChargeIncome().getValue() - 4);
    }

    @Override
    public void addVps(Player player) {
      player.getScore()
          .setValue(
              player.getScore().getValue()
                  + 4 * (player.getPi().size() + player.getKa().size() + player.getQa().size()));
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
}
