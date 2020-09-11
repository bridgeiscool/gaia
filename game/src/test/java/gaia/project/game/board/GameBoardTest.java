package gaia.project.game.board;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class GameBoardTest {
  @Test
  public void orderedBoardInvalid() {

    LinkedHashMap<SectorLocation, Rot> orderedValues = Arrays.stream(SectorLocation.values())
        .collect(Collectors.toMap(sl -> sl, sl -> Rot.POS_1, (a, b) -> a, LinkedHashMap::new));

    Assert.assertFalse(new GameBoard(orderedValues).isValid());
  }

  @Ignore
  @Test
  public void originalBoardValid() {
    Assert.assertTrue(GameBoard.originalBoard().isValid());
  }

}
