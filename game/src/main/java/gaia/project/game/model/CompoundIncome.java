package gaia.project.game.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class CompoundIncome implements IncomeUpdater {
  private static final long serialVersionUID = 2727657735059260017L;
  public final List<IncomeUpdater> updaters;

  public CompoundIncome(IncomeUpdater... incomeUpdaters) {
    updaters = ImmutableList.copyOf(incomeUpdaters);
  }

  @Override
  public void addTo(Income income) {
    updaters.forEach(iu -> iu.addTo(income));
  }

  @Override
  public void removeFrom(Income income) {
    updaters.forEach(iu -> iu.removeFrom(income));
  }

}
