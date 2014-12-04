/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.ResourceBundle;

// Net utilities
import java.net.URL;

// JavaFX controls
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

// FXML utilities
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

// Models
import dk.itu.bookie.model.Reservation;

/**
 * Reservation controller class.
 *
 * @version 1.0.0
 */
public class ReservationController implements Initializable {
  private static ReservationController instance;

  @FXML
  private TableView<Reservation> reservations;

  @FXML
  private TableColumn phoneColumn;

  @FXML
  private TableColumn ticketColumn;

  @FXML
  private TableColumn statusColumn;

  @FXML
  private TableColumn movieColumn;

  @FXML
  private TableColumn auditoriumColumn;

  @FXML
  private TableColumn dateColumn;

  @FXML
  private TableColumn timeColumn;

  @FXML
  private TableColumn actionsColumn;

  public ReservationController getInstance() {
    return ReservationController.instance;
  }

  public void initialize(final URL url, final ResourceBundle resourceBundle) {
    ReservationController.instance = this;

    this.bindTableColumnWidths();
  }

  public void bindTableColumnWidths() {
    this.phoneColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().multiply(0.15)
    );

    this.ticketColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().multiply(0.10)
    );

    this.statusColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().multiply(0.10)
    );

    this.movieColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().multiply(0.15)
    );

    this.auditoriumColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().multiply(0.10)
    );

    this.dateColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().multiply(0.10)
    );

    this.timeColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().multiply(0.10)
    );

    this.actionsColumn.prefWidthProperty().bind(
      this.reservations.widthProperty().multiply(0.20)
    );
  }
}
