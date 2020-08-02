package gaia.project.game.model;

public enum PlayerEnum {
  PLAYER1(0), PLAYER2(1), PLAYER3(2);

  private final int idx;

  private PlayerEnum(int idx) {
    this.idx = idx;
  }

  public int getIdx() {
    return idx;
  }
}
