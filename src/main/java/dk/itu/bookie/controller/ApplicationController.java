/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.ResourceBundle;

// Net utilities
import java.net.URL;

// SQL utilities
import java.sql.SQLException;

// JavaFX collections
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// FXML utilities
import javafx.fxml.FXML;

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
  private static ApplicationController instance;

  private static ObservableList<Showtime> showtimes;

  private static ObservableList<Reservation> reservations;

  public ApplicationController getInstance() {
    return ApplicationController.instance;
  }

  public void initialize() {
    ApplicationController.instance = this;
  }

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

  public static ObservableList<Reservation> reservations() throws SQLException {
    if (ApplicationController.reservations == null) {
      ApplicationController.reservations = FXCollections.observableArrayList(
        Model
          .find(Reservation.class)
          .where("showtimes.playingat", ">", System.currentTimeMillis())
          .orderBy("showtimes.playingat")
          .get()
      );
    }

    return ApplicationController.reservations;
  }
}
