package gaia.project.game.model;

public class OreIncome implements IncomeUpdater {
  private static final long serialVersionUID = -4916904735424681891L;
  private final int ore;

  public OreIncome(int ore) {
    this.ore = ore;
  }

  @Override
  public void addTo(Income income) {
    Util.plus(income.getOreIncome(), ore);
  }

  @Override
  public void removeFrom(Income income) {
    Util.minus(income.getOreIncome(), ore);
  }
}
