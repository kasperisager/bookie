/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

import java.util.List;

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
   * Number of rows in the auditorium.
   */
  public int rows;

  /**
   * Number of seats per row in the auditorium.
   */
  public int seats;

  /**
   * Initialize an auditorium.
   */
  public Auditorium() {
    super("auditoriums", Bookie.db());
  }
}
