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
 * Showtime class.
 *
 * @version 1.0.0
 */
public class Showtime extends Model {
  /**
   * List of tickets.
   */
  public List<Ticket> tickets;

  /**
   * The movie to show.
   */
  public Movie movie;

  /**
   * The auditorium in which the movie is shown.
   */
  public Auditorium auditorium;

  /**
   * Initialize a showtime.
   */
  public Showtime() {
    super("showtimes", Bookie.db());
  }
}
