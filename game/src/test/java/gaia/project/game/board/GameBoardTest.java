package gaia.project.game.board;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class GameBoardTest {
  @Test
  public void orderedBoardInvalid() {
    Assert.assertFalse(new GameBoard(Arrays.asList(SectorLocation.values())).isValid());
  }

  @Test
  public void originalBoardValid() {
    Assert.assertTrue(GameBoard.originalBoard().isValid());
  }

}
