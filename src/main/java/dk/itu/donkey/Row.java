/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The Row class is an extension of a linked hash map and is used for
 * representing database rows.
 *
 * @since 1.0.0 Initial release.
 */
public final class Row extends LinkedHashMap<String, Object> {
  /**
   * Get a list of all columns in the row.
   *
   * @return A list of columns.
   */
  public List<String> getColumns() {
    return new ArrayList<String>(this.keySet());
  }

  /**
   * Get a list of all values in the row.
   *
   * @return A list of values.
   */
  public List<Object> getValues() {
    return new ArrayList<Object>(this.values());
  }
}
