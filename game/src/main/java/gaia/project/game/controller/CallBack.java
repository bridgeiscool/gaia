package gaia.project.game.controller;

@FunctionalInterface
public interface CallBack {
  void call();

  public static final CallBack EMPTY = () -> {};
}
