/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey.database;

// General utilities
import java.util.ArrayList;
import java.util.List;

/**
 * Grammar class.
 *
 * @version 1.0.0
 */
public final class Grammar {
  /**
   * Database table.
   */
  private String table;

  /**
   * List of formatted columns.
   */
  private List<String> columns = new ArrayList<>();

  /**
   * List of formatted values.
   */
  private List<String> values = new ArrayList<>();

  /**
   * List of raw values.
   */
  private List<Object> rawValues = new ArrayList<>();

  /**
   * List of formatted wheres.
   */
  private List<String> wheres = new ArrayList<>();

  /**
   * List of where values.
   */
  private List<Object> whereValues = new ArrayList<>();

  /**
   * List of formatted orders.
   */
  private List<String> orders = new ArrayList<>();

  /**
   * Formatted result limit.
   */
  private String limit = "";

  /**
   * Formatted result offset.
   */
  private String offset = "";

  /**
   * Build a formatted table clause.
   *
   * @param table The table to format.
   * @return      The formatted table.
   */
  public String buildTable(final String table) {
    return table.trim();
  }

  /**
   * Add a table to the grammar.
   *
   * @param table The table to add.
   */
  public void addTable(final String table) {
    this.table = this.buildTable(table);
  }

  /**
   * Build a formatted column clause.
   *
   * @param column  The column to format.
   * @return        The formatted column.
   */
  public String buildColumn(final String column) {
    return column.trim();
  }

  /**
   * Build a list of formatted columns.
   *
   * @param columns The formatted columns.
   * @return        A comma-seprated list of columns.
   */
  public String buildColumns(final List<String> columns) {
    return String.join(", ", columns);
  }

  /**
   * Add a column to the grammar.
   *
   * @param column The column to add.
   */
  public void addColumn(final String column) {
    this.columns.add(this.buildColumn(column));
  }

  /**
   * Build a formatted value clause.
   *
   * This method simply returns a "?"-character as all values are removed from
   * statements prior to pre-compilation.
   *
   * @param value The value to format.
   * @return      The formatted value.
   */
  public String buildValue(final Object value) {
    return "?";
  }

  /**
   * Build a list of formatted values.
   *
   * @param values  The formatted values.
   * @return        A comma-separated list of values.
   */
  public String buildValues(final List<String> values) {
    return String.join(", ", values);
  }

  /**
   * Add a value to the grammar.
   *
   * @param value The value to add.
   */
  public void addValue(final Object value) {
    // Store the original value for later access.
    this.rawValues.add(value);

    this.values.add(this.buildValue(value));
  }

  /**
   * Build a formatted set clause.
   *
   * @param column  The formatted column of the set clause.
   * @param value   The formatted value of the set clause.
   * @return        The formatted set clause.
   */
  public String buildSet(final String column, final String value) {
    return String.format("%s = %s", column, value);
  }

  /**
   * Build a list of formatted set clauses.
   *
   * @param columns The columns of the set clause.
   * @param values  The values of the set clause.
   * @return        A list of comma-separated set clauses.
   */
  public String buildSets(
    final List<String> columns,
    final List<String> values
  ) {
    List<String> sets = new ArrayList<>();
    int length = columns.size();

    for (int i = 0; i < length; i++) {
      sets.add(this.buildSet(columns.get(i), values.get(i)));
    }

    return (!sets.isEmpty()) ? "set " + String.join(", ", sets) : "";
  }

  /**
   * Build a formatted where clause.
   *
   * @param column      The column of the where clause.
   * @param operator    The operator of the where clause.
   * @param value       The value of the where clause.
   * @param comparator  The comparator to use.
   * @return            The formatted where clause.
   */
  public String buildWhere(
    final String column,
    final String operator,
    final Object value,
    final String comparator
  ) {
    // Store the original value for later access.
    this.whereValues.add(value);

    return String.format(
      "%s %s %s %s",
      comparator.trim(),
      this.buildColumn(column),
      operator.trim(),
      this.buildValue(value)
    );
  }

  /**
   * Build a list of formatted where clauses.
   *
   * @param wheres  The formatted where clauses.
   * @return        A comma-separated list of where clauses.
   */
  public String buildWheres(final List<String> wheres) {
    if (!wheres.isEmpty()) {
      return "where " + String.join(" ", wheres).replaceAll("^and |^or ", "");
    }
    else {
      return "";
    }
  }

  /**
   * Add a where clause to the grammar.
   *
   * @param column      The column of the where clause.
   * @param operator    The operator of the where clause.
   * @param value       The value of the where clause.
   * @param comparator  The comparator to use.
   */
  public void addWhere(
    final String column,
    final String operator,
    final Object value,
    final String comparator
  ) {
    this.wheres.add(this.buildWhere(column, operator, value, comparator));
  }

  /**
   * Build a formatted order by clause.
   *
   * @param column    The column of the order by clause.
   * @param direction The direction of the ordering.
   * @return          The formatted order by clause.
   */
  public String buildOrder(final String column, final String direction) {
    return String.format(
      "%s %s",
      this.buildColumn(column),
      direction.trim()
    );
  }

  /**
   * Build a list of formatetd order by clauses.
   *
   * @param orders  The formatted order by clauses.
   * @return        A comma-separated list of order by clauses.
   */
  public String buildOrders(final List<String> orders) {
    if (!orders.isEmpty()) {
      return "order by " + String.join(", ", orders);
    }
    else {
      return "";
    }
  }

  /**
   * Add an order by clause to the grammar.
   *
   * @param column    The column of the order by clause.
   * @param direction The direction fo the ordering.
   */
  public void addOrder(final String column, final String direction) {
    this.orders.add(this.buildOrder(column, direction));
  }

  /**
   * Build a formatted limit clause.
   *
   * @param limit The limit.
   * @return      The formatted limit clause.
   */
  public String buildLimit(final int limit) {
    return (limit > 0) ? "limit " + limit : "";
  }

  /**
   * Add a limit to the grammar.
   *
   * @param limit The limit.
   */
  public void addLimit(final int limit) {
    this.limit = this.buildLimit(limit);
  }

  /**
   * Build a formatted offset clause.
   *
   * @param offset  The offset.
   * @return        The formatted offset.
   */
  public String buildOffset(final int offset) {
    return (offset > 0) ? "offset " + offset : "";
  }

  /**
   * Add an offset to the grammar.
   *
   * @param offset The offset.
   */
  public void addOffset(final int offset) {
    this.offset = this.buildOffset(offset);
  }

  /**
   * Build a formatted column clause with a data type.
   *
   * @param column    The column.
   * @param type      The type of the column.
   * @param length    The length of the column.
   * @param required  Whether or not this column is required.
   * @return          The formatted column clause.
   */
  public String buildDataType(
    final String column,
    final String type,
    final int length,
    final boolean required
  ) {
    String nullable = (required) ? "not null" : "null";

    return String.format("%s %s(%s) %s", column, type, length, nullable);
  }

  /**
   * Build a formatted column clause with a data type.
   *
   * @param column    The column.
   * @param type      The type of the column.
   * @param required  Whether or not this column is required.
   * @return          The formatted column clause.
   */
  public String buildDataType(
    final String column,
    final String type,
    final boolean required
  ) {
    String nullable = (required) ? "not null" : "null";

    return String.format("%s %s %s", column, type, nullable);
  }

  /**
   * Add a column with a data type to the grammar.
   *
   * @param column    The column.
   * @param type      The type of the column.
   * @param length    The length of the column.
   * @param required  Whether or not this column is required.
   */
  public void addDataType(
    final String column,
    final String type,
    final int length,
    final boolean required
  ) {
    this.addColumn(this.buildDataType(column, type, length, required));
  }

  /**
   * Add a column with a data type to the grammar.
   *
   * @param column    The column.
   * @param type      The type of the column.
   * @param required  Whether or not this column is required.
   */
  public void addDataType(
    final String column,
    final String type,
    final boolean required
  ) {
    this.addColumn(this.buildDataType(column, type, required));
  }

  /**
   * Build a formatted foreign key clause.
   *
   * @param column        The column.
   * @param foreignTable  The foreign table.
   * @param foreignColumn The foreign column.
   * @return              The formatted foreign key clause.
   */
  public String buildForeignKey(
    final String column,
    final String foreignTable,
    final String foreignColumn
  ) {
    return String.format(
      "foreign key(%s) references %s(%s)",
      column,
      foreignTable,
      foreignColumn
    );
  }

  /**
   * Add a foreign key to the grammar.
   *
   * @param column        The column.
   * @param foreignTable  The foreign table.
   * @param foreignColumn The foreign column.
   */
  public void addForeignKey(
    final String column,
    final String foreignTable,
    final String foreignColumn
  ) {
    this.columns.add(this.buildForeignKey(
      column, foreignTable, foreignColumn
    ));
  }

  /**
   * Compile a select statement based on the current state of the grammar.
   *
   * @return The full select statement.
   */
  public String compileSelect() {
    if (this.columns.isEmpty()) {
      this.addColumn("*");
    }

    // Since `offset` requires `limit` to also be specified, add the largest
    // possible integer as the limit if it hasn't been set. This is strangely
    // enough the official advice:
    // http://dev.mysql.com/doc/refman/5.0/en/select.html
    if (this.limit.isEmpty() && !this.offset.isEmpty()) {
      this.addLimit(Integer.MAX_VALUE);
    }

    return String.format(
      "select %s from %s %s %s %s %s",
      this.buildColumns(this.columns),
      this.table,
      this.buildWheres(this.wheres),
      this.buildOrders(this.orders),
      this.limit,
      this.offset
    ).trim().replaceAll(" {2,}", " ");
  }

  /**
   * Compile raw values for a select statement.
   *
   * @return List of values for the select statement.
   */
  public List<Object> compileSelectValues() {
    return this.whereValues;
  }

  /**
   * Compile an insert statement based on the current state of the grammar.
   *
   * @return The full insert statement.
   */
  public String compileInsert() {
    return String.format(
      "insert into %s (%s) values (%s)",
      this.table,
      this.buildColumns(this.columns),
      this.buildValues(this.values)
    ).trim().replaceAll(" {2,}", " ");
  }

  /**
   * Compile raw values for an insert statement.
   *
   * @return List of values for the insert statement.
   */
  public List<Object> compileInsertValues() {
    return this.rawValues;
  }

  /**
   * Compile an update statement based on the current state of the grammar.
   *
   * @return The full update statement.
   */
  public String compileUpdate() {
    return String.format(
      "update %s %s %s",
      this.table,
      this.buildSets(this.columns, this.values),
      this.buildWheres(this.wheres)
    ).trim().replaceAll(" {2,}", " ");
  }

  /**
   * Compile raw values for an update statement.
   *
   * @return List of values for the update statement.
   */
  public List<Object> compileUpdateValues() {
    List<Object> updateValues = new ArrayList<>();

    updateValues.addAll(this.rawValues);
    updateValues.addAll(this.whereValues);

    return updateValues;
  }

  /**
   * Compile a delete statement based on the current state of the grammar.
   *
   * @return The full delete statement.
   */
  public String compileDelete() {
    return String.format(
      "delete from %s %s",
      this.table,
      this.buildWheres(this.wheres)
    ).trim().replaceAll(" {2,}", " ");
  }

  /**
   * Compile raw values for a delete statement.
   *
   * @return List of values for the delete statement.
   */
  public List<Object> compileDeleteValues() {
    return this.whereValues;
  }

  /**
   * Compile a create statement based on the current state of the grammar.
   *
   * @return The full create statement.
   */
  public String compileCreate() {
    return String.format(
      "create table if not exists %s (%s)",
      this.table,
      this.buildColumns(this.columns)
    ).trim().replaceAll(" {2,}", " ");
  }

  /**
   * Compile a drop statement based on the current state of the grammar.
   *
   * @return The full drop statement.
   */
  public String compileDrop() {
    return String.format(
      "drop table if exists %s",
      this.table
    ).trim().replaceAll(" {2,}", " ");
  }
}
