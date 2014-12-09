/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

// SQL utilities
import java.sql.SQLException;

// JavaFX utilities
import javafx.scene.Node;

// JavaFX layouts
import javafx.scene.layout.GridPane;

// JavaFX controls
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

// JavaFX geometry
import javafx.geometry.HPos;
import javafx.geometry.VPos;

// JavaFX collections
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

// JavaFX properties
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;

// JavaFX bindings
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.When;

// FXML utilities
import javafx.fxml.FXML;

// Components
import dk.itu.bookie.component.Filter;
import dk.itu.bookie.component.Seat;

// Models
import dk.itu.bookie.model.Auditorium;
import dk.itu.bookie.model.Showtime;
import dk.itu.bookie.model.Reservation;
import dk.itu.bookie.model.Ticket;

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
   * The column containing the number of available seats in the auditorium.
   */
  @FXML
  private TableColumn<Showtime, Integer> availableColumn;

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

  @FXML
  private Button reserve;

  @FXML
  private Button buy;

  @FXML
  private TextField phone;

  @FXML
  private TextField seats;

  private Showtime activeShowtime;
  private Reservation activeReservation;

  private ObservableSet<Seat> selectedSeats;

  /**
   * Get the singleton instance of the controller.
   *
   * @return The singleton Showtime controller.
   */
  public static ShowtimeController getInstance() {
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

    this.showtimes.setItems(ApplicationController.showtimes());

    this.showtimes
      .getSelectionModel()
      .selectedItemProperty().addListener((ob, ov, nv) -> {
      if (nv == null) {
        return;
      }

      this.activeShowtime = nv;

      this.renderShowtime(this.activeShowtime);
    });

    this.movieColumn.setCellValueFactory((data) -> {
      return new SimpleStringProperty(data.getValue().movie.name);
    });

    this.movieColumn.setGraphic(new Filter("Film", Filter.TEXT));

    this.auditoriumColumn.setCellValueFactory((data) -> {
      return new SimpleStringProperty(data.getValue().auditorium.name);
    });

    this.dateColumn.setCellValueFactory((data) -> {
      return new SimpleStringProperty(data.getValue().date());
    });

    this.dateColumn.setGraphic(new Filter("Dato", Filter.DATE));

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

    this.reserve.setOnAction((e) -> {
      if (activeReservation == null) {
        this.makeReservation(this.phone.getText(), false);
      }
      else {
        this.editReservation(this.activeReservation, false);
      }
    });

    this.buy.setOnAction((e) -> {
      if (activeReservation == null) {
        this.makeReservation(this.phone.getText(), true);
      }
      else {
        this.editReservation(this.activeReservation, true);
      }
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
      this.showtimes.widthProperty().subtract(18).multiply(0.20)
    );

    this.availableColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().subtract(18).multiply(0.15)
    );

    this.dateColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().subtract(18).multiply(0.20)
    );

    this.timeColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().subtract(18).multiply(0.10)
    );
  }

  private void renderAuditoriumLabels(final Auditorium auditorium) {
    int rows = auditorium.rows;
    int seats = auditorium.seats;

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
  }

  private void renderAuditoriumSeats(final Auditorium auditorium) {
    int rows = auditorium.rows;
    int seats = auditorium.seats;

    ReadOnlyDoubleProperty width = this.auditorium.widthProperty();
    ReadOnlyDoubleProperty height = this.auditorium.heightProperty();

    NumberBinding size = new When(
      width.divide(seats).lessThan(height.divide(rows))
    )
      .then(
        width.subtract(60).divide(seats).subtract(4)
      )
      .otherwise(
        height.subtract(60).divide(rows).subtract(4)
      );

    this.selectedSeats = FXCollections.observableSet();

    for (int row = 2; row <= (rows + 1); row++) {
      for (int seat = 2; seat <= (seats + 1); seat++) {
        Seat auditoriumSeat = new Seat(row - 2, seat - 2);

        auditoriumSeat.widthProperty().bind(size);
        auditoriumSeat.heightProperty().bind(size);

        auditoriumSeat.getState().addListener((e, ov, nv) -> {
          if (nv) {
            this.selectedSeats.add(auditoriumSeat);
          }
          else {
            this.selectedSeats.remove(auditoriumSeat);
          }
        });

        this.auditorium.add(auditoriumSeat, seat, row);
      }
    }
  }

  private void renderSelectedSeats(final boolean[][] seats) {
    for (int i = 0; i < seats.length; i++) {
      for (int j = 0; j < seats[i].length; j++) {
        if (seats[i][j]) {
          Seat seat = this.getSeat(i, j);

          if (seat != null) {
            seat.select();
          }
        }
      }
    }
  }

  private void renderReservedSeats(final boolean[][] seats) {
    for (int i = 0; i < seats.length; i++) {
      for (int j = 0; j < seats[i].length; j++) {
        if (seats[i][j]) {
          Seat seat = this.getSeat(i, j);

          if (seat != null) {
            seat.reserve();
          }
        }
      }
    }
  }

  private void renderBoughtSeats(final boolean[][] seats) {
    for (int i = 0; i < seats.length; i++) {
      for (int j = 0; j < seats[i].length; j++) {
        if (seats[i][j]) {
          Seat seat = this.getSeat(i, j);

          if (seat != null) {
            seat.buy();
          }
        }
      }
    }
  }

  public Seat getSeat(final int row, final int index) {
    Seat seat = null;

    for (Node node: this.auditorium.getChildren()) {
      if (!Seat.class.isAssignableFrom(node.getClass())) {
        continue;
      }

      if ((GridPane.getRowIndex(node) - 2) == row
          && (GridPane.getColumnIndex(node) - 2) == index) {
        seat = (Seat) node;

        break;
      }
    }

    return seat;
  }

  public boolean[][] getSeats(final Reservation reservation) {
    int rows = reservation.showtime.auditorium.rows;
    int seats = reservation.showtime.auditorium.seats;

    boolean[][] reservedSeats = new boolean[rows][seats];

    for (Ticket ticket: reservation.tickets) {
      reservedSeats[ticket.row][ticket.seat] = true;
    }

    return reservedSeats;
  }

  public boolean[][] getSeats(final Showtime showtime) {
    int rows = showtime.auditorium.rows;
    int seats = showtime.auditorium.seats;

    boolean[][] reservedSeats = new boolean[rows][seats];

    for (Reservation reservation: showtime.reservations) {
      for (Ticket ticket: reservation.tickets) {
        reservedSeats[ticket.row][ticket.seat] = true;
      }
    }

    return reservedSeats;
  }

  /**
   * Render an auditorium.
   *
   * @param auditorium The Auditorium to render.
   */
  public void renderShowtime(final Showtime showtime) {
    this.auditorium.getChildren().clear();

    if (activeReservation != null) {
      activeReservation = null;
      this.phone.setText("");
      this.phone.setDisable(false);
    }

    this.renderAuditoriumLabels(showtime.auditorium);
    this.renderAuditoriumSeats(showtime.auditorium);

    for (Reservation reservation: showtime.reservations) {
      boolean[][] seats = this.getSeats(reservation);

      if (!reservation.bought) {
        this.renderReservedSeats(seats);
      }
      else {
        this.renderBoughtSeats(seats);
      }
    }
  }

  public void renderReservation(final Reservation reservation) {
    this.showtimes.getSelectionModel().clearSelection();

    this.renderShowtime(reservation.showtime);

    this.phone.setText(reservation.phoneNumber + "");
    this.phone.setDisable(true);

    this.renderSelectedSeats(this.getSeats(reservation));

    // Set the active reservation.
    this.activeReservation = reservation;

    // Set the active showtime.
    this.activeShowtime = reservation.showtime;
  }

  public void makeReservation(final String phone, final boolean buy) {
    if (this.selectedSeats.isEmpty()) {
      return;
    }

    Showtime showtime = this.activeShowtime;

    if (showtime == null) {
      return;
    }

    if (phone == null || phone.isEmpty()) {
      return;
    }

    int phoneNumber;

    try {
      phoneNumber = Integer.parseInt(phone);
    }
    catch (NumberFormatException ex) {
      return;
    }

    try {
      Reservation reservation = new Reservation();
      reservation.showtime = showtime;
      reservation.phoneNumber = phoneNumber;
      reservation.bought = buy;
      reservation.insert();

      // Add the reservation to the corresponding showtime.
      showtime.reservations.add(reservation);

      // Add the reservation to the list of reservations.
      ApplicationController.reservations().add(reservation);

      Iterator<Seat> seats = this.selectedSeats.iterator();

      while (seats.hasNext()) {
        // Grab the next seat.
        Seat seat = seats.next();

        // Remove the seat from the list of selected seats.
        seats.remove();

        Ticket ticket = new Ticket();
        ticket.row = seat.getRow();
        ticket.seat = seat.getSeat();
        ticket.reservation = reservation;
        ticket.insert();
        reservation.tickets.add(ticket);

        if (buy) {
          seat.buy();
        }
        else {
          seat.reserve();
        }
      }
    }
    catch (SQLException ex) {
      return;
    }

    this.phone.setText("");
    this.phone.setDisable(false);
    this.activeReservation = null;
  }

  public void editReservation(final Reservation reservation, final boolean buy) {
    try {
      reservation.delete();
      reservation.showtime.reservations.remove(reservation);
      ApplicationController.reservations().removeAll(reservation);
    }
    catch (SQLException ex) {
      return;
    }

    this.makeReservation(reservation.phoneNumber + "", buy);
  }
}
