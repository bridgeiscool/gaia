package gaia.project.game.board;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class GameBoardTest {
  @Test
  public void orderedBoardInvalid() {
    System.out.println("Test 1");
    List<SectorLocation> orderedValues = Arrays.asList(SectorLocation.values());
    Assert.assertFalse(new GameBoard(orderedValues).isValid());
  }

  @Test
  public void originalBoardValid() {
    System.out.println("Test 2");
    Assert.assertTrue(GameBoard.originalBoard().isValid());
  }

}
