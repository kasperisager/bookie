/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

/**
 * SQLite grammar class.
 *
 * @version 1.0.0
 */
public final class SqliteGrammar extends Grammar {
  /**
   * Build an auto incrementing column.
   *
   * @param column  The name of the column.
   * @return        The formatted auto incrementing column.
   */
  public String buildAutoIncrement(final String column) {
    return String.format("%s integer primary key autoincrement", column);
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
