/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie;

// General utilities
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

// IO utilities
import java.io.IOException;

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
      config.put("database", "sqlite");

      Bookie.db = new Database(Driver.SQLITE, config);
    }

    return Bookie.db;
  }

  /**
   * Launch the application.
   *
   * @param args Runtime arguments.
   */
  public static void main(final String[] args) throws SQLException {
    Seeder.reset();
    Seeder.init();
    Bookie.launch(args);
  }

  /**
   * Start the JavaFX thread and hand off control to the primary controller.
   *
   * @param primaryStage The primary stage of the application.
   *
   * @throws IOException If the main FXML-view or CSS stylesheet cannot load.
   */
  @Override
  public void start(final Stage primaryStage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader();

    Locale locale = new Locale("da", "DK");

    fxmlLoader.setResources(
      ResourceBundle.getBundle("dk.itu.bookie.Locale", locale)
    );

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
}
