/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// JavaFX layouts
import javafx.scene.layout.GridPane;

// JavaFX controls
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

// JavaFX geometry
import javafx.geometry.HPos;
import javafx.geometry.VPos;

// JavaFX properties
import javafx.beans.property.SimpleStringProperty;

// FXML utilities
import javafx.fxml.FXML;

// Components
import dk.itu.bookie.component.Seat;

// Models
import dk.itu.bookie.model.Showtime;

/**
 * Showtime controller class.
 *
 * @version 1.0.0
 */
public final class ShowtimeController {
  /**
   * The singleton instance of the controller.
   */
  private static ShowtimeController instance;

  /**
   * List of letters in the English alphabet.
   */
  private char[] alphabet = new char[]{
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
  };

  /**
   * Table containing the different showtimes.
   */
  @FXML
  private TableView<Showtime> showtimes;

  /**
   * The column containing the name of the movie playing.
   */
  @FXML
  private TableColumn<Showtime, String> movieColumn;

  /**
   * The column containing the name of the auditorium that the movie is playing
   * in.
   */
  @FXML
  private TableColumn<Showtime, String> auditoriumColumn;

  /**
   * The column containing the date that the movie is playing.
   */
  @FXML
  private TableColumn<Showtime, String> dateColumn;

  /**
   * The column containing the time at which the movie is playing.
   */
  @FXML
  private TableColumn<Showtime, String> timeColumn;

  /**
   * The gridpane containing the seats in the auditorium.
   */
  @FXML
  private GridPane auditorium;

  /**
   * Get the singleton instance of the controller.
   *
   * @return The singleton Showtime controller.
   */
  public ShowtimeController getInstance() {
    return ShowtimeController.instance;
  }

  /**
   * Initialize the controller.
   *
   * @throws Exception In case of uncaught errors.
   */
  public void initialize() throws Exception {
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

        // Center align the label within its column.
        this.auditorium.setHalignment(label, HPos.CENTER);
        this.auditorium.setValignment(label, VPos.CENTER);
      }
    }

    for (int row = 2; row <= (rows + 1); row++) {
      for (int seat = 2; seat <= (seats + 1); seat++) {
        Seat auditoriumSeat = new Seat(row, seat);

        this.bindSeatWidth(auditoriumSeat, seats);

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

    this.showtimes.setItems(ApplicationController.showtimes());

    this.auditoriumColumn.setCellValueFactory((data) -> {
      return new SimpleStringProperty(data.getValue().auditorium.name);
    });

    this.movieColumn.setCellValueFactory((data) -> {
      return new SimpleStringProperty(data.getValue().movie.name);
    });

    this.dateColumn.setCellValueFactory((data) -> {
      return new SimpleStringProperty(data.getValue().date());
    });

    this.dateColumn.setComparator((date1, date2) -> {
      Date date1Parsed = null;
      Date date2Parsed = null;

      try {
        date1Parsed = Showtime.dateFormat().parse(date1);
        date2Parsed = Showtime.dateFormat().parse(date2);
      }
      catch (Exception e) {
        return 0;
      }

      return date1Parsed.compareTo(date2Parsed);
    });

    this.timeColumn.setCellValueFactory((data) -> {
      return new SimpleStringProperty(data.getValue().time());
    });
  }

  /**
   * Bind the widths of the individual columns to the entire width of the
   * containing table.
   */
  public void bindTableColumnWidths() {
    this.movieColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().subtract(18).multiply(0.35)
    );

    this.auditoriumColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().subtract(18).multiply(0.35)
    );

    this.dateColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().subtract(18).multiply(0.20)
    );

    this.timeColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().subtract(18).multiply(0.10)
    );
  }

  /**
   * Bind the widths of the individual seats to the entire width of the
   * containing grid.
   *
   * @param seat  The seat whose width to bind.
   * @param seats The total number of seats per row in the grid.
   */
  public void bindSeatWidth(final Seat seat, final int seats) {
    seat.widthProperty().bind(
      this.showtimes.widthProperty().divide(seats)
    );

    seat.heightProperty().bind(
      this.showtimes.widthProperty().divide(seats)
    );
  }
}
