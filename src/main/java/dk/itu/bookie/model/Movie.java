/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// Base model
import dk.itu.donkey.Model;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Movie class.
 *
 * @version 1.0.0
 */
public class Movie extends Model {
  /**
   * The name of the movie.
   */
  public String name;

  /**
   * Initialize a movie.
   */
  public Movie() {
    super("movies", Bookie.db());
  }
}
