package gaia.project.game.model;

public enum Round {
  SETUP("Setup"), ROUND1("1"), ROUND2("2"), ROUND3("3"), ROUND4("4"), ROUND5("5"), ROUND6("6");

  private final String display;

  private Round(String display) {
    this.display = display;
  }

  public Round nextRound() {
    return values()[ordinal() + 1];
  }

  public String display() {
    return display;
  }
}
