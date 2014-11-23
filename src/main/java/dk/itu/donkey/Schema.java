/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

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
    this.db.execute(this.grammar.compileCreate());
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
    this.db.execute(this.grammar.compileDrop());
  }

  /**
   * Add a text column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema text(final String column) {
    this.grammar.addDataType(column, "text", true);

    return this;
  }

  /**
   * Add an integer column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema integer(final String column) {
    this.grammar.addDataType(column, "integer", true);

    return this;
  }

  /**
   * Add a double column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema doublePrecision(final String column) {
    this.grammar.addDataType(column, "double", true);

    return this;
  }

  /**
   * Add a float column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema floatingPoint(final String column) {
    this.grammar.addDataType(column, "float", true);

    return this;
  }

  /**
   * Add a real column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema real(final String column) {
    this.grammar.addDataType(column, "real", true);

    return this;
  }

  /**
   * Add a numeric column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema numeric(final String column) {
    this.grammar.addDataType(column, "numeric", true);

    return this;
  }

  /**
   * Add a boolean column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema bool(final String column) {
    this.grammar.addDataType(column, "boolean", true);

    return this;
  }

  /**
   * Add a date column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema date(final String column) {
    this.grammar.addDataType(column, "date", true);

    return this;
  }

  /**
   * Add a time column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema time(final String column) {
    this.grammar.addDataType(column, "time", true);

    return this;
  }

  /**
   * Add a date/time column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema datetime(final String column) {
    this.grammar.addDataType(column, "datetime", true);

    return this;
  }

  /**
   * Add a blob column to the schema.
   *
   * @param column  The name of the column.
   * @return        The current {@link Schema} object, for chaining.
   */
  public Schema blob(final String column) {
    this.grammar.addDataType(column, "blob", true);

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
