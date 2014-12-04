/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.ArrayList;
import java.util.List;

/**
 * The Grammar class defines methods for building the individual clauses of and
 * compiling complete standard SQL statements.
 *
 * <p>
 * All methods implemented in this abstract class should be valid SQL in each of
 * the available {@link Driver}-implementations. Deviations from ANSI (American
 * National Standard Institute) SQL are allowed as long as these deviations are
 * consistent across drivers.
 *
 * @see <a href="https://en.wikipedia.org/wiki/SQL">Wikipedia - Structured
 *      Query Language</a>
 * @see <a href="https://en.wikipedia.org/wiki/ANSI">Wikipedia - American
 *      National Standards Institute</a>
 * @see <a href="http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt">
 *      Information Technology - Database Language SQL</a>
 *
 * @since 1.0.0 Initial release.
 */
public abstract class Grammar {
  /**
   * Database table.
   *
   * <p>
   * This is used in the following clauses:
   *
   * <ul>
   * <li>select [...] from [table] [...]</li>
   * <li>insert into [table] [...]</li>
   * <li>update [table]</li>
   * <li>delete from [table] [...]</li>
   * <li>create table [table] (</li>
   * <li>drop [table]</li>
   * </ul>
   */
  private String table;

  /**
   * List of formatted columns.
   *
   * <p>
   * This is used in the following clauses:
   *
   * <ul>
   * <li>select [column1, column2, ...] [...]</li>
   * <li>insert into test ([column1, column2, ...]) [...]</li>
   * <li>update test set [column1] = x, [column2] = y, [...]</li>
   * <li>create table test ([column 1] integer, [column2] boolean, [...])</li>
   * </ul>
   */
  private List<String> columns = new ArrayList<>();

  /**
   * List of formatted values.
   *
   * <p>
   * This is used in the following clauses:
   *
   * <ul>
   * <li>insert into test ([...]) values ([value1], [value2], [...])</li>
   * <li>update test set x = [value1], y = [value2]</li>
   * </ul>
   */
  private List<String> values = new ArrayList<>();

  /**
   * List of raw values.
   */
  private List<Object> rawValues = new ArrayList<>();

  /**
   * List of formatted joins.
   *
   * <p>
   * This is used in the following clauses:
   *
   * <ul>
   * <li>select [...] [join x on x.col = z.col]</li>
   * </ul>
   */
  private List<String> joins = new ArrayList<>();

  /**
   * List of formatted wheres.
   *
   * <p>
   * This is used in the following clauses:
   *
   * <ul>
   * <li>select [...] where [column1 = x] [or column2 = y], [...]</li>
   * <li>update [...] where [column1 = x] [or column2 = y], [...]</li>
   * <li>delete [...] where [column1 = x] [or column2 = y], [...]</li>
   * </ul>
   */
  private List<String> wheres = new ArrayList<>();

  /**
   * List of where values.
   */
  private List<Object> whereValues = new ArrayList<>();

  /**
   * List of formatted orders.
   *
   * <p>
   * This is used in the following clauses:
   *
   * <ul>
   * <li>select [...] order by [column1 asc], [column2 desc], [...]</li>
   * </ul>
   */
  private List<String> orders = new ArrayList<>();

  /**
   * List of formatted foreign keys.
   */
  private List<String> foreignKeys = new ArrayList<>();

  /**
   * Formatted result limit.
   *
   * <p>
   * This is used in the following clauses:
   *
   * <ul>
   * <li>select [...] [limit 100]</li>
   * </ul>
   */
  private String limit = "";

  /**
   * Formatted result offset.
   *
   * <p>
   * This is used in the following clauses:
   *
   * <ul>
   * <li>select [...] [offset 100]</li>
   * </ul>
   */
  private String offset = "";

  /**
   * Build a formatted table clause.
   *
   * <p>
   * <code>"  table "</code> becomes <code>"table"</code>
   *
   * @param table The table to format.
   * @return      The formatted table.
   */
  public final String buildTable(final String table) {
    return table.trim();
  }

  /**
   * Add a table to the grammar.
   *
   * @param table The table to add.
   */
  public final void addTable(final String table) {
    this.table = this.buildTable(table);
  }

  /**
   * Build a formatted column clause.
   *
   * <p>
   * <code>"  column  "</code> becomes <code>"column"</code>
   *
   * @param column  The column to format.
   * @return        The formatted column.
   */
  public final String buildColumn(final String column) {
    return column.trim();
  }

  /**
   * Build a list of formatted columns.
   *
   * <p>
   * <code>["col1", "col2"]</code> becomes <code>"col1, col2"</code>
   *
   * @param columns The formatted columns.
   * @return        A comma-seprated list of columns.
   */
  public final String buildColumns(final List<String> columns) {
    return String.join(", ", columns);
  }

  /**
   * Add a column to the grammar.
   *
   * @param column The column to add.
   */
  public final void addColumn(final String column) {
    this.columns.add(this.buildColumn(column));
  }

  /**
   * Build a formatted value clause.
   *
   * <p>
   * This method simply returns a "?"-character as all values are removed from
   * statements prior to pre-compilation.
   *
   * @param value The value to format.
   * @return      The formatted value.
   */
  public final String buildValue(final Object value) {
    return "?";
  }

  /**
   * Build a list of formatted values.
   *
   * <p>
   * <code>["?", "?"]</code> becomes <code>"?, ?"</code>
   *
   * @param values  The formatted values.
   * @return        A comma-separated list of values.
   */
  public final String buildValues(final List<String> values) {
    return String.join(", ", values);
  }

  /**
   * Add a value to the grammar.
   *
   * @param value The value to add.
   */
  public final void addValue(final Object value) {
    // Store the original value for later access.
    this.rawValues.add(value);

    this.values.add(this.buildValue(value));
  }

  /**
   * Build a formatted set clause.
   *
   * <p>
   * <code>("col", "?")</code> becomes <code>"col = ?"</code>
   *
   * @param column  The formatted column of the set clause.
   * @param value   The formatted value of the set clause.
   * @return        The formatted set clause.
   */
  public final String buildSet(final String column, final String value) {
    return String.format("%s = %s", column, value);
  }

  /**
   * Build a list of formatted set clauses.
   *
   * <p>
   * <code>["col1", "col2"], ["?", "?"]</code> becomes <code>"set col1 = ?, col2 =
   * ?"</code>
   *
   * @param columns The columns of the set clause.
   * @param values  The values of the set clause.
   * @return        A list of comma-separated set clauses.
   */
  public final String buildSets(
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
   * Build a formatted join clause.
   *
   * @param type          The type of join to perform.
   * @param foreignTable  The foreign table to join.
   * @param localColumn   The column in the local table.
   * @param operator      The logical operator to use in the join constraint.
   * @param foreignColumn The column in the foreign table.
   * @return              A formatted join clause.
   */
  public final String buildJoin(
    final String type,
    final String foreignTable,
    final String localColumn,
    final String operator,
    final String foreignColumn
  ) {
    return String.format("%s join %s on %s %s %s",
      type.trim(),
      this.buildTable(foreignTable),
      this.buildColumn(localColumn),
      operator.trim(),
      this.buildColumn(foreignColumn)
    );
  }

  /**
   * Build a list of formatted join clauses.
   *
   * @param joins The formatted join clauses.
   * @return      A space-separated list of joins.
   */
  public final String buildJoins(final List<String> joins) {
    return String.join(" ", joins);
  }

  /**
   * Add a join clause to the grammar.
   *
   * @param type          The type of join to perform.
   * @param foreignTable  The foreign table to join.
   * @param localColumn   The column in the local table.
   * @param operator      The logical operator to use in the join constraint.
   * @param foreignColumn The column in the foreign table.
   */
  public final void addJoin(
    final String type,
    final String foreignTable,
    final String localColumn,
    final String operator,
    final String foreignColumn
  ) {
    this.joins.add(
      this.buildJoin(type, foreignTable, localColumn, operator, foreignColumn)
    );
  }

  /**
   * Build a formatted where clause.
   *
   * <p>
   * <code>("col", "=", "val", "and")</code> becomes <code>"and col = ?"</code>
   *
   * @param column      The column of the where clause.
   * @param operator    The operator of the where clause.
   * @param value       The value of the where clause.
   * @param comparator  The comparator to use.
   * @return            The formatted where clause.
   */
  public final String buildWhere(
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
   * <p>
   * <code>["and col1 = ?", "or col2 = ?"]</code> becomes <code>"where col1 =
   * ? or col2 = ?"</code>
   *
   * @param wheres  The formatted where clauses.
   * @return        A comma-separated list of where clauses.
   */
  public final String buildWheres(final List<String> wheres) {
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
  public final void addWhere(
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
   * <p>
   * <code>("col", "asc")</code> becomes <code>"col asc"</code>
   *
   * @param column    The column of the order by clause.
   * @param direction The direction of the ordering.
   * @return          The formatted order by clause.
   */
  public final String buildOrder(final String column, final String direction) {
    return String.format(
      "%s %s",
      this.buildColumn(column),
      direction.trim()
    );
  }

  /**
   * Build a list of formatted order by clauses.
   *
   * <p>
   * <code>["col1 asc", "col2 desc"]</code> becomes <code>"order by col1 asc,
   * col2 desc"</code>
   *
   * @param orders  The formatted order by clauses.
   * @return        A comma-separated list of order by clauses.
   */
  public final String buildOrders(final List<String> orders) {
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
  public final void addOrder(final String column, final String direction) {
    this.orders.add(this.buildOrder(column, direction));
  }

  /**
   * Build a formatted limit clause.
   *
   * <p>
   * <code>(100)</code> becomes <code>"limit 100"</code>
   *
   * @param limit The limit.
   * @return      The formatted limit clause.
   */
  public final String buildLimit(final int limit) {
    return (limit > 0) ? "limit " + limit : "";
  }

  /**
   * Add a limit to the grammar.
   *
   * @param limit The limit.
   */
  public final void addLimit(final int limit) {
    this.limit = this.buildLimit(limit);
  }

  /**
   * Build a formatted offset clause.
   *
   * <p>
   * <code>(100)</code> becomes <code>"offset 100"</code>
   *
   * @param offset  The offset.
   * @return        The formatted offset.
   */
  public final String buildOffset(final int offset) {
    return (offset > 0) ? "offset " + offset : "";
  }

  /**
   * Add an offset to the grammar.
   *
   * @param offset The offset.
   */
  public final void addOffset(final int offset) {
    this.offset = this.buildOffset(offset);
  }

  /**
   * Build a formatted column clause with a data type.
   *
   * <p>
   * <code>("col", "integer", 11, true)</code> becomes <code>"col integer(11)
   * not null"</code>
   *
   * @param column    The column.
   * @param type      The type of the column.
   * @param length    The length of the column.
   * @param required  Whether or not this column is required.
   * @return          The formatted column clause.
   */
  public final String buildDataType(
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
   * <p>
   * <code>("col", "text", false)</code> becomes <code>"col text null"</code>
   *
   * @param column    The column.
   * @param type      The type of the column.
   * @param required  Whether or not this column is required.
   * @return          The formatted column clause.
   */
  public final String buildDataType(
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
  public final void addDataType(
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
  public final void addDataType(
    final String column,
    final String type,
    final boolean required
  ) {
    this.addColumn(this.buildDataType(column, type, required));
  }

  /**
   * Build a formatted foreign key clause.
   *
   * <p>
   * <code>("col1", "table", "col2")</code> becomes <code>"foreign key(col1)
   * references table(col2) on update cascade on delete cascade"</code>
   *
   * @param column        The column.
   * @param foreignTable  The foreign table.
   * @param foreignColumn The foreign column.
   * @return              The formatted foreign key clause.
   */
  public final String buildForeignKey(
    final String column,
    final String foreignTable,
    final String foreignColumn
  ) {
    return String.format(
      "foreign key(%s) references %s(%s) on update cascade on delete cascade",
      column,
      foreignTable,
      foreignColumn
    );
  }

  /**
   * Build a list of formatted foreign keys.
   *
   * @param foreignKeys The formatted foreign keys.
   * @return            A comma-separated list of foreign keys.
   */
  public final String buildForeignKeys(final List<String> foreignKeys) {
    if (!foreignKeys.isEmpty()) {
      return ", " + String.join(", ", foreignKeys);
    }
    else {
      return "";
    }
  }

  /**
   * Add a foreign key to the grammar.
   *
   * @param column        The column.
   * @param foreignTable  The foreign table.
   * @param foreignColumn The foreign column.
   */
  public final void addForeignKey(
    final String column,
    final String foreignTable,
    final String foreignColumn
  ) {
    this.foreignKeys.add(this.buildForeignKey(
      column, foreignTable, foreignColumn
    ));
  }

  /**
   * Return the name of auto generated columns.
   *
   * @return The name of auto generated columns.
   */
  public abstract String generatedAutoIncrementRow();

  /**
   * Build an auto incrementing column.
   *
   * <p>
   * Auto incrementing keys are not part of the ANSI SQL standard and are not
   * implemented consistently across the different {@link Driver}s. It's
   * therefore left up to subclasses to implement it.
   *
   * @param column  The name of the column.
   * @return        A formatted auto incrementing column.
   */
  public abstract String buildAutoIncrement(final String column);

  /**
   * Add an auto incrementing column to the grammar.
   *
   * <p>
   * Auto incrementing keys are not part of the ANSI SQL standard and are not
   * implemented consistently across the different {@link Driver}s. It's
   * therefore left up to subclasses to implement it.
   *
   * @param column The name of the column.
   */
  public abstract void addAutoIncrement(final String column);

  /**
   * Compile a select statement based on the current state of the grammar.
   *
   * @return The full select statement.
   */
  public final String compileSelect() {
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
      "select %s from %s %s %s %s %s %s",
      this.buildColumns(this.columns),
      this.table,
      this.buildJoins(this.joins),
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
  public final List<Object> compileSelectValues() {
    return this.whereValues;
  }

  /**
   * Compile an insert statement based on the current state of the grammar.
   *
   * @return The full insert statement.
   */
  public final String compileInsert() {
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
  public final List<Object> compileInsertValues() {
    return this.rawValues;
  }

  /**
   * Compile an update statement based on the current state of the grammar.
   *
   * @return The full update statement.
   */
  public final String compileUpdate() {
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
  public final List<Object> compileUpdateValues() {
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
  public final String compileDelete() {
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
  public final List<Object> compileDeleteValues() {
    return this.whereValues;
  }

  /**
   * Compile a create statement based on the current state of the grammar.
   *
   * @return The full create statement.
   */
  public final String compileCreate() {
    return String.format(
      "create table if not exists %s (%s %s)",
      this.table,
      this.buildColumns(this.columns),
      this.buildForeignKeys(this.foreignKeys)
    ).trim().replaceAll(" {2,}", " ");
  }

  /**
   * Compile a drop statement based on the current state of the grammar.
   *
   * @return The full drop statement.
   */
  public final String compileDrop() {
    return String.format(
      "drop table if exists %s",
      this.table
    ).trim().replaceAll(" {2,}", " ");
  }
}
