package gaia.project.game.model;

public enum SetupEnum {
  RANDOM_RACES("Randomize Races"), DRAFT_RACES("Draft Races"), AUCTION_RACES("Auction Races");

  private final String display;

  SetupEnum(String display) {
    this.display = display;
  }

  public String getDisplay() {
    return display;
  }

  public static SetupEnum fromDisplay(String toMatch) {
    for (SetupEnum maybeMatch : values()) {
      if (maybeMatch.display.equals(toMatch)) {
        return maybeMatch;
      }
    }

    throw new IllegalStateException("Unknown constant: " + toMatch);
  }

}
