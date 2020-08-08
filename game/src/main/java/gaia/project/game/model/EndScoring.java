package gaia.project.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import gaia.project.game.PlanetType;
import gaia.project.game.board.Satellite;
import javafx.collections.SetChangeListener;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;

public enum EndScoring {
  PLANET_TYPES("Planets") {
    public void bindToPlayer(Player player, Label label) {
      label.setText(String.valueOf(player.getBuiltOn().size()));
      player.getBuiltOn()
          .addListener(
              (SetChangeListener<PlanetType>) change -> label.setText(String.valueOf(player.getBuiltOn().size())));
    };

    @Override
    public Function<Player, Integer> getCategoryScore() {
      return p -> p.getBuiltOn().size();
    }
  },
  BUILDINGS("Bldgs") {
    @Override
    public void bindToPlayer(Player player, Label label) {
      label.textProperty().bindBidirectional(player.getTotalBuildings(), new NumberStringConverter());
    }

    @Override
    public Function<Player, Integer> getCategoryScore() {
      return p -> p.getTotalBuildings().getValue();
    }
  },
  BUILDINGS_IN_FED("Fed Bldgs") {
    @Override
    public void bindToPlayer(Player player, Label label) {
      label.textProperty().bindBidirectional(player.getBuildingsInFeds(), new NumberStringConverter());
    }

    @Override
    public Function<Player, Integer> getCategoryScore() {
      return p -> p.getBuildingsInFeds().getValue();
    }
  },
  GAIA_PLANETS("GPs") {
    @Override
    public void bindToPlayer(Player player, Label label) {
      label.textProperty().bindBidirectional(player.getGaiaPlanets(), new NumberStringConverter());
    }

    @Override
    public Function<Player, Integer> getCategoryScore() {
      return p -> p.getGaiaPlanets().getValue();
    }
  },
  SATELLITES("Sats") {
    @Override
    public void bindToPlayer(Player player, Label label) {
      label.setText(String.valueOf(player.getSatellites().size()));
      player.getSatellites()
          .addListener(
              (SetChangeListener<Satellite>) change -> label.setText(String.valueOf(player.getSatellites().size())));
    }

    @Override
    public Function<Player, Integer> getCategoryScore() {
      return p -> p.getSatellites().size();
    }
  },
  SECTORS("Sectors") {
    @Override
    public void bindToPlayer(Player player, Label label) {
      label.setText(String.valueOf(player.getSectors().size()));
      player.getSectors()
          .addListener(
              (SetChangeListener<Integer>) change -> label.setText(String.valueOf(player.getSectors().size())));
    }

    @Override
    public Function<Player, Integer> getCategoryScore() {
      return p -> p.getSectors().size();
    }
  };

  private final String displayText;

  private EndScoring(String displayText) {
    this.displayText = displayText;
  }

  public String getDisplayText() {
    return displayText;
  }

  public abstract void bindToPlayer(Player player, Label label);

  public abstract Function<Player, Integer> getCategoryScore();

  public void addToFinalScores(Collection<Player> players) {
    List<Player> sorted = new ArrayList<>(players);
    Collections.sort(sorted, Comparator.comparingInt(p -> getCategoryScore().apply(p)));
    Collections.reverse(sorted);

    // TODO: Update for 4Ps
    if (getCategoryScore().apply(sorted.get(0)) > getCategoryScore().apply(sorted.get(1))) {
      // Clean win for P1
      addToScore(sorted.get(0), 18);

      if (getCategoryScore().apply(sorted.get(1)) > getCategoryScore().apply(sorted.get(2))) {
        // Clean 2nd
        addToScore(sorted.get(1), 12);
        addToScore(sorted.get(2), 6);
      } else {
        // Tied for 2-3
        addToScore(sorted.get(1), 9);
        addToScore(sorted.get(2), 9);
      }

    } else {
      if (getCategoryScore().apply(sorted.get(1)) > getCategoryScore().apply(sorted.get(2))) {
        // 1-2 tie
        addToScore(sorted.get(0), 15);
        addToScore(sorted.get(1), 15);
        addToScore(sorted.get(2), 6);
      } else {
        // 3-way tie
        addToScore(sorted.get(0), 12);
        addToScore(sorted.get(1), 12);
        addToScore(sorted.get(2), 12);
      }
    }
  }

  private static void addToScore(Player player, int toAdd) {
    player.getScore().setValue(player.getScore().getValue() + toAdd);
  }
}
