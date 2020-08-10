package gaia.project.game.board;

public enum Building {
  MINE(1), TRADING_POST(2), RESEARCH_LAB(2), PLANETARY_INSTITUTE(3), ACADEMY(3);

  private final int power;

  private Building(int power) {
    this.power = power;
  }

  public int getPower() {
    return power;
  }
}
