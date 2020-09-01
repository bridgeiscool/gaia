package gaia.project.game.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

import gaia.project.game.PlanetType;
import javafx.scene.paint.Color;

public enum Race {
  XENOS("Xenos", "yellowRace", Color.YELLOW, PlanetType.YELLOW) {
    @Override
    public int getStartingAiLevel() {
      return 1;
    }

    @Override
    public IncomeUpdater getPiIncome() {
      return new CompoundIncome(new PowerIncome(4), new QicIncome(1));
    }

    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new XenosPlayer(playerEnum);
    }
  },
  GLEENS("Gleens", "yellowRace", Color.YELLOW, PlanetType.YELLOW) {
    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new GleensPlayer(playerEnum);
    }

    @Override
    public int getStartingQic() {
      return 0;
    }

    @Override
    public int getStartingNavLevel() {
      return 1;
    }

    @Override
    public IncomeUpdater getPiIncome() {
      return new CompoundIncome(new PowerIncome(4), new OreIncome(1));
    }

    @Override
    public UpdatePlayer gaiaTerraformCost() {
      return new OneOre();
    }
  },
  TERRANS("Terrans", "blueRace", Color.BLUE, PlanetType.BLUE) {
    @Override
    public int getStartingBin1() {
      return 4;
    }

    @Override
    public int getStartingGaiaformingLevel() {
      return 1;
    }

    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new TerranPlayer(playerEnum);
    }
  },
  // LANTIDS("Lantids", "blueRace", Color.BLUE, PlanetType.BLUE) {
  // @Override
  // public int getStartingCredits() {
  // return 13;
  // }
  //
  // @Override
  // public int getStartingBin2() {
  // return 2;
  // }
  // },
  HADSCH_HALLAS("Hadsch Hallas", "redRace", Color.RED, PlanetType.RED) {
    @Override
    public int getStartingCreditIncome() {
      return 3;
    }

    @Override
    public int getStartingEconLevel() {
      return 1;
    }

    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      // HH have no player-level anything, only the conversions once PI is built
      return new Player(this, playerEnum);
    }
  },
  IVITS("Ivits", "redRace", Color.RED, PlanetType.RED) {
    @Override
    public int getStartingQicIncome() {
      return 1;
    }

    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new IvitsPlayer(playerEnum);
    }
  },
  GEODENS("Geodens", "orangeRace", Color.ORANGE, PlanetType.ORANGE) {

    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new GeodensPlayer(playerEnum);
    }

    @Override
    public int getStartingTerraformingLevel() {
      return 1;
    }
  },

  BALTAKS("Bal Taks", "orangeRace", Color.ORANGE, PlanetType.ORANGE) {
    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new BalTaksPlayer(playerEnum);
    }

    @Override
    public PlayerBoardAction getQaAction() {
      return PlayerBoardAction.GAIN_4C;
    }

    @Override
    public int getStartingGaiaformingLevel() {
      return 1;
    }

    @Override
    public int getStartingBin2() {
      return 2;
    }
  },

  ITARS("Itars", "whiteRace", Color.WHITE, PlanetType.WHITE) {
    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new ItarsPlayer(playerEnum);
    }

    @Override
    public int getStartingOre() {
      return 5;
    }

    @Override
    public int getStartingPtIncome() {
      return 1;
    }

    @Override
    public int getKaIncome() {
      return 3;
    }

    @Override
    public int getStartingBin1() {
      return 4;
    }
  },
  NEVLAS("Nevlas", "whiteRace", Color.WHITE, PlanetType.WHITE) {

    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new NevlasPlayer(playerEnum);
    }

    @Override
    public int getStartingKnowledgeLevel() {
      return 1;
    }

    @Override
    public int getStartingKnowledge() {
      return 2;
    }

    @Override
    public List<IncomeUpdater> getRlIncome() {
      return ImmutableList.of(new PowerIncome(2), new PowerIncome(2), new PowerIncome(2));
    }
  },

  FIRAKS("Firaks", "grayRace", Color.GRAY, PlanetType.GRAY) {

    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new FiraksPlayer(playerEnum);
    }

    @Override
    public int getStartingKnowledge() {
      return 2;
    }

    @Override
    public int getStartingOre() {
      return 3;
    }

    @Override
    public int getStartingResearchIncome() {
      return 2;
    }
  },
  BESCODS("Bescods", "grayRace", Color.GRAY, PlanetType.GRAY) {
    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new BescodsPlayer(playerEnum);
    }

    @Override
    public List<IncomeUpdater> getTpIncome() {
      return ImmutableList
          .of(new ResearchIncome(1), new ResearchIncome(1), new ResearchIncome(1), new ResearchIncome(1));
    }

    @Override
    public List<IncomeUpdater> getRlIncome() {
      return ImmutableList.of(new CreditIncome(3), new CreditIncome(4), new CreditIncome(5));
    }

    @Override
    public IncomeUpdater getPiIncome() {
      return new CompoundIncome(new PowerIncome(4), new TokenIncome(2));
    }

    @Override
    public int getStartingKnowledge() {
      return 1;
    }

    @Override
    public int getStartingResearchIncome() {
      return 0;
    }
  },

  AMBAS("Ambas", "brownRace", Color.TAN, PlanetType.BROWN) {
    @Override
    public Player getPlayer(PlayerEnum playerEnum) {
      return new AmbasPlayer(playerEnum);
    }

    @Override
    public int getStartingNavLevel() {
      return 1;
    }

    @Override
    public int getStartingOreIncome() {
      return 2;
    }

    @Override
    public IncomeUpdater getPiIncome() {
      return new CompoundIncome(new PowerIncome(4), new TokenIncome(2));
    }
  };

  private final String raceName;
  private final String boardStyle;
  private final Color color;
  private final PlanetType homePlanet;

  private Race(String raceName, String boardStyle, Color color, PlanetType homePlanet) {
    this.raceName = raceName;
    this.boardStyle = boardStyle;
    this.color = color;
    this.homePlanet = homePlanet;
  }

  public abstract Player getPlayer(PlayerEnum playerEnum);

  public String getRaceName() {
    return raceName;
  }

  public String getBoardStyle() {
    return boardStyle;
  }

  public Color getColor() {
    return color;
  }

  public PlanetType getHomePlanet() {
    return homePlanet;
  }

  public int getStartingOre() {
    return 4;
  }

  public int getStartingCredits() {
    return 15;
  }

  public int getStartingKnowledge() {
    return 3;
  }

  public int getStartingQic() {
    return 1;
  }

  public int getStartingBin1() {
    return 2;
  }

  public int getStartingBin2() {
    return 4;
  }

  public int getStartingBin3() {
    return 0;
  }

  public int getStartingOreIncome() {
    return 1;
  }

  public int getStartingCreditIncome() {
    return 0;
  }

  public int getStartingResearchIncome() {
    return 1;
  }

  public int getStartingPtIncome() {
    return 0;
  }

  public int getStartingQicIncome() {
    return 0;
  }

  public int getStartingTerraformingLevel() {
    return 0;
  }

  public int getStartingNavLevel() {
    return 0;
  }

  public int getStartingAiLevel() {
    return 0;
  }

  public int getStartingGaiaformingLevel() {
    return 0;
  }

  public int getStartingEconLevel() {
    return 0;
  }

  public int getStartingKnowledgeLevel() {
    return 0;
  }

  public UpdatePlayer gaiaTerraformCost() {
    return new OneQic();
  }

  public PlayerBoardAction getQaAction() {
    return PlayerBoardAction.GAIN_QIC;
  }

  public List<IncomeUpdater> getTpIncome() {
    return ImmutableList.of(new CreditIncome(3), new CreditIncome(4), new CreditIncome(4), new CreditIncome(5));
  }

  public List<IncomeUpdater> getRlIncome() {
    return ImmutableList.of(new ResearchIncome(1), new ResearchIncome(1), new ResearchIncome(1));
  }

  public IncomeUpdater getPiIncome() {
    return new CompoundIncome(new PowerIncome(4), new TokenIncome(1));
  }

  public int getKaIncome() {
    return 2;
  }

  private static class OneQic implements UpdatePlayer {
    private static final long serialVersionUID = -304948874042749436L;

    @Override
    public void updatePlayer(Player player) {
      Util.minus(player.getQic(), 1);
    }
  }

  private static class OneOre implements UpdatePlayer {
    private static final long serialVersionUID = 4405543174181017949L;

    @Override
    public void updatePlayer(Player player) {
      Util.minus(player.getOre(), 1);
    }
  }
}
