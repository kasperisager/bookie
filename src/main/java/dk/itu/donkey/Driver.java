/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

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
  MYSQL,

  /**
   * SQLite driver.
   *
   * http://mvnrepository.com/artifact/org.xerial/sqlite-jdbc/3.8.7
   */
  SQLITE;

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
        connectionUrl = String.format("sqlite:%s.db", database);
    }

    return DriverManager.getConnection("jdbc:" + connectionUrl);
  }
}
