/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Grammar class unit tests.
 *
 * @version 1.0.0
 */
public final class GrammarTest {
  /**
   * Grammar object to test against.
   */
  private Grammar g;

  /**
   * Run before each test.
   *
   * This re-intializes the grammar object after each test so the next test
   * can start off fresh.
   */
  @Before
  public void before() {
    this.g = new Grammar();
  }

  /**
   * Test table building.
   */
  @Test
  public void testBuildTable() {
    assertEquals("test", this.g.buildTable("test"));
  }

  /**
   * Test column building.
   */
  @Test
  public void testBuildColumn() {
    assertEquals("test", this.g.buildColumn("test"));
  }

  /**
   * Test column-list building.
   */
  @Test
  public void testBuildColumns() {
    List<String> columns = new ArrayList<>();
    columns.add(this.g.buildColumn("test1"));
    columns.add(this.g.buildColumn("test2"));
    columns.add(this.g.buildColumn("test3"));

    assertEquals("test1, test2, test3", this.g.buildColumns(columns));
  }

  /**
   * Test value building.
   */
  @Test
  public void testBuildValue() {
    assertEquals("?", this.g.buildValue("test"));
    assertEquals("?", this.g.buildValue(100));
    assertEquals("?", this.g.buildValue(2.123));
  }

  /**
   * Test value-list building.
   */
  @Test
  public void testBuildValues() {
    List<String> values = new ArrayList<>();
    values.add(this.g.buildValue("test"));
    values.add(this.g.buildValue(100));
    values.add(this.g.buildValue(2.123));

    assertEquals("?, ?, ?", this.g.buildValues(values));
  }

  /**
   * Test set building.
   */
  @Test
  public void testBuildSet() {
    String column = this.g.buildColumn("test");

    String value1 = this.g.buildValue("test");
    assertEquals("test = ?", this.g.buildSet(column, value1));

    String value2 = this.g.buildValue(100);
    assertEquals("test = ?", this.g.buildSet(column, value2));

    String value3 = this.g.buildValue(2.123);
    assertEquals("test = ?", this.g.buildSet(column, value3));
  }

  /**
   * Test set-list building.
   */
  @Test
  public void testBuildSets() {
    List<String> columns = new ArrayList<>();
    columns.add(this.g.buildColumn("test1"));
    columns.add(this.g.buildColumn("test2"));
    columns.add(this.g.buildColumn("test3"));

    List<String> values = new ArrayList<>();
    values.add(this.g.buildValue("test"));
    values.add(this.g.buildValue(100));
    values.add(this.g.buildValue(2.123));

    assertEquals(
      "set test1 = ?, test2 = ?, test3 = ?",
      this.g.buildSets(columns, values)
    );
    assertEquals("", this.g.buildSets(new ArrayList<>(), new ArrayList<>()));
  }

  /**
   * Test where building.
   */
  @Test
  public void testBuildWhere() {
    assertEquals("and test = ?", this.g.buildWhere("test", "=", "val", "and"));
    assertEquals("or test > ?", this.g.buildWhere("test", ">", 100, "or"));
    assertEquals("and test < ?", this.g.buildWhere("test", "<", 2.123, "and"));
    assertEquals("or col <= ?", this.g.buildWhere("col", "<=", 21, "or"));
  }

  /**
   * Test where-list building.
   */
  @Test
  public void testBuildWheres() {
    List<String> wheres = new ArrayList<>();
    wheres.add(this.g.buildWhere("test", "=", "val", "and"));
    wheres.add(this.g.buildWhere("test", ">", 100, "or"));
    wheres.add(this.g.buildWhere("test", "<", 2.123, "and"));

    assertEquals(
      "where test = ? or test > ? and test < ?",
      this.g.buildWheres(wheres)
    );
    assertEquals("", this.g.buildWheres(new ArrayList<>()));
  }

  /**
   * Test order by building.
   */
  @Test
  public void testBuildOrder() {
    assertEquals("test asc", this.g.buildOrder("test", "asc"));
    assertEquals("test desc", this.g.buildOrder("test", "desc"));
  }

  /**
   * Test order-list building.
   */
  @Test
  public void testBuildOrders() {
    List<String> orders = new ArrayList<>();
    orders.add(this.g.buildOrder("test", "asc"));
    orders.add(this.g.buildOrder("test", "desc"));
    orders.add(this.g.buildOrder("col", "asc"));

    assertEquals(
      "order by test asc, test desc, col asc",
      this.g.buildOrders(orders)
    );
    assertEquals("", this.g.buildOrders(new ArrayList<>()));
  }

  /**
   * Test limit building.
   */
  @Test
  public void testBuildLimit() {
    assertEquals("limit 100", this.g.buildLimit(100));
    assertEquals("", this.g.buildLimit(0));
  }

  /**
   * Test offset building.
   */
  @Test
  public void testBuildOffset() {
    assertEquals("offset 100", this.g.buildOffset(100));
    assertEquals("", this.g.buildOffset(0));
  }

  /**
   * Test data type column building.
   */
  @Test
  public void testBuildDataType() {
    assertEquals(
      "test varchar(123) null",
      this.g.buildDataType("test", "varchar", 123, false)
    );
    assertEquals(
      "test integer(321) not null",
      this.g.buildDataType("test", "integer", 321, true)
    );
    assertEquals(
      "test integer null",
      this.g.buildDataType("test", "integer", false)
    );
    assertEquals(
      "test time not null",
      this.g.buildDataType("test", "time", true)
    );
  }

  /**
   * Test foreign key building.
   */
  @Test
  public void testBuildForeignKey() {
    assertEquals(
      "foreign key(test1) references test2(test3)",
      this.g.buildForeignKey("test1", "test2", "test3")
    );
  }

  /**
   * Test select-statement compilation.
   */
  @Test
  public void testCompileSelect() {
    this.g.addTable("test");

    this.g.addColumn("column1");
    this.g.addColumn("column2");
    this.g.addColumn("column3");
    this.g.addColumn("column4");

    this.g.addWhere("column1", ">", 100, "and");
    this.g.addWhere("column2", "=", "value1", "and");
    this.g.addWhere("column3", "<", 2.123, "or");
    this.g.addWhere("column4", "=", "value2", "or");

    this.g.addOrder("column2", "asc");
    this.g.addOrder("column4", "desc");

    this.g.addLimit(101);
    this.g.addOffset(102);

    assertEquals(
      "select column1, column2, column3, column4 from test"
    + " where column1 > ? and column2 = ? or column3 < ? or column4 = ?"
    + " order by column2 asc, column4 desc"
    + " limit 101"
    + " offset 102",
      this.g.compileSelect()
    );

    List<Object> values = new ArrayList<>();
    values.add(100);
    values.add("value1");
    values.add(2.123);
    values.add("value2");

    assertEquals(values, this.g.compileSelectValues());
  }

  /**
   * Test select-statement compilation without columns.
   */
  @Test
  public void testCompileSelectWithoutColumns() {
    this.g.addTable("table");

    assertEquals("select * from table", this.g.compileSelect());
  }

  /**
   * Test select-statement compilation without limit, but with offset.
   */
  @Test
  public void testCompileSelectWithoutLimitWithOffset() {
    this.g.addTable("table");

    this.g.addOffset(100);

    assertEquals(
      "select * from table limit " + Integer.MAX_VALUE + " offset 100",
      this.g.compileSelect()
    );
  }

  /**
   * Test insert-statement compilation.
   */
  @Test
  public void testCompileInsert() {
    this.g.addTable("test");

    this.g.addColumn("column1");
    this.g.addValue("value1");
    this.g.addColumn("column2");
    this.g.addValue(100);
    this.g.addColumn("column3");
    this.g.addValue(2.123);

    assertEquals(
      "insert into test (column1, column2, column3) values (?, ?, ?)",
      this.g.compileInsert()
    );

    List<Object> values = new ArrayList<>();
    values.add("value1");
    values.add(100);
    values.add(2.123);

    assertEquals(values, this.g.compileInsertValues());
  }

  /**
   * Test update-statement compilation.
   */
  @Test
  public void testCompileUpdate() {
    this.g.addTable("test");

    this.g.addWhere("column1", "=", "value1", "and");

    this.g.addColumn("column1");
    this.g.addValue("value1");
    this.g.addColumn("column2");
    this.g.addValue(100);

    this.g.addWhere("column2", ">", 80, "or");

    this.g.addColumn("column3");
    this.g.addValue(2.123);

    assertEquals(
      "update test set column1 = ?, column2 = ?, column3 = ?"
    + " where column1 = ? or column2 > ?",
      this.g.compileUpdate()
    );

    List<Object> values = new ArrayList<>();
    values.add("value1");
    values.add(100);
    values.add(2.123);
    values.add("value1");
    values.add(80);

    assertEquals(values, this.g.compileUpdateValues());
  }

  /**
   * Test delete-statement compilation.
   */
  @Test
  public void testCompileDelete() {
    this.g.addTable("test");

    this.g.addWhere("test1", "=", "val", "and");
    this.g.addWhere("test2", ">", 100, "or");
    this.g.addWhere("test3", "<", 2.123, "and");

    assertEquals(
      "delete from test where test1 = ? or test2 > ? and test3 < ?",
      this.g.compileDelete()
    );

    List<Object> values = new ArrayList<>();
    values.add("val");
    values.add(100);
    values.add(2.123);

    assertEquals(values, this.g.compileDeleteValues());
  }

  /**
   * Test create-statement compilation.
   */
  @Test
  public void testCompileCreate() {
    this.g.addTable("test");

    this.g.addDataType("test1", "varchar", 123, true);
    this.g.addDataType("test2", "integer", 321, true);
    this.g.addDataType("test3", "date", false);

    this.g.addForeignKey("test2", "table", "column");

    assertEquals(
      "create table if not exists test ("
    + "test1 varchar(123) not null,"
    + " test2 integer(321) not null,"
    + " test3 date null,"
    + " foreign key(test2) references table(column)"
    + ")",
      this.g.compileCreate()
    );
  }

  /**
   * Test drop-statement compilation.
   */
  @Test
  public void testCompileDrop() {
    this.g.addTable("test");

    assertEquals("drop table if exists test", this.g.compileDrop());
  }
}
