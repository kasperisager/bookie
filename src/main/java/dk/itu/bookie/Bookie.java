/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie;

// General utilities
import java.sql.SQLException;
import java.util.Properties;

// JavaFX utilities
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;

// FXML utilities
import javafx.fxml.FXMLLoader;

// Donkey utilities
import dk.itu.donkey.Database;
import dk.itu.donkey.Driver;

// Controllers
import dk.itu.bookie.controller.ErrorController;

/**
 * Bookie class.
 *
 * @version 1.0.0
 */
public final class Bookie extends Application {
  /**
   * Database instance for the application.
   */
  private static Database db;

  /**
   * Initialize database instance or get it if it's already set.
   *
   * @return The database instance for the application.
   */
  public static Database db() {
    if (Bookie.db == null) {
      Properties config = new Properties();
      config.put("database", "Bookie");

      Bookie.db = new Database(Driver.SQLITE, config);
    }

    return Bookie.db;
  }

  /**
   * Launch the application.
   *
   * @param args Runtime arguments.
   *
   * @throws SQLException In case of a SQL exception during seeding.
   */
  public static void main(final String[] args) throws SQLException {
    // Clear the demo database.
    Seeder.reset();

    // Initialize the demo database.
    Seeder.init();

    // Liftoff!
    Bookie.launch(args);
  }

  /**
   * Start the JavaFX thread and hand off control to the primary controller.
   *
   * @param primaryStage The primary stage of the application.
   */
  @Override
  public void start(final Stage primaryStage) {
    Thread.currentThread().setUncaughtExceptionHandler(
      ErrorController::crash
    );

    try {
      FXMLLoader fxmlLoader = new FXMLLoader();

      Parent root = fxmlLoader.load(
        this.getClass().getResource("view/Application.fxml")
      );

      Scene scene = new Scene(root);

      scene.getStylesheets().add(
        this.getClass().getResource("stylesheet/Main.css").toExternalForm()
      );

      primaryStage.setTitle("Bookie");
      primaryStage.setScene(scene);
      primaryStage.setResizable(false);
      primaryStage.show();
    }
    catch (Throwable t) {
      ErrorController.crash(Thread.currentThread(), t);
    }
  }
}
