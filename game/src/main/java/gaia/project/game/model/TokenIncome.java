package gaia.project.game.model;

public class TokenIncome implements IncomeUpdater {
  private static final long serialVersionUID = 9037246823625490344L;
  private final int pt;

  public TokenIncome(int pt) {
    this.pt = pt;
  }

  @Override
  public void addTo(Income income) {
    Util.plus(income.getPowerIncome(), pt);
  }

  @Override
  public void removeFrom(Income income) {
    Util.minus(income.getPowerIncome(), pt);
  }
}
