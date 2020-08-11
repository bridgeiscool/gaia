package gaia.project.game;

@FunctionalInterface
public interface CallBack {
  void call();

  public static final CallBack EMPTY = () -> {};
}
