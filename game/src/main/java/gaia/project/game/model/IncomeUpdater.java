package gaia.project.game.model;

import java.io.Serializable;

public interface IncomeUpdater extends Serializable {
  void update(Income income);
}
