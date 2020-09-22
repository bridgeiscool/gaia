package gaia.project.game.model;

/**
 * Bean to hold game options.
 */
public final class GameOpts {
  private final boolean balTaksBuff;
  private final Integer numPlayers;
  private final SetupEnum setup;
  private final long seed;

  public GameOpts(boolean balTaksBuff, Integer numPlayers, SetupEnum setup) {
    this.balTaksBuff = balTaksBuff;
    this.numPlayers = numPlayers;
    this.setup = setup;
    this.seed = System.currentTimeMillis();
  }

  public GameOpts(boolean balTaksBuff, Integer numPlayers, SetupEnum setup, long seed) {
    this.balTaksBuff = balTaksBuff;
    this.numPlayers = numPlayers;
    this.setup = setup;
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

  public long getSeed() {
    return seed;
  }
}
