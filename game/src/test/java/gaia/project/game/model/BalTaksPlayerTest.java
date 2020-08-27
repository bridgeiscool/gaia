package gaia.project.game.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Test;

public class BalTaksPlayerTest {
  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    BalTaksPlayer player = new BalTaksPlayer(PlayerEnum.PLAYER1);
    player.setGaiaformersInGaiaBin(2);
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(player);
      try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
          ObjectInputStream ois = new ObjectInputStream(bais)) {
        BalTaksPlayer reRead = (BalTaksPlayer) ois.readObject();
        Assert.assertEquals(2, reRead.getGaiaformersInGaiaBin());
      }
    }
  }
}
