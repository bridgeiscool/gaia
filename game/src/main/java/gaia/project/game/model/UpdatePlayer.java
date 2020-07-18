package gaia.project.game.model;

import java.io.Serializable;

@FunctionalInterface
public interface UpdatePlayer extends Serializable {
  void updatePlayer(Player player);
}
