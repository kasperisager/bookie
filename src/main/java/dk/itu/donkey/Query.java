/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.List;

// SQL utilities
import java.sql.SQLException;

/**
 * The Query class is used for executing Data Manipulation Language (DML)
 * statements against a databse and returns {@link Row}-representations of the
 * database response.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Data_manipulation_language">
 *      Wikipedia - Data manipulation language</a>
 *
 * @since 1.0.0 Initial release.
 */
public final class Query {
  /**
   * The database to perform the query against.
   */
  private Database db;

  /**
   * The SQL grammar to use for the query.
   */
  private Grammar grammar;

  /**
   * Build and perform a query against a database.
   *
   * @param db    The database to perform the query against.
   * @param table The database table to operate on.
   */
  public Query(final Database db, final String table) {
    this.db = db;
    this.grammar = db.grammar();
    this.grammar.addTable(table);
  }

  /**
   * Select a set of columns from a table.
   *
   * @param columns The columns to select from the table.
   * @return        The current {@link Query} object, for chaining.
   */
  public Query select(final String... columns) {
    for (String column: columns) {
      this.grammar.addColumn(column);
    }

    return this;
  }

  /**
   * Insert a row into a table.
   *
   * @param row The row to insert into the table.
   * @return    Generated keys, if any.
   *
   * @throws SQLException In case of a SQL error.
   */
  public List<Row> insert(final Row row) throws SQLException {
    for (String column: row.getColumns()) {
      this.grammar.addColumn(column);
    }

    for (Object value: row.getValues()) {
      this.grammar.addValue(value);
    }

    return this.db.execute(
      this.grammar.compileInsert(), this.grammar.compileInsertValues()
    );
  }

  /**
   * Update one or more rows in a table.
   *
   * <p>
   * This can be combined with a `where` statement to selectively update a set
   * of table rows.
   *
   * <p>
   * If no `where` statement is supplied, all rows of the table will be updated.
   *
   * @param row Row of columns to update in the table.
   * @return    Generated keys, if any.
   *
   * @throws SQLException In case of a SQL error.
   */
  public List<Row> update(final Row row) throws SQLException {
    for (String column: row.getColumns()) {
      this.grammar.addColumn(column);
    }

    for (Object value: row.getValues()) {
      this.grammar.addValue(value);
    }

    return this.db.execute(
      this.grammar.compileUpdate(), this.grammar.compileUpdateValues()
    );
  }

  /**
   * Delete rows from a table.
   *
   * <p>
   * This can be combine with a `where` statement to selectively delete a set
   * of table rows.
   *
   * <p>
   * If no `where` statement is supplied, all rows of the table will be deleted.
   *
   * @return Generated keys, if any.
   *
   * @throws SQLException In case of a SQL error.
   */
  public List<Row> delete() throws SQLException {
    return this.db.execute(
      this.grammar.compileDelete(), this.grammar.compileDeleteValues()
    );
  }

  /**
   * Add a `right join` clause to the query.
   *
   * @param foreignTable  The foreign table to join.
   * @param localColumn   The column in the local table.
   * @param operator      The logical operator to use in the join constraint.
   * @param foreignColumn The column in the foreign table.
   * @return              The current {@link Query} object, for chaining.
   */
  public Query join(
    final String foreignTable,
    final String localColumn,
    final String operator,
    final String foreignColumn
  ) {
    this.grammar.addJoin(
      "inner", foreignTable, localColumn, operator, foreignColumn
    );

    return this;
  }

  /**
   * Add a `join` clause to the query.
   *
   * @param foreignTable  The foreign table to join.
   * @param localColumn   The column in the local table.
   * @param foreignColumn The column in the foreign table.
   * @return              The current {@link Query} object, for chaining.
   */
  public Query join(
    final String foreignTable,
    final String localColumn,
    final String foreignColumn
  ) {
    this.grammar.addJoin(
      "inner", foreignTable, localColumn, "=", foreignColumn
    );

    return this;
  }

  /**
   * Add a `left join` clause to the query.
   *
   * @param foreignTable  The foreign table to join.
   * @param localColumn   The column in the local table.
   * @param operator      The logical operator to use in the join constraint.
   * @param foreignColumn The column in the foreign table.
   * @return              The current {@link Query} object, for chaining.
   */
  public Query leftJoin(
    final String foreignTable,
    final String localColumn,
    final String operator,
    final String foreignColumn
  ) {
    this.grammar.addJoin(
      "left outer", foreignTable, localColumn, operator, foreignColumn
    );

    return this;
  }

  /**
   * Add a `left join` clause to the query.
   *
   * @param foreignTable  The foreign table to join.
   * @param localColumn   The column in the local table.
   * @param foreignColumn The column in the foreign table.
   * @return              The current {@link Query} object, for chaining.
   */
  public Query leftJoin(
    final String foreignTable,
    final String localColumn,
    final String foreignColumn
  ) {
    this.grammar.addJoin(
      "left outer", foreignTable, localColumn, "=", foreignColumn
    );

    return this;
  }

  /**
   * Add a `where` statement to the query.
   *
   * @param column    The column to compare.
   * @param operator  The logical operator to use for the comparison.
   * @param value     The value to compare against.
   * @return          The current {@link Query} object, for chaining.
   */
  public Query where(
    final String column,
    final String operator,
    final Object value
  ) {
    this.grammar.addWhere(column, operator, value, "and");

    return this;
  }

  /**
   * Add a `where` statement to the query.
   *
   * <p>
   * This method is a shorthand for the equality operator.
   *
   * @param column  The column to compare.
   * @param value   The value to compare against.
   * @return        The current {@link Query} object, for chaining.
   */
  public Query where(final String column, final Object value) {
    return this.where(column, "=", value);
  }

  /**
   * Add a `where` statement to the query, with a logical operator o `or`.
   *
   * @param column    The column to compare.
   * @param operator  The logical operator to use for the comparison.
   * @param value     The value to compare against.
   * @return          The current {@link Query} object, for chaining.
   */
  public Query orWhere(
    final String column,
    final String operator,
    final Object value
  ) {
    this.grammar.addWhere(column, operator, value, "or");

    return this;
  }

  /**
   * Add a `where` statement to the query, with a logical operator of `or`.
   *
   * <p>
   * This method is a shorthand for the equality operator.
   *
   * @param column  The column to compare.
   * @param value   The value to compare against.
   * @return        The current {@link Query} object, for chaining.
   */
  public Query orWhere(final String column, final Object value) {
    return this.orWhere(column, "=", value);
  }

  /**
   * Add an `order by` statement to the query.
   *
   * @param column    The column to order by.
   * @param direction The ordering direction. Either "asc" or "desc".
   * @return          The current {@link Query} object, for chaining.
   */
  public Query orderBy(final String column, final String direction) {
    this.grammar.addOrder(column, direction);

    return this;
  }

  /**
   * Add an `order by` statement to the query.
   *
   * <p>
   * This is a shorthand for ascending ordering.
   *
   * @param column  The column to order by.
   * @return        The current {@link Query} object, for chaining.
   */
  public Query orderBy(final String column) {
    return this.orderBy(column, "asc");
  }

  /**
   * Add a `limit` statement to the query.
   *
   * @param limit The limit to add.
   * @return      The current {@link Query} object, for chaining.
   */
  public Query limit(final int limit) {
    this.grammar.addLimit(limit);

    return this;
  }

  /**
   * Add an `offset` statement to the query.
   *
   * @param offset  The offset to add.
   * @return        The current {@link Query} object, for chaining.
   */
  public Query offset(final int offset) {
    this.grammar.addOffset(offset);

    return this;
  }

  /**
   * Execute a select query against the database.
   *
   * @return A list of database rows.
   *
   * @throws SQLException In case of a SQL error.
   */
  public List<Row> get() throws SQLException {
    return this.db.execute(
      this.grammar.compileSelect(), this.grammar.compileSelectValues()
    );
  }

  /**
   * Execute a select query against the database and grab the first result.
   *
   * @return A single database row.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Row first() throws SQLException {
    List<Row> rows = this.limit(1).get();

    return (rows.size() > 0) ? rows.get(0) : null;
  }

  /**
   * Run a single aggregate function on a table column.
   *
   * @param function  The aggregate function to execute.
   * @param column    The column to run the aggregate function against.
   * @return          The direct result of the query.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Object aggregate(
    final String function,
    final String column
  ) throws SQLException {
    String aggregate = String.format("%s(%s)", function, column);

    Row row = this.select(aggregate).first();

    Object value = row.get(aggregate);

    if (value == null) {
      value = row.get(function);
    }

    return value;
  }

  /**
   * Run a single aggregate function on all table rows.
   *
   * @param function  The aggregate function to execute.
   * @return          The direct result of the aggregate function.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Object aggregate(final String function) throws SQLException {
    return this.aggregate(function, "*");
  }

  /**
   * Count all rows in a table.
   *
   * @return The number of rows in a table.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Number count() throws SQLException {
    return (Number) this.aggregate("count");
  }

  /**
   * Get the largest value of a table column.
   *
   * @param column  The column to run the aggregrate function against.
   * @return        The largest value in the specified column.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Object max(final String column) throws SQLException {
    return this.aggregate("max", column);
  }

  /**
   * Get the smallest value of a table column.
   *
   * @param column  The column to run the aggregrate function against.
   * @return        The smallest value in the specified table column.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Object min(final String column) throws SQLException {
    return this.aggregate("min", column);
  }

  /**
   * Get the average value of a table column.
   *
   * @param column  The column to run the aggregate function against.
   * @return        The average value of the specified table column.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Number avg(final String column) throws SQLException {
    return (Number) this.aggregate("avg", column);
  }

  /**
   * Get the sum of a table column.
   *
   * @param column  The column to run the aggregate function against.
   * @return        The sum of the specified table column.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Number sum(final String column) throws SQLException {
    return (Number) this.aggregate("sum", column);
  }
}
