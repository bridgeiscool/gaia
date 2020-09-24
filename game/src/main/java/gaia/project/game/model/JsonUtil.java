package gaia.project.game.model;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import gaia.project.game.model.Player.FedToken;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class JsonUtil {
  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  // Constants
  public static final String COORDS = "coords";

  // Traits
  public static final String RACE = "race";
  public static final String PLAYER_ENUM = "playerEnum";

  // Player board
  public static final String GAIA_BIN = "gaiaBin";
  public static final String BIN1 = "bin1";
  public static final String BIN2 = "bin2";
  public static final String BIN3 = "bin3";
  public static final String ORE = "ore";
  public static final String CREDITS = "credits";
  public static final String RESEARCH = "research";
  public static final String QIC = "qic";
  public static final String POWER = "power";
  public static final String CHARGE = "charge";
  public static final String AVAILABLE_GAIAFORMERS = "availableGaiaformers";
  public static final String CURRENT_INCOME = "currentIncome";
  public static final String SCORE = "score";
  public static final String ROUND_BOOSTER = "roundBooster";
  public static final String ROUND_BOOSTER_USED = "roundBoosterUsed";

  // Tech
  public static final String TERRA_LEVEL = "terraformingLevel";
  public static final String NAV_LEVEL = "navLevel";
  public static final String AI_LEVEL = "aiLevel";
  public static final String GAIA_LEVEL = "gaiaformingLevel";
  public static final String ECON_LEVEL = "econLevel";
  public static final String K_LEVEL = "knowledgeLevel";
  public static final String TERRA_COST = "terraCost";
  public static final String NAV_RANGE = "navRange";
  public static final String GAIA_COST = "gaiaCost";

  // Tech Tiles
  public static final String BB_POWER = "bbPower";
  public static final String BUILT_ON = "builtOn";
  public static final String GAIA_PLANETS = "gaiaPlanets";
  public static final String TECH_TILES = "techTiles";
  public static final String SPECIAL_ACTIONS = "specialActions";
  public static final String COVERED_TECH_TILES = "coveredTechTiles";
  public static final String ADV_TECH_TILES = "advancedTechTiles";

  // Federations
  public static final String FEDERATIONS = "federations";
  public static final String FED = "fed";
  public static final String FED_TILES = "fedTiles";
  public static final String SATS = "sats";

  // Buildings
  public static final String MINES = "mines";
  public static final String TPS = "tps";
  public static final String RLS = "rls";
  public static final String PI = "pi";
  public static final String KA = "ka";
  public static final String QA = "qa";
  public static final String GFS = "gfs";
  public static final String LP = "lp";

  // Scoring related
  public static final String SECTORS = "sectors";
  public static final String BUILDINGS = "bldgs";
  public static final String BLDGS_IN_FEDS = "bldgsInFeds";
  public static final String SCORE_LOG = "scoreLog";

  // Extra
  public static final String BRAINSTONE = "brainStone";
  public static final String GFS_IN_GAIA_BIN = "gfsInGaiaBin";
  public static final String GRAY_PLANETS = "graayPlanets";
  public static final String SPACE_STATIONS = "spaceStations";
  public static final String LEECH_MINES = "leechMines";

  // Game Level
  public static final String CURRENT_ROUND = "currentRound";
  public static final String K3_ACTION_TAKEN = "k3ActionTaken";
  public static final String TF2_ACTION_TAKEN = "tf2ActionTaken";
  public static final String CREDITS_ACTION_TAKEN = "creditsActionTaken";
  public static final String ORE_ACTION_TAKEN = "oreActionTaken";
  public static final String K2_ACTION_TAKEN = "k2ActionTaken";
  public static final String TF_ACTION_TAKEN = "tfActionTaken";
  public static final String PT_ACTION_TAKEN = "ptActionTaken";
  public static final String Q2_ACTION_TAKEN = "q2ActionTaken";
  public static final String Q3_ACTION_TAKEN = "q3ActionTaken";
  public static final String Q4_ACTION_TAKEN = "q4ActionTaken";

  public static final String VP_FEDS = "vpFeds";
  public static final String PT_FEDS = "ptFeds";
  public static final String QIC_FEDS = "qicFeds";
  public static final String ORE_FEDS = "oreFeds";
  public static final String CREDIT_FEDS = "creditFeds";
  public static final String K_FEDS = "kFeds";

  public static final String PLAYER_ORDER = "playerOrder";
  public static final String PASSED_PLAYERS = "passedPlayers";
  public static final String ACTIVE_PLAYER = "activePlayer";

  public static final String GAME_BOARD = "gameBoard";
  public static final String GAIAFORMED = "gaiaformed";
  public static final String ROUND_BOOSTERS = "roundBoosters";
  public static final String ROUND_SCORING = "roundScoring";
  public static final String END_SCORING1 = "endScoring1";
  public static final String END_SCORING2 = "endScoring2";
  public static final String TERRA_BONUS = "terraBonus";

  public static final String TURN = "turn";

  // Game options
  public static final String BALTAKS_BUFF = "baltaksBuff";
  public static final String SETUP = "setup";
  public static final String SEED = "seed";

  public static <T> void writeCollection(
      JsonWriter json,
      String name,
      Collection<T> collection,
      Function<T, String> toString)
      throws IOException {
    json.name(name).beginArray();
    for (T t : collection) {
      json.value(toString.apply(t));
    }

    json.endArray();
  }

  public static void writeCoordsCollection(JsonWriter json, String name, Collection<String> coords) throws IOException {
    json.name(name).beginArray();
    for (String c : coords) {
      json.value(c);
    }
    json.endArray();
  }

  public static <T> void readStringArray(Collection<T> addTo, JsonReader json, Function<String, T> function)
      throws IOException {
    json.beginArray();
    while (json.hasNext()) {
      addTo.add(function.apply(json.nextString()));
    }
    json.endArray();
  }

  public static <T> void readCoordsArray(Collection<String> addTo, JsonReader json) throws IOException {
    json.beginArray();
    while (json.hasNext()) {
      addTo.add(json.nextString());
    }
    json.endArray();
  }

  public static void readIntegerArray(Collection<Integer> addTo, JsonReader json) throws IOException {
    json.beginArray();
    while (json.hasNext()) {
      addTo.add(json.nextInt());
    }
    json.endArray();
  }

  public static void readSetOfSets(Set<Set<String>> sets, JsonReader json) throws IOException {
    json.beginArray();
    while (json.hasNext()) {
      Set<String> federation = new HashSet<>();
      json.beginObject();
      json.nextName();
      json.beginArray();
      while (json.hasNext()) {
        federation.add(json.nextString());
      }
      json.endArray();
      sets.add(federation);
      json.endObject();
    }

    json.endArray();
  }

  public static void readFedTiles(Set<FedToken> fedTokens, JsonReader json) throws IOException {
    json.beginArray();
    while (json.hasNext()) {
      json.beginObject();
      fedTokens.add(new FedToken(FederationTile.valueOf(json.nextName()), json.nextBoolean()));
      json.endObject();
    }
    json.endArray();
  }

  public static void readSpecialActions(Map<Enum<?>, BooleanProperty> specialActions, JsonReader json)
      throws IOException {
    json.beginObject();
    while (json.hasNext()) {
      specialActions.put(getEnum(json.nextName()), new SimpleBooleanProperty(json.nextBoolean()));
    }
    json.endObject();
  }

  private static Enum<?> getEnum(String name) {
    for (TechTile tt : TechTile.values()) {
      if (tt.name().equals(name)) {
        return tt;
      }
    }

    for (AdvancedTechTile att : AdvancedTechTile.values()) {
      if (att.name().equals(name)) {
        return att;
      }
    }

    for (PlayerBoardAction pba : PlayerBoardAction.values()) {
      if (pba.name().equals(name)) {
        return pba;
      }
    }

    throw new IllegalStateException("Other action!");
  }

  public static void readScoreLog(Map<String, Integer> scoreLog, JsonReader json) throws IOException {
    json.beginObject();
    while (json.hasNext()) {
      scoreLog.put(json.nextName(), json.nextInt());
    }

    json.endObject();
  }
}
