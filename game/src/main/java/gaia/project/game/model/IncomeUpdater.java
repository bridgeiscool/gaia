package gaia.project.game.model;

import java.io.Serializable;

public interface IncomeUpdater extends Serializable {
  void addTo(Income income);

  void removeFrom(Income income);
}
