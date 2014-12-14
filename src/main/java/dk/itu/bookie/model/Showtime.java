/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// General utilities
import java.util.Date;
import java.util.Locale;

// Text utilities
import java.text.SimpleDateFormat;

// Time utilities
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// JavaFX collections
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// JavaFX properties
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

// Base model
import dk.itu.bookie.FXModel;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Showtime class.
 *
 * @version 1.0.0
 */
public final class Showtime extends FXModel {
  /**
   * The movie to show.
   */
  public ObjectProperty<Movie> movie =
    new SimpleObjectProperty<>();

  /**
   * The auditorium in which the movie is shown.
   */
  public ObjectProperty<Auditorium> auditorium =
    new SimpleObjectProperty<>();

  /**
   * List of reservations.
   */
  public ObservableList<Reservation> reservations =
    FXCollections.observableArrayList();

  /**
   * The time at which the movie is playing.
   */
  public LongProperty playingAt =
    new SimpleLongProperty();

  /**
   * Initialize a showtime.
   */
  public Showtime() {
    super("showtimes", Bookie.db());
  }

  /**
   * Return a date format for formatting showtime dates.
   *
   * @return A date format for formatting showtime dates.
   */
  public static SimpleDateFormat dateFormat() {
    return new SimpleDateFormat("EEE dd/MM yyyy", new Locale("da"));
  }

  /**
   * Return a pretty version of the showtime's date.
   *
   * @return A formatted showtime date.
   */
  public StringProperty date() {
    return new SimpleStringProperty(
      this.dateFormat().format(new Date(this.playingAt.get()))
    );
  }

  /**
   * Return a date format for formatting showtime times.
   *
   * @return A date format for formatting showtime times.
   */
  public static SimpleDateFormat timeFormat() {
    return new SimpleDateFormat("HH:mm", new Locale("da"));
  }

  /**
   * Return a pretty version of the showtime's time.
   *
   * @return A formatted showtime time.
   */
  public StringProperty time() {
    return new SimpleStringProperty(
      this.timeFormat().format(new Date(this.playingAt.get()))
    );
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
    this.playingAt.set(
      LocalDateTime
        .of(year, month, day, hour, minute)
        .toEpochSecond(ZoneOffset.of("Z")) * 1000
    );
  }
}
