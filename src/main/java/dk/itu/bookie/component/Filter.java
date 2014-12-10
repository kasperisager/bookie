/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.component;

// JavaFX layout
import javafx.scene.layout.VBox;

// JavaFX controls
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
   * Text filter type.
   */
  public static final String TEXT = "text";

  /**
   * Date filter type.
   */
  public static final String DATE = "date";

  /**
   * Initialize a filter.
   *
   * @param label The label to add to the filter.
   * @param type  The type of the filter.
   */
  public Filter(final String label, final String type) {
    this.getStyleClass().add("filter");

    PopOver popOver = new PopOver();
    popOver.setDetachable(false);
    popOver.setAutoHide(true);

    GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

    this.setGraphic(fontAwesome.create("filter").size(10));

    this.getStyleClass().add("button-small");

    this.selectedProperty().addListener((ob, ov, nv) -> {
      if (nv) {
        popOver.show(this);
      }
      else {
        popOver.hide();
      }
    });

    popOver.showingProperty().addListener((ob, ov, nv) -> {
      if (!nv && this.isSelected()) {
        this.setSelected(false);
      }
    });

    VBox filterContent = new VBox();
    filterContent.setSpacing(6);
    filterContent.getStyleClass().add("filter-content");

    Label filterLabel = new Label(label);
    filterLabel.getStyleClass().add("filter-label");

    filterContent.getChildren().add(filterLabel);

    switch (type) {
      case TEXT:
        TextField textField = new TextField();
        textField.setPromptText("Søg...");

        filterContent.getChildren().add(textField);
        break;
      case DATE:
      default:
        DatePicker datePickerFrom = new DatePicker();
        datePickerFrom.setPromptText("Fra");
        datePickerFrom.setShowWeekNumbers(true);

        DatePicker datePickerTo = new DatePicker();
        datePickerTo.setPromptText("Til (valgfri)");
        datePickerTo.setShowWeekNumbers(true);

        filterContent.getChildren().add(datePickerFrom);
        filterContent.getChildren().add(datePickerTo);
    }

    popOver.setContentNode(filterContent);
  }
}
