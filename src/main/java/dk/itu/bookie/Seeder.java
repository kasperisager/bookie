/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie;

// SQL utilities
import java.sql.SQLException;

// Models
import dk.itu.bookie.model.Auditorium;
import dk.itu.bookie.model.Movie;
import dk.itu.bookie.model.Reservation;
import dk.itu.bookie.model.Ticket;
import dk.itu.bookie.model.Showtime;

/**
 * Bookie class.
 *
 * @version 1.0.0
 */
public final class Seeder {

  public static void reset() throws SQLException{
    Bookie.db().schema().drop("auditoriums");
    Bookie.db().schema().drop("movies");
    Bookie.db().schema().drop("tickets");
    Bookie.db().schema().drop("showtimes");
    Bookie.db().schema().drop("reservations");
  }

  public static void init() throws SQLException{
    Auditorium aud1 = new Auditorium();
    aud1.name = "Sal 1";
    aud1.rows = 10;
    aud1.seats = 15;
    aud1.insert();

    Auditorium aud2 = new Auditorium();
    aud2.name = "Sal 2";
    aud2.rows = 8;
    aud2.seats = 14;
    aud2.insert();

    Auditorium aud3 = new Auditorium();
    aud3.name = "Sal 3";
    aud3.rows = 15;
    aud3.seats = 25;
    aud3.insert();

    String[] movies = new String[]{
      "Interstellar",
      "The Hobbit",
      "The Hunger Games: Mockingjay - Part 1",
      "Nightcrawler",
      "Stjerner Uden Hjerner",
      "Planes 2: Fire and Rescue",
      "Jurassic Park 4"
    };

    for (String movieName: movies) {
      Movie mov = new Movie();
      mov.name = movieName;
      mov.insert();

      Showtime show1 = new Showtime();
      show1.auditorium = aud1;
      show1.movie = mov;
      show1.playingAt(2015, 1, 12, 18, 30);
      show1.insert();

      Showtime show2 = new Showtime();
      show2.auditorium = aud2;
      show2.movie = mov;
      show2.playingAt(2015, 1, 12, 19, 00);
      show2.insert();

      Showtime show3 = new Showtime();
      show3.auditorium = aud3;
      show3.movie = mov;
      show3.playingAt(2015, 1, 14, 17, 00);
      show3.insert();

      Showtime show4 = new Showtime();
      show4.auditorium = aud2;
      show4.movie = mov;
      show4.playingAt(2015, 1, 14, 18, 30);
      show4.insert();

      Showtime show5 = new Showtime();
      show5.auditorium = aud1;
      show5.movie = mov;
      show5.playingAt(2015, 1, 12, 18, 30);
      show5.insert();

      Reservation res1 = new Reservation();
      res1.insert();

      Reservation res2 = new Reservation();
      res2.insert();

      Ticket tick1 = new Ticket();
      tick1.showtime = show1;
      tick1.reservation = res1;
      tick1.insert();

      Ticket tick2 = new Ticket();
      tick2.showtime = show1;
      tick2.reservation = res1;
      tick2.insert();
    }
  }
}
