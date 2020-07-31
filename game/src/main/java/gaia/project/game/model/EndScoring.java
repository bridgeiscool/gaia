package gaia.project.game.model;

public enum EndScoring {
  PLANET_TYPES("Planets"),
  BUILDINGS("Bldgs"),
  BUILDINGS_IN_FED("Fed Bldgs"),
  GAIA_PLANETS("GPs"),
  SATTELITES("Sats"),
  SECTORS("Sectors");

  private final String displayText;

  private EndScoring(String displayText) {
    this.displayText = displayText;
  }

  public String getDisplayText() {
    return displayText;
  }
}
