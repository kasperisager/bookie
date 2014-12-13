/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey.grammar;

// Base grammar
import dk.itu.donkey.Grammar;

/**
 * SQLite grammar class.
 *
 * @since 1.0.0 Initial release.
 */
public final class SqliteGrammar extends Grammar {
  /**
   * Return the name of auto increment columns.
   *
   * @return The name of auto increment columns.
   */
  @Override
  public String generatedAutoIncrementRow() {
    return "last_insert_rowid()";
  }

  /**
   * Build an auto incrementing column.
   *
   * @param column  The name of the column.
   * @return        The formatted auto incrementing column.
   */
  @Override
  protected String buildAutoIncrement(final String column) {
    return String.format("%s integer primary key autoincrement", column);
  }

  /**
   * Add an auto incrementing column to the grammar.
   *
   * @param column The name of the column.
   */
  @Override
  public void addAutoIncrement(final String column) {
    this.addColumn(this.buildAutoIncrement(column));
  }
}
