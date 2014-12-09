/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// General utilities
import java.util.ArrayList;
import java.util.List;

// Base model
import dk.itu.donkey.Model;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Reservation class.
 *
 * @version 1.0.0
 */
public final class Reservation extends Model {
  /**
   * The phone number for identifying the reservation.
   */
  public int phoneNumber;

  /**
   * Has the reservation been payed for?
   */
  public boolean bought;

  /**
   * The showtime that the reservation is for.
   */
  public Showtime showtime;

  /**
   * The reserved tickets.
   */
  public List<Ticket> tickets = new ArrayList<>();

  /**
   * Initialize a reservation.
   */
  public Reservation() {
    super("reservations", Bookie.db());
  }
}
