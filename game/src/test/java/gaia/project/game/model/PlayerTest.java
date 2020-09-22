package gaia.project.game.model;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class PlayerTest {
  private Player player;

  @Before
  public void before() {
    player = Player.create(Race.XENOS, PlayerEnum.PLAYER1);
  }

  // Charge power tests (Gleens start with 0-4-2 in bins 1-2-3)
  @Test
  public void chargeBin1Only() {
    player.chargePower(1);
    Assert.assertEquals(1, player.getBin1().getValue().intValue());
    Assert.assertEquals(5, player.getBin2().getValue().intValue());
    Assert.assertEquals(0, player.getBin3().getValue().intValue());
  }

  @Test
  public void chargeAllBin1Exactly() {
    player.chargePower(2);
    Assert.assertEquals(0, player.getBin1().getValue().intValue());
    Assert.assertEquals(6, player.getBin2().getValue().intValue());
    Assert.assertEquals(0, player.getBin3().getValue().intValue());
  }

  @Test
  public void chargeBin1And2() {
    player.chargePower(3);
    Assert.assertEquals(0, player.getBin1().getValue().intValue());
    Assert.assertEquals(5, player.getBin2().getValue().intValue());
    Assert.assertEquals(1, player.getBin3().getValue().intValue());
  }

  @Test
  public void chargeBin2Only() {
    player.chargePower(4);
    player.chargePower(1);
    Assert.assertEquals(0, player.getBin1().getValue().intValue());
    Assert.assertEquals(3, player.getBin2().getValue().intValue());
    Assert.assertEquals(3, player.getBin3().getValue().intValue());
  }

  @Test
  public void chargeTooMany() {
    player.chargePower(9);
    Assert.assertEquals(0, player.getBin1().getValue().intValue());
    Assert.assertEquals(0, player.getBin2().getValue().intValue());
    Assert.assertEquals(6, player.getBin3().getValue().intValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void exceptionOnEmptyChargeCall() {
    player.chargePower(0);
  }

  @Test
  public void testSerialization() throws IOException {
    Player player = Player.create(Race.AMBAS, PlayerEnum.PLAYER1);
    StringWriter writer = new StringWriter();
    JsonWriter newJsonWriter = JsonUtil.GSON.newJsonWriter(writer);

    player.write(newJsonWriter);

    String text = writer.toString();

    JsonReader reader = JsonUtil.GSON.newJsonReader(new StringReader(writer.toString()));

    Player reRead = Player.read(Player.empty(), reader);

    // Juat testing one random thing for now.
    Assert.assertEquals(player.getAiLevel().get(), reRead.getAiLevel().get());
  }

  @Test
  public void testCoordsSerialization() throws IOException {
    Player player = Player.create(Race.AMBAS, PlayerEnum.PLAYER1);
    player.getMines().add(new Coords(1.0, -1.0));
    player.getMines().add(new Coords(2.0, -2.0));
    StringWriter writer = new StringWriter();
    JsonWriter newJsonWriter = JsonUtil.GSON.newJsonWriter(writer);

    player.write(newJsonWriter);

    String text = writer.toString();

    JsonReader reader = JsonUtil.GSON.newJsonReader(new StringReader(writer.toString()));

    Player reRead = Player.read(Player.empty(), reader);

    // Juat testing one random thing for now.
    Assert.assertTrue(reRead.getMines().contains(new Coords(1.0, -1.0)));
    Assert.assertTrue(reRead.getMines().contains(new Coords(2.0, -2.0)));
  }
}
