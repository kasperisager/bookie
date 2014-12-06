/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie.controller;

/**
 * Menu controller class.
 *
 * @version 1.0.0
 */
public final class MenuController {
  /**
   * The singleton instance of the controller.
   */
  private static MenuController instance;

  /**
   * Get the singleton instance of the controller.
   *
   * @return The singleton Menu controller.
   */
  public MenuController getInstance() {
    return MenuController.instance;
  }

  /**
   * Initialize the controller.
   *
   * @throws Exception In case of uncaught errors.
   */
  public void initialize() throws Exception {
    MenuController.instance = this;
  }
}
