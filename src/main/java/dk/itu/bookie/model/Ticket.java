/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// Base model
import dk.itu.donkey.Model;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Ticket class.
 *
 * @version 1.0.0
 */
public class Ticket extends Model {
  /**
   * The showtime that the ticket is for.
   */
  public Showtime showtime;

  /**
   * The reservation that this ticket is part of.
   */
  public Reservation reservation;

  /**
   * Initialize a ticket.
   */
  public Ticket() {
    super("tickets", Bookie.db());
  }
}
