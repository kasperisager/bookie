/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

// General utilities
import java.util.ResourceBundle;

// Net utilities
import java.net.URL;

// FXML utilities
import javafx.fxml.Initializable;

/**
 * Application controller class.
 *
 * @version 1.0.0
 */
public final class ApplicationController implements Initializable {
  private static ApplicationController instance;

  public ApplicationController getInstance() {
    return ApplicationController.instance;
  }

  public void initialize(URL url, ResourceBundle resourceBundle) {
    ApplicationController.instance = this;
  }
}
