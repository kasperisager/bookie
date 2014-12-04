/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.ResourceBundle;

// Net utilities
import java.net.URL;

// JavaFX layouts
import javafx.scene.layout.GridPane;

// JavaFX controls
import javafx.scene.control.Label;
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
  private static ShowtimeController instance;

  private char[] alphabet = new char[]{
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
  };

  @FXML
  private TableView<Showtime> showtimes;

  @FXML
  private TableColumn movieColumn;

  @FXML
  private TableColumn auditoriumColumn;

  @FXML
  private TableColumn dateColumn;

  @FXML
  private TableColumn timeColumn;

  @FXML
  private GridPane auditorium;

  public ShowtimeController getInstance() {
    return ShowtimeController.instance;
  }

  public void initialize(final URL url, final ResourceBundle resourceBundle) {
    ShowtimeController.instance = this;

    this.bindTableColumnWidths();

    int rows = 10;
    int seats = 15;

    Set<Seat> selectedSeats = new HashSet<>();

    for (int row = 1; row <= (rows + 1); row++) {
      for (int seat = 1; seat <= (seats + 1); seat++) {
        if ((row > 1) && (seat > 1) || (seat == row)) {
          continue;
        }

        String text;

        if (row == 1) {
          text = String.format("%s", seat - 1);
        }
        else {
          text = String.format("%s", this.alphabet[row - 2]);
        }

        Label label = new Label(text);

        this.auditorium.add(label, seat, row);
      }
    }

    for (int row = 2; row <= (rows + 1); row++) {
      for (int seat = 2; seat <= (seats + 1); seat++) {
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

  public void bindTableColumnWidths() {
    this.movieColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().multiply(0.30)
    );

    this.auditoriumColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().multiply(0.30)
    );

    this.dateColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().multiply(0.20)
    );

    this.timeColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().multiply(0.20)
    );
  }
}
