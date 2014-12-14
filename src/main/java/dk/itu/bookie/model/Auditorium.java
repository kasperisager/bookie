/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// JavaFX properties
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// Base model
import dk.itu.bookie.FXModel;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Auditorium class.
 *
 * @version 1.0.0
 */
public final class Auditorium extends FXModel {
  /**
   * The name of the auditorium.
   */
  public StringProperty name =
    new SimpleStringProperty();

  /**
   * Number of rows in the auditorium.
   */
  public IntegerProperty rows =
    new SimpleIntegerProperty();

  /**
   * Number of seats per row in the auditorium.
   */
  public IntegerProperty seats =
    new SimpleIntegerProperty();

  /**
   * Initialize an auditorium.
   */
  public Auditorium() {
    super("auditoriums", Bookie.db());
  }
}
