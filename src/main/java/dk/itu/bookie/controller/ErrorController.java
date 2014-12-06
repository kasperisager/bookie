/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// IO utilities
import java.io.PrintWriter;
import java.io.StringWriter;

// JavaFX layouts
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

// JavaFX controls
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Error controller class.
 *
 * @version 1.0.0
 */
public final class ErrorController {
  /**
   * "Graceful" crash handling.
   */
  public static void crash(final Thread thread, final Throwable throwable) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Whoops!");
    alert.setHeaderText("Bookie crashed!");
    alert.setContentText(
      "Bookie detected an unexpected error and had to shut down."
    + "\n\nThe details below have been passed along to our team of highly"
    + " trained marmosets for further analysis.\n"
    );

    // Create expandable Exception.
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    throwable.printStackTrace(pw);
    String exceptionText = sw.toString();

    Label label = new Label("Something went wrong about here-ish:");

    TextArea textArea = new TextArea(exceptionText);
    textArea.setEditable(false);
    textArea.setWrapText(true);

    VBox expContent = new VBox();
    expContent.setSpacing(10);
    expContent.getChildren().add(label);
    expContent.getChildren().add(textArea);

    // Set expandable Exception into the dialog pane.
    alert.getDialogPane().setExpandableContent(expContent);

    alert.showAndWait();

    System.exit(1);
  }
}
