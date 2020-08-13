package gaia.project.game.model;

public class ResearchIncome implements IncomeUpdater {
  private static final long serialVersionUID = -3805302622280800571L;
  private final int research;

  public ResearchIncome(int research) {
    this.research = research;
  }

  @Override
  public void addTo(Income income) {
    Util.plus(income.getResearchIncome(), research);
  }

  @Override
  public void removeFrom(Income income) {
    Util.minus(income.getResearchIncome(), research);
  }
}
