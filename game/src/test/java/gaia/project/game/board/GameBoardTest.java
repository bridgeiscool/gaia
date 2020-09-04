package gaia.project.game.board;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class GameBoardTest {
  @Test
  public void orderedBoardInvalid() {
    List<SectorLocation> orderedValues = Arrays.asList(SectorLocation.values());
    Assert.assertFalse(new GameBoard(orderedValues).isValid());
  }

  @Ignore
  @Test
  public void originalBoardValid() {
    Assert.assertTrue(GameBoard.originalBoard().isValid());
  }

}
