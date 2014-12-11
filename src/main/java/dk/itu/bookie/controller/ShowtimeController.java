/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
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
import javafx.beans.property.ReadOnlyObjectWrapper;

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

  /**
   * The button for making reservation.
   */
  @FXML
  private Button reserve;

  /**
   * The button for marking reservations as bought.
   */
  @FXML
  private Button buy;

  /**
   * The text field for inputting phone numbers for reservations.
   */
  @FXML
  private TextField phone;

  /**
   * The text field for selecting the number of seats to reserve.
   */
  @FXML
  private TextField seats;

  /**
   * The currently active showtime.
   */
  private ReadOnlyObjectWrapper<Showtime> activeShowtime;

  /**
   * The currently active reservation.
   */
  private ReadOnlyObjectWrapper<Reservation> activeReservation;

  /**
   * The set of selected seats.
   */
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

    this.activeShowtime = new ReadOnlyObjectWrapper<>();
    this.activeReservation = new ReadOnlyObjectWrapper<>();

    this.bindTableColumnWidths();

    this.showtimes.setItems(ApplicationController.showtimes());

    this.showtimes.getSelectionModel().selectedItemProperty().addListener(
      (ob, ov, nv)-> {
        this.activeShowtime.set(nv);
      }
    );

    this.activeShowtime.addListener((ob, ov, nv)-> {
      if (nv != null) {
        this.renderShowtime(nv);
      }
    });

    this.activeReservation.addListener((ob, ov, nv)-> {
      if (nv != null) {
        this.renderReservation(nv);
      }
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
      if (this.activeReservation == null) {
        this.makeReservation(false);
      }
      else {
        this.editReservation(false);
      }
    });

    this.buy.setOnAction((e) -> {
      if (this.activeReservation == null) {
        this.makeReservation(true);
      }
      else {
        this.editReservation(true);
      }
    });
  }

  /**
   * Set the currently active reservation.
   *
   * @param reservation The reservation to set as the active reservation.
   */
  public void setActiveReservation(final Reservation reservation) {
    this.activeReservation.set(reservation);
  }

  /**
   * Bind the widths of the individual columns to the entire width of the
   * containing table.
   */
  private void bindTableColumnWidths() {
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

  /**
   * Given an auditorium, render its seat labels.
   *
   * @param auditorium The auditorium whose seat labels to render.
   */
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

  /**
   * Given an auditorium, render its seats.
   *
   * @param auditorium The auditorium whose seats to render.
   */
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

  /**
   * Given a list of selected seats, render them in the current auditorium.
   *
   * @param seats The list of selected seats to render.
   */
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

  /**
   * Given a list of reserved seats, render them in the current auditorium.
   *
   * @param seats The list of reserved seats to render.
   */
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

  /**
   * Given a list of bought seats, render them in the current auditorium.
   *
   * @param seats The list of bought seats to render.
   */
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

  /**
   * Get a seat from the current auditorium.
   *
   * @param row   The row of the seat.
   * @param index The seat number of the seat.
   * @return      The seat if found, otherwise null.
   */
  private Seat getSeat(final int row, final int index) {
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

  /**
   * Get the reserved seats of a reservation.
   *
   * @param reservation The reservation whose reserved seats to get.
   * @return            A double boolean array indicating reserved seats.
   */
  private boolean[][] getSeats(final Reservation reservation) {
    int rows = reservation.showtime.auditorium.rows;
    int seats = reservation.showtime.auditorium.seats;

    boolean[][] reservedSeats = new boolean[rows][seats];

    for (Ticket ticket: reservation.tickets) {
      reservedSeats[ticket.row][ticket.seat] = true;
    }

    return reservedSeats;
  }

  /**
   * Get the reserved seats of a showtime.
   *
   * @param showtime  The showtime whose reserved seats to get.
   * @return          A double boolean array indicating reserved seats.
   */
  private boolean[][] getSeats(final Showtime showtime) {
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
   * Render the auditorium of a showtime.
   *
   * @param showtime The showtime whose auditorium to render.
   */
  private void renderShowtime(final Showtime showtime) {
    this.auditorium.getChildren().clear();

    if (this.activeReservation != null) {
      this.activeReservation = null;
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

  /**
   * Render the auditorium of a reservation.
   *
   * @param reservation The reservation whose auditorium to render.
   */
  private void renderReservation(final Reservation reservation) {
    this.showtimes.getSelectionModel().select(reservation.showtime);

    this.phone.setText(reservation.phoneNumber + "");
    this.phone.setDisable(true);

    this.renderSelectedSeats(this.getSeats(reservation));
  }

  /**
   * Reserve the currently selected seats.
   *
   * @param buy Whether or not to mark the reservation as bought.
   */
  private void makeReservation(final boolean buy) {
    if (this.selectedSeats.isEmpty()) {
      return;
    }

    String phone = this.phone.getText();

    Showtime showtime = this.activeShowtime.get();

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

  /**
   * Edit the currently active reservation.
   *
   * @param buy Whether or not to mark the reservation as bought.
   */
  private void editReservation(final boolean buy) {
    Reservation reservation = this.activeReservation.get();

    try {
      reservation.delete();
      reservation.showtime.reservations.remove(reservation);
      ApplicationController.reservations().removeAll(reservation);
    }
    catch (SQLException ex) {
      return;
    }

    this.makeReservation(buy);
  }
}
