/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// Base model
import dk.itu.donkey.Model;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Reservation class.
 *
 * @version 1.0.0
 */
public class Reservation extends Model {
  /**
   * The phone number for identifying the reservation.
   */
  public int phoneNumber;

  /**
   * The reserved ticket.
   */
  public Ticket ticket;

  /**
   * Initialize a reservation.
   */
  public Reservation() {
    super("reservations", Bookie.db());
  }
}
