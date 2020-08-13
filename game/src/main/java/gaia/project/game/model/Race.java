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
  },
  GLEENS("Gleens", "yellowRace", Color.YELLOW, PlanetType.YELLOW) {
    @Override
    public int getStartingQic() {
      return 0;
    }

    @Override
    public int getStartingNavLevel() {
      return 1;
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
  },
  LANTIDS("Lantids", "blueRace", Color.BLUE, PlanetType.BLUE) {
    @Override
    public int getStartingCredits() {
      return 13;
    }

    @Override
    public int getStartingBin2() {
      return 2;
    }
  },
  HADSCH_HALLAS("Hadsch Hallas", "redRace", Color.RED, PlanetType.RED) {
    @Override
    public int getStartingCreditIncome() {
      return 3;
    }

    @Override
    public int getStartingEconLevel() {
      return 1;
    }
  },
  IVITS("Ivits", "redRace", Color.RED, PlanetType.RED) {
    @Override
    public int getStartingQicIncome() {
      return 1;
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

  public List<IncomeUpdater> getTpIncome() {
    return ImmutableList.of(new CreditIncome(3), new CreditIncome(4), new CreditIncome(4), new CreditIncome(5));
  }

  public List<IncomeUpdater> getRlIncome() {
    return ImmutableList.of(new ResearchIncome(1), new ResearchIncome(1), new ResearchIncome(1));
  }
}
