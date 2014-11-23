/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Row class unit tests.
 *
 * @version 1.0.0
 */
public final class RowTest {
  /**
   * Test row initialization.
   */
  @Test
  public void testInitialization() {
    Row row = new Row();
    assertNotNull(row);
  }

  /**
   * Test row data storage.
   */
  @Test
  public void testStorage() {
    Row row = new Row();

    row.put("column1", "test");
    row.put("column2", 100);
    row.put("column3", 2.123);
    row.put("column4", false);
    row.put("column5", true);

    assertEquals("test", row.get("column1"));
    assertEquals(100, row.get("column2"));
    assertEquals(2.123, row.get("column3"));
    assertEquals(false, row.get("column4"));
    assertEquals(true, row.get("column5"));

    List<String> columns = new ArrayList<>();
    columns.add("column1");
    columns.add("column2");
    columns.add("column3");
    columns.add("column4");
    columns.add("column5");

    List<Object> values = new ArrayList<>();
    values.add("test");
    values.add(100);
    values.add(2.123);
    values.add(false);
    values.add(true);

    assertEquals(columns, row.getColumns());
    assertEquals(values, row.getValues());
  }
}
