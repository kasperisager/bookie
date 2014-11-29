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

// SQL grammars
import dk.itu.donkey.grammar.MySqlGrammar;
import dk.itu.donkey.grammar.PostgreSqlGrammar;
import dk.itu.donkey.grammar.SqliteGrammar;

/**
 * The Driver enumerator describes the different JDBC drivers supported by
 * Donkey.
 *
 * @see <a href="https://en.wikipedia.org/wiki/JDBC_driver">Wikipedia - JDBC
 *      driver</a>
 *
 * @since 1.0.0 Initial release.
 */
public enum Driver {
  /**
   * MySQL driver.
   *
   * <p>
   * Available connection properties:
   *
   * <pre>
   * database - The name of the database to connect to. Required.
   * user     - The user to connect as.
   * password - The password of the user.
   * url      - The URL to connect to. Default: localhost:3306
   * </pre>
   *
   * @see dk.itu.donkey.grammar.MySqlGrammar
   * @see <a href="http://bit.ly/11StjtX">Maven Repository - MySQL</a>
   */
  MYSQL {
    /**
     * Initialize a SQL grammar for a MySQL database.
     *
     * @return A grammar object for the database.
     */
    @Override
    public Grammar grammar() {
      return new MySqlGrammar();
    }

    /**
     * Initialize a connection to a MySQL database.
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

      String user = p.getProperty("user");
      String password = p.getProperty("password");
      String url = p.getProperty("url", "localhost:3306");

      connectionUrl = String.format("mysql://%s/%s", url, database);

      return DriverManager.getConnection(
        "jdbc:" + connectionUrl, user, password
      );
    }
  },

  /**
   * PostgreSQL driver.
   *
   * <p>
   * Available connection properties:
   *
   * <pre>
   * database - The name of the database to connect to. Required.
   * user     - The user to connect as.
   * password - The password of the user.
   * url      - The URL to connect to. Default: localhost:5432
   * </pre>
   *
   * @see dk.itu.donkey.grammar.PostgreSqlGrammar
   * @see <a href="http://bit.ly/11Un3Ro">Maven Repository - PostgreSQL</a>
   */
  POSTGRESQL {
    /**
     * Initialize a SQL grammar for a PostgreSQL database.
     *
     * @return A grammar object for the database.
     */
    @Override
    public Grammar grammar() {
      return new PostgreSqlGrammar();
    }

    /**
     * Initialize a connection to a PostgreSQL database.
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

      String user = p.getProperty("user");
      String password = p.getProperty("password");
      String url = p.getProperty("url", "localhost:5432");

      connectionUrl = String.format("postgresql://%s/%s", url, database);

      return DriverManager.getConnection(
        "jdbc:" + connectionUrl, user, password
      );
    }
  },

  /**
   * SQLite driver.
   *
   * <p>
   * Available connection properties:
   *
   * <pre>
   * database - The name of the database to connect to. Required.
   * </pre>
   *
   * @see dk.itu.donkey.grammar.SqliteGrammar
   * @see <a href="http://bit.ly/1xzBsvg">Maven Repository - SQLite</a>
   */
  SQLITE {
    /**
     * Initialize a SQL grammar for a SQLite database.
     *
     * @return A grammar object for the database.
     */
    @Override
    public Grammar grammar() {
      return new SqliteGrammar();
    }

    /**
     * Initialize a connection to a SQLite database.
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
   * Initialize the SQL grammar for a database driver.
   *
   * @return A grammar object for the database.
   */
  public abstract Grammar grammar();

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
