/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// JavaFX properties
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

// Base model
import dk.itu.bookie.FXModel;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Movie class.
 *
 * @version 1.0.0
 */
public final class Movie extends FXModel {
  /**
   * The name of the movie.
   */
  public StringProperty name =
    new SimpleStringProperty();

  /**
   * Initialize a movie.
   */
  public Movie() {
    super("movies", Bookie.db());
  }
}
