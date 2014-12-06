/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.Date;
import java.util.ResourceBundle;

// Net utilities
import java.net.URL;

// JavaFX controls
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

// JavaFX properties
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// FXML utilities
import javafx.fxml.FXML;

// Models
import dk.itu.bookie.model.Showtime;
import dk.itu.bookie.model.Reservation;

/**
 * Reservation controller class.
 *
 * @version 1.0.0
 */
public class ReservationController {
  private static ReservationController instance;

  @FXML
  private TableView<Reservation> reservations;

  @FXML
  private TableColumn<Reservation, Number> phoneColumn;

  @FXML
  private TableColumn<Reservation, Number> ticketColumn;

  @FXML
  private TableColumn<Reservation, String> statusColumn;

  @FXML
  private TableColumn<Reservation, String> movieColumn;

  @FXML
  private TableColumn<Reservation, String> auditoriumColumn;

  @FXML
  private TableColumn<Reservation, String> dateColumn;

  @FXML
  private TableColumn<Reservation, String> timeColumn;

  @FXML
  private TableColumn<Reservation, String> actionsColumn;

  public ReservationController getInstance() {
    return ReservationController.instance;
  }

  public void initialize() throws Exception {
    ReservationController.instance = this;

    this.bindTableColumnWidths();

    this.reservations.setItems(ApplicationController.reservations());

    this.phoneColumn.setCellValueFactory((data) -> {
      return new SimpleIntegerProperty(
        data.getValue().phoneNumber
      );
    });

    this.ticketColumn.setCellValueFactory((data) -> {
      return new SimpleIntegerProperty(
        data.getValue().tickets.size()
      );
    });

    this.movieColumn.setCellValueFactory((data) -> {
      return new SimpleStringProperty(data.getValue().showtime.movie.name);
    });

    this.auditoriumColumn.setCellValueFactory((data) -> {
      return new SimpleStringProperty(
        data.getValue().showtime.auditorium.name
      );
    });

    this.dateColumn.setCellValueFactory((data) -> {
      return new SimpleStringProperty(data.getValue().showtime.date());
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
      return new SimpleStringProperty(data.getValue().showtime.time());
    });
  }

  public void bindTableColumnWidths() {
    this.phoneColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().subtract(18).multiply(0.15)
    );

    this.ticketColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().subtract(18).multiply(0.10)
    );

    this.statusColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().subtract(18).multiply(0.10)
    );

    this.movieColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().subtract(18).multiply(0.15)
    );

    this.auditoriumColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().subtract(18).multiply(0.10)
    );

    this.dateColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().subtract(18).multiply(0.10)
    );

    this.timeColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().subtract(18).multiply(0.10)
    );

    this.actionsColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().subtract(18).multiply(0.20)
    );
  }
}
