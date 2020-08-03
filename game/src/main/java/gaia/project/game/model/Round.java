package gaia.project.game.model;

public enum Round {
  SETUP, ROUND1, ROUND2, ROUND3, ROUND4, ROUND5, ROUND6;

  public Round nextRound() {
    return values()[ordinal() + 1];
  }
}
