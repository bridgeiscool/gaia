package gaia.project.game.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public enum RoundScoringBonus {
  MINE("Mine -> 2") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getMines().addListener((SetChangeListener<Coords>) change -> {
        if (game.getCurrentRound().getValue() == round && change.wasAdded()) {
          p.getScore().setValue(p.getScore().getValue() + 2);
        }
      }));
    }
  },
  MINE_GP3("GP Mine -> 3") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getGaiaPlanets().addListener((o, oldValue, newValue) -> {
        if (game.getCurrentRound().getValue() == round) {
          p.getScore().setValue(p.getScore().getValue() + 3);
        }
      }));
    }
  },
  MINE_GP4("GP Mine -> 4") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getGaiaPlanets().addListener((o, oldValue, newValue) -> {
        if (game.getCurrentRound().getValue() == round) {
          p.getScore().setValue(p.getScore().getValue() + 4);
        }
      }));
    }
  },
  TP_3("TP -> 3") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getTradingPosts().addListener((SetChangeListener<Coords>) change -> {
        if (game.getCurrentRound().getValue() == round && change.wasAdded()) {
          p.getScore().setValue(p.getScore().getValue() + 3);
        }
      }));
    }
  },
  TP_4("TP -> 4") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> p.getTradingPosts().addListener((SetChangeListener<Coords>) change -> {
        if (game.getCurrentRound().getValue() == round && change.wasAdded()) {
          p.getScore().setValue(p.getScore().getValue() + 4);
        }
      }));
    }
  },
  DIG("DIG -> 2") {
    @Override
    public void addListeners(Game game, Round round) {
      // TODO: Implement
    }
  },
  TECH("TECH -> 2") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> {
        addTechTrackListener(p.getTerraformingLevel(), game.getCurrentRound(), p.getScore(), round);
        addTechTrackListener(p.getNavLevel(), game.getCurrentRound(), p.getScore(), round);
        addTechTrackListener(p.getAiLevel(), game.getCurrentRound(), p.getScore(), round);
        addTechTrackListener(p.getGaiaformingLevel(), game.getCurrentRound(), p.getScore(), round);
        addTechTrackListener(p.getEconLevel(), game.getCurrentRound(), p.getScore(), round);
        addTechTrackListener(p.getKnowledgeLevel(), game.getCurrentRound(), p.getScore(), round);
      });
    }
  },
  FED("FED -> 5") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> {
        p.getFederationTiles().addListener((ListChangeListener<FederationTile>) change -> {
          if (game.getCurrentRound().getValue() == round) {
            p.getScore().setValue(p.getScore().getValue() + 5);
          }
        });
      });
    }
  },

  BB_1("BB -> 5") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> {
        addBigBuildingListener(p.getPi(), game.getCurrentRound(), p.getScore(), round);
        addBigBuildingListener(p.getQa(), game.getCurrentRound(), p.getScore(), round);
        addBigBuildingListener(p.getKa(), game.getCurrentRound(), p.getScore(), round);
      });
    }
  },
  BB_2("BB -> 5") {
    @Override
    public void addListeners(Game game, Round round) {
      game.getPlayers().values().forEach(p -> {
        addBigBuildingListener(p.getPi(), game.getCurrentRound(), p.getScore(), round);
        addBigBuildingListener(p.getQa(), game.getCurrentRound(), p.getScore(), round);
        addBigBuildingListener(p.getKa(), game.getCurrentRound(), p.getScore(), round);
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
      IntegerProperty score,
      Round round) {
    techTrack.addListener((o, oldValue, newValue) -> {
      if (currentRound.getValue() == round) {
        score.setValue(score.getValue() + 2);
      }
    });
  }

  private static <T> void addBigBuildingListener(
      ObservableSet<T> bigBuilding,
      Property<Round> currentRound,
      IntegerProperty score,
      Round round) {
    bigBuilding.addListener((SetChangeListener<T>) change -> {
      if (currentRound.getValue() == round) {
        score.setValue(score.getValue() + 5);
      }
    });
  }
}
