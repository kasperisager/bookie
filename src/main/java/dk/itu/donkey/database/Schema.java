/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey.database;

// SQL utilities
import java.sql.SQLException;

/**
 * Schema class.
 *
 * @version 1.0.0
 */
public final class Schema {
  /**
   * The database to run the schema against.
   */
  private Database db;

  /**
   * The SQL grammar to use for the schema.
   */
  private Grammar grammar;

  /**
   * Initialize a new schema.
   *
   * @param db      The database to run the schema against.
   * @param grammar The SQL grammar to use for the schema.
   */
  public Schema(final Database db, final Grammar grammar) {
    this.db = db;
    this.grammar = grammar;
  }

  /**
   * Begin a table creation statement.
   *
   * @param table The name of the table to create.
   * @return      The current {@link Schema} object, for chaining.
   */
  public Schema create(final String table) {
    this.grammar.addTable(table);

    return this;
  }

  /**
   * Run the create statement.
   *
   * @throws SQLException In case of a SQL error.
   */
  public void run() throws SQLException {
    this.db.execute(this.grammar.buildCreate());
  }

  /**
   * Drop a database table.
   *
   * @param table The table to drop from the database.
   *
   * @throws SQLException In case of a SQL error.
   */
  public void drop(final String table) throws SQLException {
    this.grammar.addTable(table);
    this.db.execute(this.grammar.buildDrop());
  }

  /**
   * Add a string column to the schema.
   *
   * @param column  The name of the column.
   * @param length  The length of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema string(final String column, final int length) {
    this.grammar.addDataType(column, "varchar", length);

    return this;
  }

  /**
   * Add a string column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema string(final String column) {
    return this.string(column, 255);
  }

  /**
   * Add a text column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema text(final String column) {
    this.grammar.addDataType(column, "text");

    return this;
  }

  /**
   * Add an integer column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema integer(final String column) {
    this.grammar.addDataType(column, "integer");

    return this;
  }

  /**
   * Add an auto incrementing integer column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema increments(final String column) {
    this.grammar.addDataType(
      column, "integer primary key auto_increment", true
    );

    return this;
  }

  /**
   * Add a boolean column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema bool(final String column) {
    this.grammar.addDataType(column, "bool");

    return this;
  }

  /**
   * Add a date column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema date(final String column) {
    this.grammar.addDataType(column, "date");

    return this;
  }

  /**
   * Add a date-time column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema dateTime(final String column) {
    this.grammar.addDataType(column, "datetime");

    return this;
  }

  /**
   * Add a time column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema time(final String column) {
    this.grammar.addDataType(column, "time");

    return this;
  }

  /**
   * Add a foreign key to the schema.
   *
   * @param column        The name of the column.
   * @param foreignTable  The name of the foreign table.
   * @param foreignColumn The name of the foreign column.
   * @return              The current {@link Schema} object, for chaining.
   */
  public Schema foreignKey(
    final String column,
    final String foreignTable,
    final String foreignColumn
  ) {
    this.grammar.addForeignKey(column, foreignTable, foreignColumn);

    return this;
  }
}
