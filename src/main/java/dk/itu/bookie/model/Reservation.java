/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.model;

// SQL utilities
import java.sql.SQLException;

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
   * Regular expression for matching 8-digit phone number.
   */
  private static final String PHONE_REGEX = "^[1-9][0-9]{7}$";

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

  /**
   * Create a reservation.
   *
   * @return Boolean indicating whether or not the query was performed.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Override
  public boolean insert() throws SQLException {
    if (!this.validatePhoneNumber(this.phoneNumber.get())) {
      throw new IllegalArgumentException("Invalid phone number.");
    }

    boolean inserted = super.insert();

    this.showtime.get().reservations.addAll(this);

    return inserted;
  }

  /**
   * Delete a reservation.
   *
   * @return Boolean indicating whether or not the query was performed.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Override
  public boolean delete() throws SQLException {
    boolean deleted = super.delete();

    this.showtime.get().reservations.removeAll(this);

    return deleted;
  }

  /**
   * Get the regular expression for validating phone numbers.
   *
   * @return The phone validation regex.
   */
  public static String getPhoneNumberValidationRegex() {
    return Reservation.PHONE_REGEX;
  }

  /**
   * Validate a phone number.
   *
   * @param number  The phone number to validate.
   * @return        Whether or not the string is a valid phone number.
   */
  public static boolean validatePhoneNumber(final String number) {
    return number.matches(Reservation.PHONE_REGEX);
  }

  /**
   * Validate a phone number.
   *
   * @param number  The phone number to validate.
   * @return        Whether or not the integer is a valid phone number.
   */
  public static boolean validatePhoneNumber(final Integer number) {
    return Reservation.validatePhoneNumber(number.toString());
  }
}
