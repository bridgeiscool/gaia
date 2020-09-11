package gaia.project.game.model;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class IncomeTest {
  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    Income income = Income.fromRace(Race.XENOS);
    StringWriter writer = new StringWriter();
    JsonWriter json = JsonUtil.GSON.newJsonWriter(writer);
    income.write(json);

    JsonReader reader = JsonUtil.GSON.newJsonReader(new StringReader(writer.toString()));

    Income reRead = Income.read(reader);

    Assert.assertEquals(income.getCreditIncome().getValue(), reRead.getCreditIncome().getValue());
    Assert.assertEquals(income.getOreIncome().getValue(), reRead.getOreIncome().getValue());
    Assert.assertEquals(income.getResearchIncome().getValue(), reRead.getResearchIncome().getValue());
    Assert.assertEquals(income.getQicIncome().getValue(), reRead.getQicIncome().getValue());
    Assert.assertEquals(income.getPowerIncome().getValue(), reRead.getPowerIncome().getValue());
    Assert.assertEquals(income.getChargeIncome().getValue(), reRead.getChargeIncome().getValue());
  }
}
