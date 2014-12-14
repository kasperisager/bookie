/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// JavaFX collections
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// JavaFX properties
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

// Base model
import dk.itu.bookie.FXModel;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Reservation class.
 *
 * @version 1.0.0
 */
public final class Reservation extends FXModel {
  /**
   * The phone number for identifying the reservation.
   */
  public IntegerProperty phoneNumber =
    new SimpleIntegerProperty();

  /**
   * Has the reservation been payed for?
   */
  public BooleanProperty bought =
    new SimpleBooleanProperty();

  /**
   * The showtime that the reservation is for.
   */
  public ObjectProperty<Showtime> showtime =
    new SimpleObjectProperty<>();

  /**
   * The reserved tickets.
   */
  public ObservableList<Ticket> tickets =
    FXCollections.observableArrayList();

  /**
   * Initialize a reservation.
   */
  public Reservation() {
    super("reservations", Bookie.db());
  }
}
