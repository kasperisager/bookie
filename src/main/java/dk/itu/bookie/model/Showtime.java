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
public class Showtime extends Model {
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
  public Timestamp playingAt;

  /**
   * Initialize a showtime.
   */
  public Showtime() {
    super("showtimes", Bookie.db());
  }

  /**
   * Return a pretty version of the showtime's date.
   *
   * @return A formatted showtime date.
   */
  public String playingAt() {
    DateFormat df = new SimpleDateFormat(
      "EEEE dd/MM yyyy, HH:mm", new Locale("da")
    );

    return df.format(this.playingAt.getTime());
  }

  /**
   * Helper method for initializing the showtime's playingAt field.
   *
   * @param year
   * @param month
   * @param day
   * @param hour
   * @param minute
   */
  public void playingAt(
    final int year,
    final int month,
    final int day,
    final int hour,
    final int minute
  ) {
    this.playingAt = Timestamp.valueOf(
      LocalDateTime.of(year, month, day, hour, minute)
    );
  }
}
