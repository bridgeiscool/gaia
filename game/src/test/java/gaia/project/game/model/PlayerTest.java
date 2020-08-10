package gaia.project.game.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
  private Player player;

  @Before
  public void before() {
    player = new Player(Race.GLEENS, PlayerEnum.PLAYER1);
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
  public void testSerialization() throws IOException, ClassNotFoundException {
    Player player = new Player(Race.TERRANS, PlayerEnum.PLAYER1);
    player.getMines().add(new Coords(0.0, 0.1));
    player.setRoundBooster(RoundBooster.BIGS);
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(player);
      try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
          ObjectInputStream ois = new ObjectInputStream(bais)) {
        Player reRead = (Player) ois.readObject();

      }
    }
  }
}
