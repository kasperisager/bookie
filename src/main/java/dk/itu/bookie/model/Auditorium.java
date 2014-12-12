/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// Base model
import dk.itu.donkey.Model;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Auditorium class.
 *
 * @version 1.0.0
 */
public final class Auditorium extends Model {
  /**
   * The name of the auditorium.
   */
  public String name;

  /**
   * Number of rows in the auditorium.
   */
  public Integer rows;

  /**
   * Number of seats per row in the auditorium.
   */
  public Integer seats;

  /**
   * Initialize an auditorium.
   */
  public Auditorium() {
    super("auditoriums", Bookie.db());
  }
}
