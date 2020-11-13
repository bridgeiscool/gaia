package gaia.project.game.model;

import gaia.project.game.model.Player.FedToken;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public enum RoundScoringBonus {
  MINE("M -> 2") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getMines().addListener((SetChangeListener<String>) change -> {
        if (game.getCurrentRound().getValue() == round && change.wasAdded()) {
          p.updateScore(2, "R" + game.getCurrentRound().getValue().display() + " " + getText());
        }
      }));
      game.getPlayers().values().forEach(p -> p.getLostPlanet().addListener((SetChangeListener<String>) change -> {
        if (game.getCurrentRound().getValue() == round && change.wasAdded()) {
          p.updateScore(2, "R" + game.getCurrentRound().getValue().display() + " " + getText());
        }
      }));
    }
  },
  MINE_GP3("GP M -> 3") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getGaiaPlanets().addListener((o, oldValue, newValue) -> {
        if (game.getCurrentRound().getValue() == round && newValue.intValue() > oldValue.intValue()) {
          p.updateScore(3, "R" + game.getCurrentRound().getValue().display() + " " + getText());
        }
      }));
    }
  },
  MINE_GP4("GP M -> 4") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getGaiaPlanets().addListener((o, oldValue, newValue) -> {
        if (game.getCurrentRound().getValue() == round && newValue.intValue() > oldValue.intValue()) {
          p.updateScore(4, "R" + game.getCurrentRound().getValue().display() + " " + getText());
        }
      }));
    }
  },
  TP_3("TP -> 3") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getTradingPosts().addListener((SetChangeListener<String>) change -> {
        if (game.getCurrentRound().getValue() == round && change.wasAdded() && !p.ignoreTpRoundBonus()) {
          p.updateScore(3, "R" + game.getCurrentRound().getValue().display() + " " + getText());
        }
      }));
    }
  },
  TP_4("TP -> 4") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getTradingPosts().addListener((SetChangeListener<String>) change -> {
        if (game.getCurrentRound().getValue() == round && change.wasAdded() && !p.ignoreTpRoundBonus()) {
          p.updateScore(4, "R" + game.getCurrentRound().getValue().display() + " " + getText());
        }
      }));
    }
  },
  DIG("TF -> 2") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getCurrentDigs().addListener((o, oldValue, newValue) -> {
        if (game.getCurrentRound().getValue() == round && newValue.intValue() > oldValue.intValue()) {
          p.updateScore(
              2 * p.getCurrentDigs().get(),
              "R" + game.getCurrentRound().getValue().display() + " " + getText());
        }
      }));
    }
  },
  TECH("TECH -> 2") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> {
        addTechTrackListener(p.getTerraformingLevel(), game.getCurrentRound(), p, getText(), round);
        addTechTrackListener(p.getNavLevel(), game.getCurrentRound(), p, getText(), round);
        addTechTrackListener(p.getAiLevel(), game.getCurrentRound(), p, getText(), round);
        addTechTrackListener(p.getGaiaformingLevel(), game.getCurrentRound(), p, getText(), round);
        addTechTrackListener(p.getEconLevel(), game.getCurrentRound(), p, getText(), round);
        addTechTrackListener(p.getKnowledgeLevel(), game.getCurrentRound(), p, getText(), round);
      });
    }
  },
  FED("FED -> 5") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers()
          .values()
          // These should never be removed so changes should be all additions...
          .forEach(p -> {
            p.getFederationTiles().addListener((SetChangeListener<FedToken>) change -> {
              if (game.getCurrentRound().getValue() == round) {
                p.updateScore(5, "R" + game.getCurrentRound().getValue().display() + " " + getText());
              }
            });
          });
    }
  },

  BB_1("BB -> 5") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> {
        addBigBuildingListener(p.getPi(), game.getCurrentRound(), p, getText(), round);
        addBigBuildingListener(p.getQa(), game.getCurrentRound(), p, getText(), round);
        addBigBuildingListener(p.getKa(), game.getCurrentRound(), p, getText(), round);
      });
    }
  },
  BB_2("BB -> 5") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> {
        addBigBuildingListener(p.getPi(), game.getCurrentRound(), p, getText(), round);
        addBigBuildingListener(p.getQa(), game.getCurrentRound(), p, getText(), round);
        addBigBuildingListener(p.getKa(), game.getCurrentRound(), p, getText(), round);
      });
    }
  };

  private final String text;

  RoundScoringBonus(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public abstract void addListeners(Game game, Round round);

  private static void addTechTrackListener(
      IntegerProperty techTrack,
      Property<Round> currentRound,
      Player player,
      String display,
      Round round) {
    techTrack.addListener((o, oldValue, newValue) -> {
      if (currentRound.getValue() == round) {
        player.updateScore(2, "R" + currentRound.getValue().display() + " " + display);
      }
    });
  }

  private static <T> void addBigBuildingListener(
      ObservableSet<T> bigBuilding,
      Property<Round> currentRound,
      Player player,
      String display,
      Round round) {
    bigBuilding.addListener((SetChangeListener<T>) change -> {
      if (currentRound.getValue() == round) {
        player.updateScore(5, "R" + currentRound.getValue().display() + " " + display);
      }
    });
  }
}
