/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.List;

// SQL utilities
import java.sql.SQLException;

// JavaFX collections
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Donkey utilities
import dk.itu.donkey.Model;

// Models
import dk.itu.bookie.model.Showtime;
import dk.itu.bookie.model.Reservation;

/**
 * Application controller class.
 *
 * @version 1.0.0
 */
public final class ApplicationController {
  /**
   * The singleton instance of the controller.
   */
  private static ApplicationController instance;

  /**
   * List of showtimes.
   */
  private static ObservableList<Showtime> showtimes;

  /**
   * List of reservations.
   */
  private static ObservableList<Reservation> reservations;

  /**
   * Get the singleton instance of the controller.
   *
   * @return The singleton Application controller.
   */
  public ApplicationController getInstance() {
    return ApplicationController.instance;
  }

  /**
   * Initialize the controller.
   *
   * @throws Exception In case of uncaught errors.
   */
  public void initialize() throws Exception {
    ApplicationController.instance = this;
  }

  /**
   * Grab all showtimes from the database.
   *
   * @return Observable list of showtimes.
   *
   * @throws SQLException In case of a SQL error.
   */
  public static ObservableList<Showtime> showtimes() throws SQLException {
    if (ApplicationController.showtimes == null) {
      ApplicationController.showtimes = FXCollections.observableArrayList(
        Model
          .find(Showtime.class)
          .where("playingat", ">", System.currentTimeMillis())
          .orderBy("movies.name")
          .orderBy("playingat")
          .get()
      );
    }

    return ApplicationController.showtimes;
  }

  /**
   * Grab all reservations from the database.
   *
   * @return Observable list of reservations.
   *
   * @throws SQLException In case of a SQL error.
   */
  public static ObservableList<Reservation> reservations() throws SQLException {
    if (ApplicationController.reservations == null) {
      List<Showtime> showtimes = ApplicationController.showtimes();

      ApplicationController.reservations = FXCollections.observableArrayList();

      for (Showtime showtime: showtimes) {
        ApplicationController.reservations.addAll(showtime.reservations);
      }
    }

    return ApplicationController.reservations;
  }
}
