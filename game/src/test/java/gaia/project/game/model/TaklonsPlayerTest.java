package gaia.project.game.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Test;

import gaia.project.game.model.TaklonsPlayer.Bin;

public class TaklonsPlayerTest {
  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    TaklonsPlayer player = new TaklonsPlayer(PlayerEnum.PLAYER1);
    player.getBrainStone().setValue(Bin.GAIA);
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(player);
      try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
          ObjectInputStream ois = new ObjectInputStream(bais)) {
        TaklonsPlayer reRead = (TaklonsPlayer) ois.readObject();
        Assert.assertEquals(Bin.GAIA, reRead.getBrainStone().getValue());
      }
    }
  }
}
