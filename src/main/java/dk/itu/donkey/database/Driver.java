/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey.database;

// General utilities
import java.util.Properties;

// SQL utilities
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Driver enum.
 *
 * @version 1.0.0
 */
public enum Driver {
  /**
   * MySQL driver.
   *
   * http://mvnrepository.com/artifact/mysql/mysql-connector-java/5.1.6
   */
  MYSQL("com.mysql.jdbc.Driver"),

  /**
   * SQLite driver.
   *
   * http://mvnrepository.com/artifact/org.xerial/sqlite-jdbc/3.8.7
   */
  SQLITE("org.sqlite.JDBC");

  /**
   * The JDBC driver class.
   */
  private String driver;

  /**
   * Initialize a database driver.
   *
   * @param driver The JDBC driver class.
   */
  private Driver(final String driver) {
    this.driver = driver;
  }

  /**
   * Initialize a connection to a database via a JDBC-compatible driver.
   *
   * @param props Database connection properties.
   * @return      A connection to the database.
   *
   * @throws SQLException In case of a connection error.
   */
  public Connection getConnection(final Properties props) throws SQLException {
    String connectionUrl;
    String database = props.getProperty("database");

    if (database == null) {
      throw new IllegalArgumentException("A database is required");
    }

    switch (this) {
      case MYSQL:
        String user = props.getProperty("user", "");
        String password = props.getProperty("password", "");
        String url = props.getProperty("url", "localhost:3306");

        connectionUrl = String.format(
          "mysql://%s/%s?user=%s&password=%s",
          url, database, user, password
        );
        break;
      case SQLITE:
      default:
        if (!database.equals(":memory:")) {
          database += ".db";
        }

        connectionUrl = String.format("sqlite:%s", database);
    }

    try {
      Class.forName(this.driver);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Driver was not found");
    }

    return DriverManager.getConnection("jdbc:" + connectionUrl);
  }
}
