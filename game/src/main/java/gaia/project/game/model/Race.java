package gaia.project.game.model;

import javafx.scene.paint.Color;

public enum Race {
  XENOS("Xenos", "yellowRace", Color.YELLOW, Color.BLACK, 3, 15, 3, 1, 2, 4, 0, 1, 0, 1),
  GLEENS("Gleens", "yellowRace", Color.YELLOW, Color.BLACK, 3, 15, 3, 1, 2, 4, 0, 1, 0, 1);

  private final String raceName;
  private final String boardStyle;
  private final Color color;
  private final Color textColor;

  private final int startingOre;
  private final int startingCredits;
  private final int startingResearch;
  private final int startingQic;

  private final int startingBin1;
  private final int startingBin2;
  private final int startingBin3;

  // Initial income
  private final int startingOreIncome;
  private final int startingCreditIncome;
  private final int startingResearchIncome;

  private Race(
      String raceName,
      String boardStyle,
      Color color,
      Color textColor,
      int startingOre,
      int startingCredits,
      int startingResearch,
      int startingQic,
      int startingBin1,
      int startingBin2,
      int startingBin3,
      int startingOreIncome,
      int startingCreditIncome,
      int startingResearchIncome) {
    this.raceName = raceName;
    this.boardStyle = boardStyle;
    this.color = color;
    this.textColor = textColor;
    this.startingOre = startingOre;
    this.startingCredits = startingCredits;
    this.startingResearch = startingResearch;
    this.startingQic = startingQic;
    this.startingBin1 = startingBin1;
    this.startingBin2 = startingBin2;
    this.startingBin3 = startingBin3;
    this.startingOreIncome = startingOreIncome;
    this.startingCreditIncome = startingCreditIncome;
    this.startingResearchIncome = startingResearchIncome;
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
    return startingOre;
  }

  public int getStartingCredits() {
    return startingCredits;
  }

  public int getStartingResearch() {
    return startingResearch;
  }

  public int getStartingQic() {
    return startingQic;
  }

  public int getStartingBin1() {
    return startingBin1;
  }

  public int getStartingBin2() {
    return startingBin2;
  }

  public int getStartingBin3() {
    return startingBin3;
  }

  public int getStartingOreIncome() {
    return startingOreIncome;
  }

  public int getStartingCreditIncome() {
    return startingCreditIncome;
  }

  public int getStartingResearchIncome() {
    return startingResearchIncome;
  }

}
