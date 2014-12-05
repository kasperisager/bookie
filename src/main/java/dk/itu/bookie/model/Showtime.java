/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// General utilities
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Text utilities
import java.text.DateFormat;
import java.text.SimpleDateFormat;

// Time utilities
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// SQL utilities
import java.sql.Timestamp;

// Base model
import dk.itu.donkey.Model;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Showtime class.
 *
 * @version 1.0.0
 */
public final class Showtime extends Model {
  /**
   * The movie to show.
   */
  public Movie movie;

  /**
   * The auditorium in which the movie is shown.
   */
  public Auditorium auditorium;

  /**
   * List of tickets.
   */
  public List<Ticket> tickets;

  /**
   * The time at which the movie is playing.
   */
  public Long playingAt;

  /**
   * Initialize a showtime.
   */
  public Showtime() {
    super("showtimes", Bookie.db());
  }

  public static SimpleDateFormat dateFormat() {
    return new SimpleDateFormat("EEE dd/MM yyyy", new Locale("da"));
  }

  /**
   * Return a pretty version of the showtime's date.
   *
   * @return A formatted showtime date.
   */
  public String date() {
    return this.dateFormat().format(new Date(this.playingAt));
  }

  public static SimpleDateFormat timeFormat() {
    return new SimpleDateFormat("HH:mm", new Locale("da"));
  }

  /**
   * Return a pretty version of the showtime's time.
   *
   * @return A formatted showtime time.
   */
  public String time() {
    return this.timeFormat().format(new Date(this.playingAt));
  }

  /**
   * Helper method for initializing the showtime's playingAt field.
   *
   * @param year    The year that the showtime is playing.
   * @param month   The month that the showtime is playing.
   * @param day     The day that the showtime is playing.
   * @param hour    The hour that the showtime is playing.
   * @param minute  The minute that the showtime is playing.
   */
  public void playingAt(
    final int year,
    final int month,
    final int day,
    final int hour,
    final int minute
  ) {
    this.playingAt = LocalDateTime.of(year, month, day, hour, minute)
                                  .toEpochSecond(ZoneOffset.of("Z")) * 1000;
  }
}
