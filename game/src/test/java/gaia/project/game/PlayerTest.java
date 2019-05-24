package gaia.project.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
  private Player player;

  @Before
  public void before() {
    player = new Player(Race.GLEENS);
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
}
