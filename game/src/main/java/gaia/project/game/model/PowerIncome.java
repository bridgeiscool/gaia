package gaia.project.game.model;

public class PowerIncome implements IncomeUpdater {
  private static final long serialVersionUID = -5716098354377884486L;
  private final int power;

  public PowerIncome(int power) {
    this.power = power;
  }

  @Override
  public void addTo(Income income) {
    Util.plus(income.getChargeIncome(), power);
  }

  @Override
  public void removeFrom(Income income) {
    Util.minus(income.getChargeIncome(), power);
  }
}
