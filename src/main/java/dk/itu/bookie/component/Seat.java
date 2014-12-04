/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.component;

// JavaFX shapes
import javafx.scene.shape.Rectangle;

// JavaFX paint
import javafx.scene.paint.Color;

// JavaFX properties
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Seat class.
 *
 * @since 1.0.0 Initial release.
 */
public final class Seat extends Rectangle {
  private int row;
  private int seat;

  private BooleanProperty reserved = new SimpleBooleanProperty(false);
  private BooleanProperty bought = new SimpleBooleanProperty(false);
  private BooleanProperty selected = new SimpleBooleanProperty(false);

  public Seat(final int row, final int seat) {
    super(40, 40);

    this.row = row;
    this.seat = seat;

    this.getStyleClass().add("seat");

    this.setOnMouseClicked(e -> {
      if (!this.selected.get()) {
        this.select();
      }
      else {
        this.deselect();
      }
    });
  }

  public void select() {
    // If this ticket has been either reserved or bought, don't allow that it
    // be selected.
    if (this.reserved.get() || this.bought.get()) {
      return;
    }

    this.getStyleClass().add("seat-selected");

    this.selected.set(true);
  }

  public void deselect() {
    // If this ticket has been either reserved or bought, don't allow that it
    // be selected.
    if (this.reserved.get() || this.bought.get()) {
      return;
    }

    this.getStyleClass().remove("seat-selected");

    this.selected.set(false);
  }

  public void reserve() {
    // If this ticket has been bought, don't allow that it be reserved.
    if (this.bought.get()) {
      return;
    }

    this.getStyleClass().remove("seat-selected");
    this.getStyleClass().add("seat-reserved");

    this.reserved.set(true);
  }

  public void buy() {
    this.getStyleClass().remove("seat-selected");
    this.getStyleClass().remove("seat-reserved");
    this.getStyleClass().add("seat-bought");

    this.bought.set(true);
  }

  public BooleanProperty getState() {
    return this.selected;
  }
}
