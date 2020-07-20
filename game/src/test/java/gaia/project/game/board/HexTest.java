package gaia.project.game.board;

import static gaia.project.game.board.BoardUtils.HEX_SIZE;
import static gaia.project.game.board.BoardUtils.ROOT_3;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Iterables;

public class HexTest {
  @Test
  public void adjacentHex() {
    Hex reference = Hex.emptyHex(0, 0, 1);
    Hex adjacent = Hex.emptyHex(3.0 * HEX_SIZE, ROOT_3 * HEX_SIZE, 2);
    Assert.assertEquals(
        adjacent,
        Iterables.getOnlyElement(reference.getHexesWithinRange(Arrays.asList(reference, adjacent), 1)));
  }

  @Test
  public void twoAwayFails() {
    Hex reference = Hex.emptyHex(0, 0, 1);
    Hex adjacent = Hex.emptyHex(6.0 * HEX_SIZE, 2 * ROOT_3 * HEX_SIZE, 2);
    Assert.assertTrue(reference.getHexesWithinRange(Arrays.asList(reference, adjacent), 1).isEmpty());
  }
}
