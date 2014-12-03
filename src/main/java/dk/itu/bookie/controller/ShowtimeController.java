/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.HashSet;
import java.util.Set;
import java.util.ResourceBundle;

// Net utilities
import java.net.URL;

// JavaFX layouts
import javafx.scene.layout.GridPane;

// JavaFX controls
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

// FXML utilities
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

// Components
import dk.itu.bookie.component.Seat;

// Models
import dk.itu.bookie.model.Showtime;

/**
 * Showtime controller class.
 *
 * @version 1.0.0
 */
public class ShowtimeController implements Initializable {
  @FXML
  private TableView<Showtime> showtimes;

  @FXML
  private GridPane auditorium;

  public void initialize(final URL url, final ResourceBundle resourceBundle) {
    int rows = 10;
    int seats = 15;

    Set<Seat> selectedSeats = new HashSet<>();

    for (int row = 1; row <= rows; row++) {
      for (int seat = 1; seat <= seats; seat++) {
        Seat auditoriumSeat = new Seat(row, seat);

        auditoriumSeat.getState().addListener((e, ov, nv) -> {
          if (nv) {
            selectedSeats.add(auditoriumSeat);
          }
          else {
            selectedSeats.remove(auditoriumSeat);
          }
        });

        this.auditorium.add(auditoriumSeat, seat, row);
      }
    }
  }
}
