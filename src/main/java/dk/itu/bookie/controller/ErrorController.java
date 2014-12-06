/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// ControlsFX
import org.controlsfx.dialog.Dialogs;

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
    Dialogs
      .create()
      .title("Whoops!")
      .masthead("Bookie crashed!")
      .message(
        "Bookie detected an unexpected error and had to shut down."
      + "\n\nThe details below have been passed along to our team of highly"
      + " trained marmosets for further analysis.\n"
      )
      .showException(throwable);

    System.exit(1);
  }
}
