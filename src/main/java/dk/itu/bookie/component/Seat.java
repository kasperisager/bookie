/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.component;

// JavaFX shapes
import javafx.scene.shape.Rectangle;

// JavaFX paint
import javafx.scene.paint.Color;

/**
 * Seat class.
 *
 * @since 1.0.0 Initial release.
 */
public final class Seat extends Rectangle {
  private static final Color COLOR_FILL = Color.web("#61BD6D");
  private static final Color COLOR_STROKE = Color.web("#41A85F");

  private static final Color COLOR_FILL_ACTIVE = Color.web("#475577");
  private static final Color COLOR_STROKE_ACTIVE = Color.web("#28324e");

  private static final Color COLOR_FILL_RESERVED = Color.web("#54acd2");
  private static final Color COLOR_STROKE_RESERVED = Color.web("#3D8EB9");

  private static final Color COLOR_FILL_BOUGHT = Color.web("#D14841");
  private static final Color COLOR_STROKE_BOUGHT = Color.web("#B8312F");

  private int row;
  private int seat;

  private boolean selected;

  public Seat(final int row, final int seat) {
    // Set the size of the seat.
    super(40, 40);

    this.row = row;
    this.seat = seat;

    // Set fill color of the seat.
    this.setFill(this.COLOR_FILL);

    // Add a stroke around the seat.
    this.setStroke(this.COLOR_STROKE);

    // Add rounded corners.
    this.setArcWidth(6);
    this.setArcHeight(6);

    this.setOnMouseClicked(e -> {
      this.select();
    });
  }

  public void select() {
    System.out.println("\nRow:  " + this.row);
    System.out.println("Seat: " + this.seat);

    if (!this.selected) {
      this.setFill(this.COLOR_FILL_ACTIVE);
      this.setStroke(this.COLOR_STROKE_ACTIVE);
    }
    else {
      this.setFill(this.COLOR_FILL);
      this.setStroke(this.COLOR_STROKE);
    }

    this.selected = !this.selected;
  }
}
