/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Row class.
 *
 * @version 1.0.0
 */
public final class Row extends LinkedHashMap<String, Object> {
  /**
   * Get a list of all columns in the row.
   *
   * @return A list of columns.
   */
  public List<String> getColumns() {
    return new ArrayList(this.keySet());
  }

  /**
   * Get a list of all values in the row.
   *
   * @return A list of values.
   */
  public List<Object> getValues() {
    return new ArrayList(this.values());
  }
}
