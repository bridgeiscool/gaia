package gaia.project.game.model;

public class QicIncome implements IncomeUpdater {
  private static final long serialVersionUID = 163523268436293585L;
  private final int qic;

  public QicIncome(int qic) {
    this.qic = qic;
  }

  @Override
  public void addTo(Income income) {
    Util.plus(income.getQicIncome(), qic);
  }

  @Override
  public void removeFrom(Income income) {
    Util.minus(income.getQicIncome(), qic);
  }
}
