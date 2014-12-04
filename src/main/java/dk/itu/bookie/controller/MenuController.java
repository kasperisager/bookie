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
 * Menu controller class.
 *
 * @version 1.0.0
 */
public final class MenuController implements Initializable {
  private static MenuController instance;

  public MenuController getInstance() {
    return MenuController.instance;
  }

  public void initialize(URL url, ResourceBundle resourceBundle) {
    MenuController.instance = this;
  }
}
