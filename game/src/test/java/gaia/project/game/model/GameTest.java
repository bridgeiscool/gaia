package gaia.project.game.model;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GameTest {
  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    Game game = Game.generateGame();

    StringWriter writer = new StringWriter();
    JsonWriter newJsonWriter = JsonUtil.GSON.newJsonWriter(writer);

    game.write(newJsonWriter);

    String text = writer.toString();

    JsonReader reader = JsonUtil.GSON.newJsonReader(new StringReader(text));

    Game reRead = Game.read(reader);

    Assert.assertEquals(game.getAdvancedTechTiles(), reRead.getAdvancedTechTiles());
    Assert.assertEquals(game.getCurrentPlayerOrder(), reRead.getCurrentPlayerOrder());
    Assert.assertEquals(game.getCurrentRound().getValue(), reRead.getCurrentRound().getValue());
    Assert.assertEquals(game.getEndScoring1(), reRead.getEndScoring1());
    Assert.assertEquals(game.getEndScoring2(), reRead.getEndScoring2());
    Assert.assertEquals(game.getGameBoard(), reRead.getGameBoard());
    Assert.assertEquals(game.getPassedPlayers(), reRead.getPassedPlayers());
    Assert.assertEquals(game.getRoundBoosters(), reRead.getRoundBoosters());
    Assert.assertEquals(game.getRoundScoringBonuses(), reRead.getRoundScoringBonuses());
    Assert.assertEquals(game.getTechTiles(), reRead.getTechTiles());
    Assert.assertEquals(game.getTerraBonus(), reRead.getTerraBonus());

    Assert.assertEquals(game.getK3ActionTaken().getValue(), reRead.getK3ActionTaken().getValue());
    Assert.assertEquals(game.getDoubleTfActionTaken().getValue(), reRead.getDoubleTfActionTaken().getValue());
    Assert.assertEquals(game.getCreditsActionTaken().getValue(), reRead.getCreditsActionTaken().getValue());
    Assert.assertEquals(game.getOreActionTaken().getValue(), reRead.getOreActionTaken().getValue());
    Assert.assertEquals(game.getK2ActionTaken().getValue(), reRead.getK2ActionTaken().getValue());
    Assert.assertEquals(game.getTfActionTaken().getValue(), reRead.getTfActionTaken().getValue());
    Assert.assertEquals(game.getPtActionTaken().getValue(), reRead.getPtActionTaken().getValue());
    Assert.assertEquals(game.getQ2ActionTaken().getValue(), reRead.getQ2ActionTaken().getValue());
    Assert.assertEquals(game.getQ3ActionTaken().getValue(), reRead.getQ3ActionTaken().getValue());
    Assert.assertEquals(game.getQ4ActionTaken().getValue(), reRead.getQ4ActionTaken().getValue());
  }
}
