package gaia.project.game.model;

import java.io.IOException;

import com.google.common.base.Preconditions;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javafx.collections.SetChangeListener;

public final class BalTaksPlayer extends Player {
  private int gaiaformersInGaiaBin;

  public static BalTaksPlayer createNew(PlayerEnum playerEnum) {
    BalTaksPlayer p = new BalTaksPlayer();
    p.fromRace(Race.BALTAKS, playerEnum);

    return p;
  }

  public static BalTaksPlayer empty() {
    return new BalTaksPlayer();
  }

  private BalTaksPlayer() {}

  @Override
  protected void raceSpecificListeners() {
    getPi().addListener((SetChangeListener<String>) change -> Util.plus(getNavLevel(), 1));
  }

  public int getGaiaformersInGaiaBin() {
    return gaiaformersInGaiaBin;
  }

  public void setGaiaformersInGaiaBin(int gaiaformersInGaiaBin) {
    this.gaiaformersInGaiaBin = gaiaformersInGaiaBin;
  }

  @Override
  public void gaiaPhase() {
    super.gaiaPhase();
    Util.plus(getAvailableGaiaformers(), gaiaformersInGaiaBin);
    gaiaformersInGaiaBin = 0;
  }

  @Override
  protected void writeExtraContent(JsonWriter json) throws IOException {
    json.name(JsonUtil.GFS_IN_GAIA_BIN).value(gaiaformersInGaiaBin);
  }

  @Override
  protected void handleAdditionalContent(String name, JsonReader json) throws IOException {
    Preconditions.checkArgument(JsonUtil.GFS_IN_GAIA_BIN.contentEquals(name));
    this.gaiaformersInGaiaBin = json.nextInt();
  }
}
