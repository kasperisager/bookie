/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.component;

// JavaFX shapes
import javafx.scene.shape.Rectangle;

// JavaFX properties
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Seat class.
 *
 * @since 1.0.0 Initial release.
 */
public final class Seat extends Rectangle {
  /**
   * The row number of the seat.
   */
  private int row;

  /**
   * The seat number (column) of the seat.
   */
  private int seat;

  /**
   * Whether or not the seat is reserved.
   */
  private BooleanProperty reserved = new SimpleBooleanProperty(false);

  /**
   * Whether or not the seat is bought.
   */
  private BooleanProperty bought = new SimpleBooleanProperty(false);

  /**
   * Whether or not the seat is selected for reservation.
   */
  private BooleanProperty selected = new SimpleBooleanProperty(false);

  /**
   * Initialize a seat.
   *
   * @param row   The row number of the seat.
   * @param seat  The seat number (column) of the seat.
   */
  public Seat(final int row, final int seat) {
    this.row = row;
    this.seat = seat;

    this.getStyleClass().add("seat");

    this.selected.addListener((ob, ov, nv) -> {
      if (nv) {
        this.getStyleClass().add("seat-selected");
      }
      else {
        this.getStyleClass().remove("seat-selected");
      }
    });

    this.reserved.addListener((ob, ov, nv) -> {
      if (nv) {
        this.getStyleClass().add("seat-reserved");
      }
      else {
        this.getStyleClass().remove("seat-reserved");
      }
    });

    this.bought.addListener((ob, ov, nv) -> {
      if (nv) {
        this.getStyleClass().add("seat-bought");
      }
      else {
        this.getStyleClass().remove("seat-bought");
      }
    });

    this.setOnMouseClicked(e -> {
      // If this ticket has been either reserved or bought, don't allow that it
      // be selected.
      if (this.reserved.get() || this.bought.get()) {
        return;
      }

      if (!this.selected.get()) {
        this.select();
      }
      else {
        this.deselect();
      }
    });
  }

  /**
   * Get the row of the seat.
   *
   * @return The row of the seat.
   */
  public int getRow() {
    return this.row;
  }

  /**
   * Get the seat number of the seat.
   *
   * @return The seat number of the seat.
   */
  public int getSeat() {
    return this.seat;
  }

  /**
   * Select a seat.
   */
  public void select() {
    this.reserved.set(false);
    this.bought.set(false);
    this.selected.set(true);
  }

  /**
   * De-select a seat.
   */
  public void deselect() {
    if (!this.selected.get()) {
      return;
    }

    this.selected.set(false);
  }

  /**
   * Reserve a seat.
   */
  public void reserve() {
    this.selected.set(false);
    this.reserved.set(true);
  }

  /**
   * Buy a seat.
   */
  public void buy() {
    this.selected.set(false);
    this.reserved.set(true);
    this.bought.set(true);
  }

  /**
   * Get the state of the seat (selected or not).
   *
   * @return Boolean property describing whether or not the seat is selected.
   */
  public BooleanProperty getState() {
    return this.selected;
  }
}
