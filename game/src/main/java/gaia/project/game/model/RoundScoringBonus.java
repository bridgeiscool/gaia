package gaia.project.game.model;

public enum RoundScoringBonus {
  MINE("Mine -> 2"),
  MINE_GP3("GP Mine -> 3"),
  MINE_GP4("GP Mine -> 4"),
  TP_3("TP -> 3"),
  TP_4("TP -> 4"),
  DIG("DIG -> 2"),
  TECH("TECH -> 2"),
  FED("FED -> 5"),
  BB_1("BB -> 5"),
  BB_2("BB -> 5");

  private final String text;

  RoundScoringBonus(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
