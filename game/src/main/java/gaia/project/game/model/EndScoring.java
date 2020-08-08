package gaia.project.game.model;

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
  },
  BUILDINGS("Bldgs") {
    @Override
    public void bindToPlayer(Player player, Label label) {
      label.textProperty().bindBidirectional(player.getTotalBuildings(), new NumberStringConverter());
    }
  },
  BUILDINGS_IN_FED("Fed Bldgs") {
    @Override
    public void bindToPlayer(Player player, Label label) {
      label.textProperty().bindBidirectional(player.getBuildingsInFeds(), new NumberStringConverter());
    }
  },
  GAIA_PLANETS("GPs") {
    @Override
    public void bindToPlayer(Player player, Label label) {
      label.textProperty().bindBidirectional(player.getGaiaPlanets(), new NumberStringConverter());
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
  },
  SECTORS("Sectors") {
    @Override
    public void bindToPlayer(Player player, Label label) {
      label.setText(String.valueOf(player.getSectors().size()));
      player.getSectors()
          .addListener(
              (SetChangeListener<Integer>) change -> label.setText(String.valueOf(player.getSectors().size())));
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
}
