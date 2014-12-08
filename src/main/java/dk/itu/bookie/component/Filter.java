/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.component;

// JavaFX controls
import javafx.scene.control.ToggleButton;

// ControlsFX
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

/**
 * Filter class.
 *
 * @version 1.0.0
 */
public final class Filter extends ToggleButton {
  /**
   * Filter pop over content.
   */
  private PopOver content = new PopOver();

  /**
   * Initialize a filter.
   *
   * @param label The label to add to the filter.
   */
  public Filter(final String label) {
    GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

    this.content.setDetachedTitle(label);

    this.setGraphic(fontAwesome.create("filter").size(10));

    this.getStyleClass().add("button-small");

    this.selectedProperty().addListener((ob, ov, nv) -> {
      if (nv) {
        this.content.show(this);
      }
      else {
        this.content.hide();
      }
    });

    this.content.showingProperty().addListener((ob, ov, nv) -> {
      if (!nv && this.isSelected()) {
        this.setSelected(false);
      }
    });
  }
}
