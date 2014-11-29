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
public class Auditorium extends Model {
  /**
   * Initialize an auditorium.
   */
  public Auditorium() {
    super("auditoriums", Bookie.db());
  }
}
