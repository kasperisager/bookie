/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// SQL utilities
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Database class.
 *
 * @version 1.0.0
 */
public final class Database {
  /**
   * The database driver to use.
   */
  private final Driver driver;

  /**
   * Database connection properties.
   */
  private final Properties properties;

  /**
   * Re-/initialize a database.
   *
   * @param driver      The database driver to use.
   * @param properties  Database connection properties.
   */
  public Database(final Driver driver, final Properties properties) {
    this.driver = driver;
    this.properties = properties;
  }

  /**
   * Initialize a connection to a database via a JDBC-compatible driver.
   *
   * @throws  SQLException  In case of a connection error.
   * @return                A connection to the database.
   */
  public Connection getConnection() throws SQLException {
    return this.driver.getConnection(this.properties);
  }

  /**
   * Execute some SQL against the database.
   *
   * This method uses the try-with-resource statement which ensures that all
   * closeable connections are automatically terminated after the code has run.
   *
   * @param   sql           The SQL to execute, without any values.
   * @param   values        Any values to add to the precompiled SQL statement.
   * @throws  SQLException  In case of a SQL error.
   * @return                The query result as a list of rows, or null if the
   *                        query was not a DQL.
   */
  public List<Row> execute(
    final String sql,
    final List<Object> values
  ) throws SQLException {
    try (
      Connection connection = this.getConnection();

      // Precompile the SQL statement without any values. This effectively
      // negates SQL injection as any input values will be added later on and
      // properly escaped by Java.
      PreparedStatement statement = connection.prepareStatement(
        sql, Statement.RETURN_GENERATED_KEYS
      );
    ) {
      // If any values need to be added to the query, go through each of the
      // values and set them on the precompiled statement.
      if (!values.isEmpty()) {
        for (int i = 0; i < values.size(); i++) {
          statement.setObject(i + 1, values.get(i));
        }
      }

      // Execute the SQL query. If the query fails, an exception will be
      // thrown. If the query succeeds, the return value will be a boolean
      // indicating whether or not the query generated a response.
      boolean response = statement.execute();

      // If the query generated a response, this means that a DQL (Data Query
      // Language) query was performed. Read the resulting result set.
      if (response) {
        try (ResultSet rs = statement.getResultSet()) {
          return this.parseResultSet(rs);
        }
      }
      // If the query didn't generate a response, it means that a DML (Data
      // Manipulation Language) or DCL (Data Control Language) query was
      // performed. Check for automatically generated keys.
      else {
        try (ResultSet rs = statement.getGeneratedKeys()) {
          return this.parseResultSet(rs);
        }
      }
    }
  }

  /**
   * Parse a result set from a database query.
   *
   * @param rs  The resutl set to parse.
   * @return    The parsed result as a list of rows.
   *
   * @throws SQLException In case of a SQL error.
   */
  private List<Row> parseResultSet(final ResultSet rs) throws SQLException {
    // Grab the result set meta data. This contains a lot of interesting
    // information such as the number of rows in the set, the data types
    // of each of the columns, and the total number of columns (which we
    // will later need).
    ResultSetMetaData rm = rs.getMetaData();

    // Get the total number of columns in each row of the result set.
    int columnCount = rm.getColumnCount();

    List<Row> rows = new ArrayList<>();

    // Run through each of the rows in the result set and add every column
    // to a row object. This gives us a data structure that is a lot
    // easier to work with than the result set itself.
    while (rs.next()) {
      Row row = new Row();

      // Run through each of the columns in the row and add them as
      // entries to the row object.
      for (int i = 1; i <= columnCount; i++) {
        row.put(rm.getColumnLabel(i), rs.getObject(i));
      }

      rows.add(row);
    }

    return rows;
  }

  /**
   * Execute a SQL query without any values.
   *
   * @param sql The SQL to execute, without any values.
   * @return    The query result as a list of rows, or null if the query was
   *            not a DQL.
   *
   * @throws SQLException In case of a SQL error.
   */
  public List<Row> execute(final String sql) throws SQLException {
    return this.execute(sql, new ArrayList<Object>());
  }

  /**
   * Initialize a SQL grammar for the database.
   *
   * @return A new SQL grammar object.
   */
  public Grammar grammar() {
    return new Grammar();
  }

  /**
   * Perform a query against a database table.
   *
   * @param name  Name of the table to perform the query against.
   * @return      A {@link Query} object initialized to the current db.
   */
  public Query table(final String name) {
    return new Query(this, this.grammar(), name);
  }

  /**
   * Run a schema against the database.
   *
   * @return A {@link Schema} oobject initialized to the current db.
   */
  public Schema schema() {
    return new Schema(this, this.grammar());
  }
}
