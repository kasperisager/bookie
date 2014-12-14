/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// JavaFX properties
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

// Base model
import dk.itu.bookie.FXModel;

// Main application
import dk.itu.bookie.Bookie;

/**
 * Ticket class.
 *
 * @version 1.0.0
 */
public final class Ticket extends FXModel {
  /**
   * The row number of the ticket.
   */
  public IntegerProperty row =
    new SimpleIntegerProperty();

  /**
   * The seat number of the ticket.
   */
  public IntegerProperty seat =
    new SimpleIntegerProperty();

  /**
   * The reservation that this ticket is part of.
   */
  public ObjectProperty<Reservation> reservation =
    new SimpleObjectProperty<>();

  /**
   * Initialize a ticket.
   */
  public Ticket() {
    super("tickets", Bookie.db());
  }
}
