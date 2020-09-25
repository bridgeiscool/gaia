package gaia.project.game.model;

/**
 * Bean to hold game options.
 */
public final class GameOpts {
  private final boolean balTaksBuff;
  private final Integer numPlayers;
  private final SetupEnum setup;
  private final boolean randomBoard;
  private final long seed;

  public GameOpts(boolean balTaksBuff, Integer numPlayers, SetupEnum setup, boolean randomBoard) {
    this.balTaksBuff = balTaksBuff;
    this.numPlayers = numPlayers;
    this.setup = setup;
    this.randomBoard = randomBoard;
    this.seed = System.currentTimeMillis();
  }

  public GameOpts(boolean balTaksBuff, Integer numPlayers, SetupEnum setup, boolean randomBoard, long seed) {
    this.balTaksBuff = balTaksBuff;
    this.numPlayers = numPlayers;
    this.setup = setup;
    this.randomBoard = randomBoard;
    this.seed = seed;
  }

  public boolean isBalTaksBuff() {
    return balTaksBuff;
  }

  public Integer getNumPlayers() {
    return numPlayers;
  }

  public SetupEnum getSetup() {
    return setup;
  }

  public boolean isRandomBoard() {
    return randomBoard;
  }

  public long getSeed() {
    return seed;
  }
}
