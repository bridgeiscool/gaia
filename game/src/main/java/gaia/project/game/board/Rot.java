package gaia.project.game.board;

import gaia.project.game.model.Coords;

public enum Rot {
  /**
   * No rotation
   */
  POS_1(0),
  /**
   * Rotated clockwise by pi/3
   */
  POS_2(Math.PI / 3.0),
  /**
   * Rotated clockwise by 2 pi / 3
   */
  POS_3(2.0 * Math.PI / 3.0),
  /**
   * Rotated clockwise by pi
   */
  POS_4(Math.PI),
  /**
   * Rotated clockwise by 4 pi / 3
   */
  POS_5(4.0 * Math.PI / 3.0),
  /**
   * Rotated clockwise by 5 pi / 3
   */
  POS_6(5.0 * Math.PI / 3.0);

  private double rotAngle;

  Rot(double rotAngle) {
    this.rotAngle = rotAngle;
  }

  public Coords applyTo(double x, double y) {
    return new Coords(x * Math.cos(rotAngle) - y * Math.sin(rotAngle), x * Math.sin(rotAngle) + y * Math.cos(rotAngle));
  }
}
