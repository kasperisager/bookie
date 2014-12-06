/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie;

// General utilities
import java.util.Calendar;

// SQL utilities
import java.sql.SQLException;

// Models
import dk.itu.bookie.model.Auditorium;
import dk.itu.bookie.model.Movie;
import dk.itu.bookie.model.Reservation;
import dk.itu.bookie.model.Ticket;
import dk.itu.bookie.model.Showtime;

/**
 * Seeder class.
 *
 * @version 1.0.0
 */
public final class Seeder {
  public static void reset() throws SQLException{
    Bookie.db().schema().drop("tickets");
    Bookie.db().schema().drop("reservations");
    Bookie.db().schema().drop("showtimes");
    Bookie.db().schema().drop("auditoriums");
    Bookie.db().schema().drop("movies");
  }

  public static void init() throws SQLException{
    Object[][] auditoriumData = new Object[][] {{
      "Sal 1", 10, 15
    }, {
      "Sal 2", 8, 14
    }, {
      "Sal 3", 15, 25
    }};

    Auditorium[] auditoriums = new Auditorium[auditoriumData.length];

    for (int i = 0; i < auditoriumData.length; i++) {
      Auditorium auditorium = new Auditorium();
      auditorium.name = (String) auditoriumData[i][0];
      auditorium.rows = (int) auditoriumData[i][1];
      auditorium.seats = (int) auditoriumData[i][2];
      auditorium.insert();
      auditoriums[i] = auditorium;
    }

    String[] movieData = new String[]{
      "Interstellar",
      "The Hobbit",
      "The Hunger Games: Mockingjay - Part 1",
      "Nightcrawler",
      "Stjerner Uden Hjerner",
      "Planes 2: Fire and Rescue",
      "Jurassic Park 4"
    };

    Movie[] movies = new Movie[movieData.length];

    for (int i = 0; i < movieData.length; i++) {
      Movie movie = new Movie();
      movie.name = movieData[i];
      movie.insert();
      movies[i] = movie;
    }

    Showtime[] showtimes = new Showtime[80];

    Calendar cal = Calendar.getInstance();

    for (int i = 0; i < 80; i++) {
      Showtime showtime = new Showtime();

      // Get the next auditorium.
      showtime.auditorium = auditoriums[i % auditoriums.length];

      // Get a random movie.
      showtime.movie = movies[(int) (Math.random() * movies.length)];

      // Set the showtime date.
      showtime.playingAt(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1,
        (i % 29) + 1,
        i % 23,
        ((i % 2) * 30) % 60
      );

      showtime.insert();
      showtimes[i] = showtime;
    }

    Reservation[] reservations = new Reservation[100];

    for (int i = 0; i < 100; i++) {
      Reservation reservation = new Reservation();
      reservation.phoneNumber = 10000000 + (int) (Math.random() * 90000000);
      reservation.showtime = showtimes[i % showtimes.length];
      reservation.insert();

      for (int j = 0; j < (int) (Math.random() * 10); j++) {
        Ticket ticket = new Ticket();
        ticket.reservation = reservation;
        ticket.insert();
      }
    }
  }
}
