/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Row class.
 *
 * @version 1.0.0
 */
public final class Row {
  /**
   * Internal map for storing column definitions.
   */
  private Map<String, Object> columns = new LinkedHashMap<>();

  /**
   * Get a column value from the row.
   *
   * @param column  The column to get.
   * @return        The column value.
   */
  public Object get(final String column) {
    return this.columns.get(column);
  }

  /**
   * Insert a value into a column.
   *
   * @param column  The column to insert the value into.
   * @param value   The value to insert.
   * @return        The current {@link Row} object, for chaining.
   */
  public Row put(final String column, final Object value) {
    this.columns.put(column, value);

    return this;
  }

  /**
   * Get a list of all columns in the row.
   *
   * @return A list of columns.
   */
  public List<String> getColumns() {
    return new ArrayList(this.columns.keySet());
  }

  /**
   * Get a list of all values in the row.
   *
   * @return A list of values.
   */
  public List<Object> getValues() {
    return new ArrayList(this.columns.values());
  }
}
