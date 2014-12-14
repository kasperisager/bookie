/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.Date;

// SQL utilities
import java.sql.SQLException;

// JavaFX controls
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

// JavaFX layouts
import javafx.scene.layout.HBox;

// JavaFX paint
import javafx.scene.paint.Color;

// JavaFX collections
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

// JavaFX properties
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

// JavaFX bindings
import javafx.beans.binding.DoubleBinding;

// FXML utilities
import javafx.fxml.FXML;

// ControlsFX
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import org.controlsfx.control.action.Action;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

// Models
import dk.itu.bookie.model.Showtime;
import dk.itu.bookie.model.Reservation;

/**
 * Reservation controller class.
 *
 * @version 1.0.0
 */
public final class ReservationController {
  /**
   * The singleton instance of the controller.
   */
  private static ReservationController instance;

  /**
   * Table containing the different reservations.
   */
  @FXML
  private TableView<Reservation> reservations;

  /**
   * The column containing the phone number of the reservation.
   */
  @FXML
  private TableColumn<Reservation, Number> phoneColumn;

  /**
   * The text field for filtering the phone column.
   */
  @FXML
  private TextField phoneFilter;

  /**
   * The column containing the number of tickets reserved.
   */
  @FXML
  private TableColumn<Reservation, Number> ticketColumn;

  /**
   * The column containing the name of the movie playing.
   */
  @FXML
  private TableColumn<Reservation, String> movieColumn;

  /**
   * The column containing the name of the auditorium that the movie is playing
   * in.
   */
  @FXML
  private TableColumn<Reservation, String> auditoriumColumn;

  /**
   * The column containing the date that the movie is playing.
   */
  @FXML
  private TableColumn<Reservation, String> dateColumn;

  /**
   * The column containing the time at which the movie is playing.
   */
  @FXML
  private TableColumn<Reservation, String> timeColumn;

  /**
   * The column containing the different actions that can be taken for each
   * reservation.
   */
  @FXML
  private TableColumn<Reservation, Reservation> actionsColumn;

  /**
   * Get the singleton instance of the controller.
   *
   * @return The singleton Reservation controller.
   */
  public static ReservationController getInstance() {
    return ReservationController.instance;
  }

  /**
   * Initialize the controller.
   *
   * @throws Exception In case of uncaught errors.
   */
  public void initialize() throws Exception {
    ReservationController.instance = this;

    this.bindTableColumnWidths();

    FilteredList<Reservation> filteredReservations = new FilteredList<>(
      ApplicationController.reservations(), p -> true
    );

    this.phoneColumn.setCellValueFactory((data) -> {
      return data.getValue().phoneNumber;
    });

    this.phoneFilter.textProperty().addListener((ob, ov, nv) -> {
      filteredReservations.setPredicate(reservation -> {
        if (nv == null || nv.isEmpty()) {
          return true;
        }

        if (reservation.phoneNumber.toString().indexOf(nv) != -1) {
          return true;
        }

        return false;
      });
    });

    SortedList<Reservation> sortedReservations = new SortedList<>(
      filteredReservations
    );

    sortedReservations.comparatorProperty().bind(
      this.reservations.comparatorProperty()
    );

    this.reservations.setItems(sortedReservations);

    this.ticketColumn.setCellValueFactory((data) -> {
      return new SimpleIntegerProperty(
        data.getValue().tickets.size()
      );
    });

    this.movieColumn.setCellValueFactory((data) -> {
      return data.getValue().showtime.get().movie.get().name;
    });

    this.auditoriumColumn.setCellValueFactory((data) -> {
      return data.getValue().showtime.get().auditorium.get().name;
    });

    this.dateColumn.setCellValueFactory((data) -> {
      return data.getValue().showtime.get().date();
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
      return data.getValue().showtime.get().time();
    });

    this.actionsColumn.setCellValueFactory((data) -> {
      return new ReadOnlyObjectWrapper(data.getValue());
    });

    this.actionsColumn.setCellFactory((column) -> {
      return new ButtonCell();
    });
  }

  /**
   * Bind the widths of the individual columns to the entire width of the
   * containing table.
   */
  public void bindTableColumnWidths() {
    DoubleBinding tableWidth = this.reservations.widthProperty().subtract(128);

    this.phoneColumn.prefWidthProperty().bind(tableWidth.multiply(0.20));

    this.ticketColumn.prefWidthProperty().bind(tableWidth.multiply(0.10));

    this.movieColumn.prefWidthProperty().bind(tableWidth.multiply(0.30));

    this.auditoriumColumn.prefWidthProperty().bind(tableWidth.multiply(0.10));

    this.dateColumn.prefWidthProperty().bind(tableWidth.multiply(0.20));

    this.timeColumn.prefWidthProperty().bind(tableWidth.multiply(0.10));
  }

  /**
   * Delete a reservation.
   *
   * @param reservation The reservation to delete.
   */
  public void deleteReservation(final Reservation reservation) {
    Action response = Dialogs
      .create()
      .title("Bekræft handling")
      .masthead("Er du sikker på, at du vil slette reservationen?")
      .actions(Dialog.ACTION_OK, Dialog.ACTION_CANCEL)
      .showConfirm();

    if (response == Dialog.ACTION_CANCEL) {
      return;
    }

    try {
      // Attempt deleting the reservation from the database. If this fails,
      // a SQL exception will be thrown.
      reservation.delete();

      // Once the reservation has been deleted from the database, remove it
      // from the list of reservations.
      ApplicationController
        .reservations()
        .removeAll(reservation);

      // Lastly, remove the reservation from the list of reservations in
      // the associated showtime. Circular relations FTW!
      reservation.showtime.get().reservations.remove(reservation);
    }
    catch (SQLException ex) {
      return;
    }
  }

  /**
   * Custom cell with support for button actions.
   */
  private class ButtonCell extends TableCell<Reservation, Reservation> {
    /**
     * Render the cell.
     *
     * @param reservation The reservation associated with the row.
     * @param empty       Whether or not the cell has content.
     */
    protected void updateItem(
      final Reservation reservation,
      final boolean empty
    ) {
      super.updateItem(reservation, empty);

      GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

      HBox buttons = new HBox();
      buttons.setSpacing(4);

      Button editButton = new Button("Redigér");

      editButton.setOnAction((e) -> {
        ShowtimeController.getInstance().setActiveReservation(reservation);
        ApplicationController.getInstance().toShowtimes();
      });

      Button deleteButton = new Button(
        "", fontAwesome.create("trash_alt").color(Color.WHITE)
      );
      deleteButton.getStyleClass().add("button-danger");

      deleteButton.setOnAction((e) -> {
        ReservationController.getInstance().deleteReservation(reservation);
      });

      buttons.getChildren().add(editButton);
      buttons.getChildren().add(deleteButton);

      if (!empty) {
        this.setGraphic(buttons);
      }
      else {
        this.setGraphic(null);
      }
    }
  }
}
