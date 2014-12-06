/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// IO utilities
import java.io.PrintWriter;
import java.io.StringWriter;

// JavaFX layouts
import javafx.scene.layout.VBox;

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
   * The singleton instance of the controller.
   */
  private static ErrorController instance;

  /**
   * Get the singleton instance of the controller.
   *
   * @return The singleton Error controller.
   */
  public ErrorController getInstance() {
    return ErrorController.instance;
  }

  /**
   * Initialize the controller.
   *
   * @throws Exception In case of uncaught errors.
   */
  public void initialize() throws Exception {
    ErrorController.instance = this;
  }

  /**
   * "Graceful" crash handling.
   *
   * @param thread    The thread in which the crash happened.
   * @param throwable The exception that caused the crash.
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
