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
   * List of columns.
   */
  private List<String> columns = new ArrayList<>();

  /**
   * List of values.
   */
  private List<Object> values = new ArrayList<>();

  /**
   * List of wheres.
   */
  private List<String> wheres = new ArrayList<>();

  /**
   * List of orders.
   */
  private List<String> orders = new ArrayList<>();

  /**
   * List of foreign keys.
   */
  private List<String> foreignKeys = new ArrayList<>();

  /**
   * Result limit.
   */
  private String limit = "";

  /**
   * Result offset.
   */
  private String offset = "";

  /**
   * Build a formatted table clause.
   *
   * @param table The table to format.
   * @return      The formatted table.
   */
  public String buildTable(final String table) {
    return String.format("%s", table);
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
   * Build a list of columns currently added to the grammar.
   *
   * @return A comma-seprated list of columns.
   */
  public String buildColumns() {
    return String.join(", ", this.columns);
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
   * Build a list of values currently added to the grammar.
   *
   * This method simply builds a list of comman-separated "?"-characters as all
   * values are removed from statements prior to pre-compilation.
   *
   * @return A comma-separated list of values.
   */
  public String buildValues() {
    List<String> values = new ArrayList<>();

    for (Object value: this.values) {
      values.add("?");
    }

    return String.join(", ", values);
  }

  /**
   * Add a value to the grammar.
   *
   * @param value The value to add.
   */
  public void addValue(final Object value) {
    this.values.add(value);
  }

  /**
   * Get all values currently added to the grammar.
   *
   * @return A list of all values.
   */
  public List<Object> getValues() {
    return this.values;
  }

  /**
   * Build a formatted set clause.
   *
   * @param column  The column of set clause.
   * @param value   The value of the set clause.
   * @return        The formatted set clause.
   */
  public String buildSet(final String column, final Object value) {
    return String.format(
      "%s = %s",
      column.trim(),
      this.buildValue(value)
    );
  }

  /**
   * Build a list of set clauses currently added to the grammar.
   *
   * @return A list of comma-separated set clauses.
   */
  public String buildSets() {
    List<String> sets = new ArrayList<>();
    int length = this.columns.size();

    for (int i = 0; i < length; i++) {
      sets.add(this.buildSet(this.columns.get(i), this.values.get(i)));
    }

    return String.join(", ", sets);
  }

  /**
   * Add a set clause to the grammar.
   *
   * @param column  The column of the set clause.
   * @param value   The value of the set clause.
   */
  public void addSet(final String column, final Object value) {
    this.addColumn(column);
    this.addValue(value);
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
    return String.format(
      "%s %s %s %s",
      comparator.trim(),
      column.trim(),
      operator.trim(),
      this.buildValue(value)
    );
  }

  /**
   * Build a list of where clauses currently added to the grammar.
   *
   * @return A comma-separated list of where clauses.
   */
  public String buildWheres() {
    if (!this.wheres.isEmpty()) {
      return "where " + String.join(" ", this.wheres)
                              .replaceAll("^and |^or ", "");
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
      column.trim(),
      direction.trim()
    );
  }

  /**
   * Build a list of order by clauses currently added to the grammer.
   *
   * @return A comma-separated list of order by clauses.
   */
  public String buildOrders() {
    if (!this.orders.isEmpty()) {
      return "order by " + String.join(", ", this.orders);
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
   * Add a column with a data type to the grammar.
   *
   * @param column    The column.
   * @param type      The type of the column.
   * @param length    The length of the column.
   */
  public void addDataType(
    final String column,
    final String type,
    final int length
  ) {
    this.addColumn(this.buildDataType(column, type, length, false));
  }

  /**
   * Add a column with a data type to the grammar.
   *
   * @param column    The column.
   * @param type      The type of the column.
   */
  public void addDataType(final String column, final String type) {
    this.addColumn(this.buildDataType(column, type, false));
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
    this.foreignKeys.add(this.buildForeignKey(
      column, foreignTable, foreignColumn
    ));
  }

  /**
   * Build a select statement based on the current state of the grammar.
   *
   * @return The full select statement.
   */
  public String buildSelect() {
    if (this.columns.isEmpty()) {
      this.addColumn("*");
    }

    // Since `offset` requires `limit` to also be specified, add the largest
    // possible integer as the limit if it hasn't been set. This is strangely
    // enough the official advice:
    // http://dev.mysql.com/doc/refman/5.0/en/select.html
    if (this.limit == null && this.offset != null) {
      this.addLimit((int) Math.pow(2, 31) - 1);
    }

    return String.format(
      "select %s from %s %s %s %s %s",
      this.buildColumns(),
      this.table,
      this.buildWheres(),
      this.buildOrders(),
      this.limit,
      this.offset
    ).trim();
  }

  /**
   * Build an insert statement based on the current state of the grammar.
   *
   * @return The full insert statement.
   */
  public String buildInsert() {
    return String.format(
      "insert into %s (%s) values (%s)",
      this.table,
      this.buildColumns(),
      this.buildValues()
    ).trim();
  }

  /**
   * Build an update statement based on the current state of the grammar.
   *
   * @return The full update statement.
   */
  public String buildUpdate() {
    return String.format(
      "update %s set %s %s",
      this.table,
      this.buildSets(),
      this.buildWheres()
    ).trim();
  }

  /**
   * Build a delete statement based on the current state of the grammar.
   *
   * @return The full delete statement.
   */
  public String buildDelete() {
    return String.format(
      "delete from %s %s",
      this.table,
      this.buildWheres()
    ).trim();
  }

  /**
   * Build a create statement based on the current state of the grammar.
   *
   * @return The full create statement.
   */
  public String buildCreate() {
    return String.format(
      "create table if not exists %s (%s)",
      this.table,
      this.buildColumns()
    ).trim();
  }

  /**
   * Build a drop statement based on the current state of the grammar.
   *
   * @return The full drop statement.
   */
  public String buildDrop() {
    return String.format("drop table if exists %s", this.table).trim();
  }
}
