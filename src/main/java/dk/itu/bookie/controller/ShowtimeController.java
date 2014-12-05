/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

// JavaFX geometry
import javafx.geometry.HPos;
import javafx.geometry.VPos;

// JavaFX collections
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// JavaFX properties
import javafx.beans.property.SimpleStringProperty;

// FXML utilities
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

// Donkey utilities
import dk.itu.donkey.Model;

// Components
import dk.itu.bookie.component.Seat;

// Models
import dk.itu.bookie.model.Auditorium;
import dk.itu.bookie.model.Showtime;
import dk.itu.bookie.model.Movie;

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
  private TableColumn<Showtime, String> movieColumn;

  @FXML
  private TableColumn<Showtime, String> auditoriumColumn;

  @FXML
  private TableColumn<Showtime, String> dateColumn;

  @FXML
  private TableColumn<Showtime, String> timeColumn;

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

        // Center alignment the label within its column.
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

    List<Showtime> models = null;

    try {
      models = Model.findAll(Showtime.class);
    }
    catch (Exception e) {
      return;
    }

    ObservableList<Showtime> showtimes = FXCollections.observableArrayList(models);

    this.showtimes.setItems(showtimes);

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

  public void bindTableColumnWidths() {
    this.movieColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().multiply(0.35)
    );

    this.auditoriumColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().multiply(0.35)
    );

    this.dateColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().multiply(0.20)
    );

    this.timeColumn.prefWidthProperty().bind(
      this.showtimes.widthProperty().multiply(0.10)
    );
  }

  public void bindSeatWidth(final Seat seat, final int seats) {
    seat.widthProperty().bind(
      this.showtimes.widthProperty().divide(seats)
    );

    seat.heightProperty().bind(
      this.showtimes.widthProperty().divide(seats)
    );
  }
}
