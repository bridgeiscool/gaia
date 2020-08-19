package gaia.project.game.board;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Test;

import gaia.project.game.model.Income;
import gaia.project.game.model.Race;

public class IncomeTest {
  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    Income income = new Income(Race.XENOS);
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(income);
      try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
          ObjectInputStream ois = new ObjectInputStream(bais)) {
        Income reRead = (Income) ois.readObject();

        Assert.assertEquals(income.getCreditIncome().getValue(), reRead.getCreditIncome().getValue());
        Assert.assertEquals(income.getOreIncome().getValue(), reRead.getOreIncome().getValue());
        Assert.assertEquals(income.getResearchIncome().getValue(), reRead.getResearchIncome().getValue());
        Assert.assertEquals(income.getQicIncome().getValue(), reRead.getQicIncome().getValue());
        Assert.assertEquals(income.getPowerIncome().getValue(), reRead.getPowerIncome().getValue());
        Assert.assertEquals(income.getChargeIncome().getValue(), reRead.getChargeIncome().getValue());
      }
    }
  }
}
