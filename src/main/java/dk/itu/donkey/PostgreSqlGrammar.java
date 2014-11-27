/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

/**
 * PostgreSQL grammar class.
 *
 * @version 1.0.0
 */
public final class PostgreSqlGrammar extends Grammar {
  /**
   * Return the name of auto increment columns.
   *
   * @return The name of auto increment columns.
   */
  public String generatedAutoIncrementRow() {
    return "id";
  }

  /**
   * Build an auto incrementing column.
   *
   * @param column  The name of the column.
   * @return        The formatted auto incrementing column.
   */
  public String buildAutoIncrement(final String column) {
    return String.format("%s serial unique", column);
  }

  /**
   * Add an auto incrementing column to the grammar.
   *
   * @param column The name of the column.
   */
  public void addAutoIncrement(final String column) {
    this.addColumn(this.buildAutoIncrement(column));
  }
}
