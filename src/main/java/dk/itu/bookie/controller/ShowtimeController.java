/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.List;
import java.util.ResourceBundle;

// Net utilities
import java.net.URL;

// SQL utilities
import java.sql.SQLException;

// JavaFX controls
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;

// JavaFX collections
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// FXML utilities
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

// Models
import dk.itu.bookie.model.Movie;

/**
 * Showtime controller class.
 *
 * @version 1.0.0
 */
public class ShowtimeController implements Initializable {
  @FXML
  private ListView movies;

  public void initialize(
    final URL url,
    final ResourceBundle resourceBundle
  ) {
    try {
      List<Movie> movies = Movie.find(Movie.class).orderBy("name") .get();

      ObservableList<String> listMovies = FXCollections.observableArrayList();

      for (Movie movie: movies) {
        listMovies.add(movie.name);
      }

      this.movies.setItems(listMovies);
    }
    catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }
}
