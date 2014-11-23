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
  MYSQL {
    /**
     * Initialize a connection to a MySQL database.
     *
     * Available connection properties:
     *
     * database - The name of the database to connect to. Required.
     * user     - The user to connect as.
     * password - The password of the user.
     * url      - The URL to connect to. Default: localhost:3306
     *
     * @param p Database connection properties.
     * @return  A connection to the database.
     *
     * @throws SQLException In case of a connection error.
     */
    @Override
    public Connection connect(final Properties p) throws SQLException {
      String connectionUrl;
      String database = p.getProperty("database");

      if (database == null) {
        throw new IllegalArgumentException("A database is required");
      }

      String user = p.getProperty("user", "");
      String password = p.getProperty("password", "");
      String url = p.getProperty("url", "localhost:3306");

      connectionUrl = String.format(
        "mysql://%s/%s?user=%s&password=%s",
        url, database, user, password
      );

      return DriverManager.getConnection("jdbc:" + connectionUrl);
    }
  },

  /**
   * SQLite driver.
   *
   * http://mvnrepository.com/artifact/org.xerial/sqlite-jdbc/3.8.7
   */
  SQLITE {
    /**
     * Initialize a connection to a SQLite database.
     *
     * Available connection properties:
     *
     * database - The name of the database to connect to. Required.
     *
     * @param p Database connection properties.
     * @return  A connection to the database.
     *
     * @throws SQLException In case of a connection error.
     */
    @Override
    public Connection connect(final Properties p) throws SQLException {
      String connectionUrl;
      String database = p.getProperty("database");

      if (database == null) {
        throw new IllegalArgumentException("A database is required");
      }

      connectionUrl = String.format("sqlite:%s.db", database);

      return DriverManager.getConnection("jdbc:" + connectionUrl);
    }
  };

  /**
   * Initialize a connection to a database via a JDBC-compatible driver.
   *
   * @param p Database connection properties.
   * @return  A connection to the database.
   *
   * @throws SQLException In case of a connection error.
   */
  public abstract Connection connect(final Properties p) throws SQLException;
}
