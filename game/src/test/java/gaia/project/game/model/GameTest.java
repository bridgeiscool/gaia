package gaia.project.game.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Test;

public class GameTest {
  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    Game game = Game.generateGame();
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(game);
      try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
          ObjectInputStream ois = new ObjectInputStream(bais)) {
        Game reRead = (Game) ois.readObject();

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
      }
    }
  }
}
