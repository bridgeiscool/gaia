package gaia.project.game.model;

import javafx.scene.paint.Color;

public enum Race {
  XENOS("Xenos", "yellowRace", Color.YELLOW, Color.BLACK) {
    @Override
    public int getStartingQic() {
      return 2;
    }
  },
  GLEENS("Gleens", "yellowRace", Color.YELLOW, Color.BLACK) {
    @Override
    public int getStartingQic() {
      return 0;
    }

    @Override
    public int getStartingOre() {
      return 5;
    }
  },
  TERRANS("Terrans", "blueRace", Color.BLUE, Color.WHITE) {
    @Override
    public int getStartingBin1() {
      return 4;
    }

    @Override
    public int getStartingGaiaformers() {
      return 1;
    }
  },
  LANTIDS("Lantids", "blueRace", Color.BLUE, Color.WHITE) {
    @Override
    public int getStartingCredits() {
      return 13;
    }

    @Override
    public int getStartingBin2() {
      return 2;
    }
  },
  HADSCH_HALLAS("Hadsch Hallas", "redRace", Color.RED, Color.WHITE) {
    @Override
    public int getStartingCreditIncome() {
      return 3;
    }
  },
  IVITS("Ivits", "redRace", Color.RED, Color.WHITE) {
    @Override
    public int getStartingQicIncome() {
      return 1;
    }
  };

  private final String raceName;
  private final String boardStyle;
  private final Color color;
  private final Color textColor;

  private Race(String raceName, String boardStyle, Color color, Color textColor) {
    this.raceName = raceName;
    this.boardStyle = boardStyle;
    this.color = color;
    this.textColor = textColor;
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

  public Color getTextColor() {
    return textColor;
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

  public int getStartingGaiaformers() {
    return 0;
  }
}
