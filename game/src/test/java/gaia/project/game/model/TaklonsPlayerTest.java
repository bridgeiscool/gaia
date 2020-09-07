package gaia.project.game.model;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import gaia.project.game.model.TaklonsPlayer.Bin;

public class TaklonsPlayerTest {
  private static final Gson GSON = new Gson();

  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    TaklonsPlayer player = TaklonsPlayer.createNew(PlayerEnum.PLAYER1);
    player.getBrainStone().setValue(Bin.GAIA);

    StringWriter writer = new StringWriter();
    JsonWriter json = GSON.newJsonWriter(writer);
    player.write(json);

    JsonReader reader = GSON.newJsonReader(new StringReader(writer.toString()));

    TaklonsPlayer reRead = (TaklonsPlayer) Player.read(TaklonsPlayer.empty(), reader);

    Assert.assertEquals(Bin.GAIA, reRead.getBrainStone().getValue());
  }
}
