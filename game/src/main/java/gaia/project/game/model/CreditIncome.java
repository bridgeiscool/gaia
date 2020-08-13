package gaia.project.game.model;

public class CreditIncome implements IncomeUpdater {
  private static final long serialVersionUID = -9214113050735998972L;
  private final int credits;

  public CreditIncome(int credits) {
    this.credits = credits;
  }

  @Override
  public void addTo(Income income) {
    Util.plus(income.getCreditIncome(), credits);
  }

  @Override
  public void removeFrom(Income income) {
    Util.minus(income.getChargeIncome(), credits);
  }
}
