/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey.grammar;

// Base grammar
import dk.itu.donkey.Grammar;

/**
 * MySQL grammar class.
 *
 * @since 1.0.0 Initial release.
 */
public final class MySqlGrammar extends Grammar {
  /**
   * Return the name of auto increment columns.
   *
   * @return The name of auto increment columns.
   */
  public String generatedAutoIncrementRow() {
    return "GENERATED_KEY";
  }

  /**
   * Build an auto incrementing column.
   *
   * @param column  The name of the column.
   * @return        The formatted auto incrementing column.
   */
  public String buildAutoIncrement(final String column) {
    return String.format("%s integer primary key auto_increment", column);
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
