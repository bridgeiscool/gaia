package gaia.project.game.model;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class BalTaksPlayerTest {
  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    BalTaksPlayer player = BalTaksPlayer.createNew(PlayerEnum.PLAYER1);
    player.setGaiaformersInGaiaBin(2);

    StringWriter writer = new StringWriter();
    JsonWriter json = JsonUtil.GSON.newJsonWriter(writer);
    player.write(json);

    JsonReader reader = JsonUtil.GSON.newJsonReader(new StringReader(writer.toString()));
    BalTaksPlayer reRead = (BalTaksPlayer) Player.read(BalTaksPlayer.empty(), reader);

    Assert.assertEquals(2, reRead.getGaiaformersInGaiaBin());
  }
}
