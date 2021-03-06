/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.ArrayList;
import java.util.List;

// SQL utilities
import java.sql.SQLException;

// JUnit assertions
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

// JUnit annotations
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Query class unit tests.
 *
 * @version 1.0.0
 */
public final class QueryTest {
  /**
   * List of databases to test against.
   */
  private List<Database> databases;

  /**
   * Initialize databases before each test and create some test data.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Before
  public void before() throws SQLException {
    this.databases = DatabaseTest.initializeDatabases();

    for (Database db: this.databases) {
      db.execute(
        "create table if not exists test ("
      + " text_col text,"
      + " integer_col integer,"
      + " double_col double precision"
      + ")"
      );
    }
  }

  /**
   * Clean up databases after each test.
   *
   * @throws SQLException In case of a SQL error.
   */
  @After
  public void after() throws SQLException {
    for (Database db: this.databases) {
      db.execute("drop table if exists test");
      db.execute("drop table if exists test1");
      db.execute("drop table if exists test2");
    }
  }

  /**
   * Test query instantiation.
   */
  @Test
  public void testInstantiation() {
    for (Database db: this.databases) {
      Query query1 = new Query(db, "test");

      // Test that databases can create separate Queries
      Query query2 = db.table("test");
      Query query3 = db.table("test");

      assertNotSame(query2, query3);
    }
  }

  /**
   * Test select statement building.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testSelect() throws SQLException {
    for (Database db: this.databases) {
      List<Object> values1 = new ArrayList<>();
      values1.add("test");
      values1.add(100);
      values1.add(2.123);

      db.execute(
        "insert into test (text_col, integer_col, double_col) values (?, ?, ?)",
        values1
      );

      // Test getting all columns from the table.
      List<Row> rows1 = db.table("test").select().get();

      assertEquals(1, rows1.size());

      Row row1 = rows1.get(0);
      assertEquals(3, row1.getColumns().size());
      assertEquals(3, row1.getValues().size());
      assertEquals("test", row1.get("text_col"));
      assertEquals(100, row1.get("integer_col"));
      assertEquals(2.123, row1.get("double_col"));

      // Test getting a subset of columns from the table.
      List<Row> rows2 = db.table("test")
                          .select("integer_col", "double_col")
                          .get();

      assertEquals(1, rows2.size());

      Row row2 = rows2.get(0);
      assertEquals(2, row2.getColumns().size());
      assertEquals(2, row2.getValues().size());
      assertEquals(100, row2.get("integer_col"));
      assertEquals(2.123, row2.get("double_col"));

      List<Object> values2 = new ArrayList<>();
      values2.add("test1");
      values2.add(1001);
      values2.add(2.1234);

      db.execute(
        "insert into test (text_col, integer_col, double_col) values (?, ?, ?)",
        values2
      );

      // Test getting several rows from the table.
      List<Row> rows3 = db.table("test").select().get();

      assertEquals(2, rows3.size());
    }
  }

  /**
   * Test select statement with join clause.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testSelectWithJoin() throws SQLException {
    for (Database db: this.databases) {
      db.execute(
        "create table if not exists test1 ("
      + "column1 integer"
      + ")"
      );

      db.execute(
        "create table if not exists test2 ("
      + "column2 integer"
      + ")"
      );

      List<Object> values1 = new ArrayList<>();
      values1.add(null);

      List<Object> values2 = new ArrayList<>();
      values2.add(1);

      for (int i = 0; i < 3; i++) {
        db.execute("insert into test1 (column1) values (?)", values1);
      }

      for (int i = 0; i < 2; i++) {
        db.execute("insert into test1 (column1) values (?)", values2);
      }

      for (int i = 0; i < 4; i++) {
        db.execute("insert into test2 (column2) values (?)", values2);
      }

      List<Row> rows1 = db.table("test1")
                          .join(
                            "test2", "test1.column1", "test2.column2"
                          )
                          .get();

      assertEquals(8, rows1.size());

      for (Row row: rows1) {
        assertEquals(row.get("column1"), row.get("column1"));
      }

      List<Row> rows2 = db.table("test1")
                          .leftJoin(
                            "test2", "test1.column1", "test2.column2"
                          )
                          .get();

      assertEquals(11, rows2.size());

      for (Row row: rows2) {
        assertEquals(row.get("column1"), row.get("column1"));
      }
    }
  }

  /**
   * Test select statement building with where clauses.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testSelectWithWhere() throws SQLException {
    for (Database db: this.databases) {
      List<Object> values1 = new ArrayList<>();
      values1.add("kasper");
      values1.add(20);

      db.execute(
        "insert into test (text_col, integer_col) values (?, ?)", values1
      );

      List<Object> values2 = new ArrayList<>();
      values2.add("karsten");
      values2.add(34);

      db.execute(
        "insert into test (text_col, integer_col) values (?, ?)", values2
      );

      // Test with a single where, using the equality shorthand.
      List<Row> rows1 = db.table("test")
                          .select("text_col", "integer_col")
                          .where("text_col", "kasper")
                          .get();

      assertEquals(1, rows1.size());

      Row row1 = rows1.get(0);
      assertEquals("kasper", row1.get("text_col"));
      assertEquals(20, row1.get("integer_col"));

      // Test with a single where, using a logical operator.
      List<Row> rows2 = db.table("test")
                          .select("text_col", "integer_col")
                          .where("integer_col", ">", 20)
                          .get();

      assertEquals(1, rows2.size());

      Row row2 = rows2.get(0);
      assertEquals("karsten", row2.get("text_col"));
      assertEquals(34, row2.get("integer_col"));

      // Test with multiple wheres.
      List<Row> rows3 = db.table("test")
                          .select("text_col", "integer_col")
                          .where("integer_col", ">", 20)
                          .where("text_col", "karsten")
                          .get();

      assertEquals(1, rows3.size());

      // Test with multiple wheres, using an or where as well.
      List<Row> rows4 = db.table("test")
                          .select("text_col", "integer_col")
                          .where("text_col", "kasper")
                          .orWhere("integer_col", ">=", 34)
                          .get();

      assertEquals(2, rows4.size());

      // Test with multiple wheres, using an or where with the equality
      // shorthand.
      List<Row> rows5 = db.table("test")
                          .select("text_col")
                          .where("text_col", "kasper")
                          .orWhere("text_col", "karsten")
                          .get();

      assertEquals(2, rows5.size());
    }
  }

  /**
   * Test select statement building returning only the first result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testSelectFirst() throws SQLException {
    for (Database db: this.databases) {
      List<Object> values = new ArrayList<>();
      values.add("test");

      for (int i = 0; i < 2; i++) {
        db.execute("insert into test (text_col) values (?)", values);
      }

      Row row = db.table("test").first();
      assertEquals("test", row.get("text_col"));
    }
  }

  /**
   * Test select statement building returning only the first result, with the
   * result being empty.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testSelectFirstWithEmptyResult() throws SQLException {
    for (Database db: this.databases) {
      assertNull(db.table("test").first());
    }
  }

  /**
   * Test select statement building returning a result ordered by one or more
   * columns.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testSelectWithOrderBy() throws SQLException {
    for (Database db: this.databases) {
      int n = 4;

      for (int i = 0; i < n; i++) {
        List<Object> values = new ArrayList<>();
        values.add(i);

        db.execute("insert into test (integer_col) values (?)", values);
      }

      List<Row> asc = db.table("test").orderBy("integer_col").get();
      assertEquals(n, asc.size());

      List<Row> desc = db.table("test").orderBy("integer_col", "desc").get();
      assertEquals(n, desc.size());

      for (int i = 0; i < n; i++) {
        assertEquals(i, asc.get(i).get("integer_col"));
        assertEquals(n - i - 1, desc.get(i).get("integer_col"));
      }
    }
  }

  /**
   * Test select statement building with a limit on the result size.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testSelectLimit() throws SQLException {
    for (Database db: this.databases) {
      List<Object> values = new ArrayList<>();
      values.add("test");

      for (int i = 0; i < 5; i++) {
        db.execute("insert into test (text_col) values (?)", values);
      }

      List<Row> rows = db.table("test").limit(3).get();
      assertEquals(3, rows.size());
    }
  }

  /**
   * Test select statement building with an offset on the result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testSelectOffset() throws SQLException {
    for (Database db: this.databases) {
      for (int i = 0; i < 4; i++) {
        List<Object> values = new ArrayList<>();
        values.add(i);

        db.execute("insert into test (integer_col) values (?)", values);
      }

      List<Row> rows = db.table("test").offset(2).get();
      assertEquals(2, rows.size());
      assertEquals(2, rows.get(0).get("integer_col"));
      assertEquals(3, rows.get(1).get("integer_col"));
    }
  }

  /**
   * Test select statement building with a count on the result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testCount() throws SQLException {
    for (Database db: this.databases) {
      for (int i = 0; i < 3; i++) {
        List<Object> values = new ArrayList<>();
        values.add("KasperGiverKage");

        db.execute("insert into test (text_col) values (?)", values);
      }

      Number count = db.table("test").count();
      assertEquals(3, count.intValue());
    }
  }

  /**
   * Test select statement building with a max on the result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testMax() throws SQLException {
    for (Database db: this.databases) {
      for (int i = 0; i < 3; i++) {
        List<Object> values = new ArrayList<>();
        values.add(i);

        db.execute("insert into test (integer_col) values (?)", values);
      }

      int max = (int) db.table("test").max("integer_col");
      assertEquals(2, max);
    }
  }

  /**
   * Test select statement building with a min on the result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testMin() throws SQLException {
    for (Database db : this.databases) {
      for (int i = 0; i < 3; i++) {
        List<Object> values = new ArrayList<>();
        values.add(i);

        db.execute("insert into test (integer_col) values (?)", values);
      }

      int min = (int) db.table("test").min("integer_col");
      assertEquals(0, min);
    }
  }

  /**
   * Test select statement building with a min on the result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testSum() throws SQLException {
    for (Database db : this.databases) {
      for (int i = 0; i < 3; i++) {
        List<Object> values = new ArrayList<>();
        values.add(i);

        db.execute("insert into test (integer_col) values (?)", values);
      }

      Number sum = (Number) db.table("test").sum("integer_col");
      assertEquals(3, sum.intValue());
    }
  }

  /**
   * Test select statement building with a avg on the result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testAvg() throws SQLException {
    for (Database db : this.databases) {
      for (int i = 0; i < 3; i++) {
        List<Object> values = new ArrayList<>();
        values.add(i);

        db.execute("insert into test (integer_col) values (?)", values);
      }

      Number avg = (Number) db.table("test").avg("integer_col");
      assertEquals(1, avg.intValue());
    }
  }

  /**
   * Test select statement building with a insert on the result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testInsert() throws SQLException {
    for (Database db : this.databases) {
      Row row1 = new Row();
      row1.put("text_col", "SigridErNice");
      row1.put("integer_col", 4);
      row1.put("double_col", 5.5);

      db.table("test").insert(row1);
      List<Row> rows = db.execute("select * from test");

      Row row2 = rows.get(0);
      assertEquals(1, rows.size());
      assertEquals("SigridErNice", (String) row2.get("text_col"));
      assertTrue(4 == (int) row2.get("integer_col"));
      assertTrue(5.5 == (double) row2.get("double_col"));
    }
  }

  /**
   * Test select statement building with a update on the result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testUpdate() throws SQLException {
    for (Database db : this.databases) {
      for (int i = 0; i < 4; i++) {
        List<Object> values = new ArrayList<>();
        values.add("Kasper");
        values.add(2);

        db.execute(
          "insert into test (text_col, integer_col) values (?, ?)", values
        );
      }

      Row row1 = new Row();
      row1.put("text_col", "Sigrid");

      db.table("test").update(row1);

      List<Row> rows = db.execute("select * from test");

      for (Row row: rows) {
        assertEquals("Sigrid", row.get("text_col"));
      }
    }
  }

  /**
   * Test select statement building with a delete on the result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testUpdateWithWhere() throws SQLException {
    for (Database db : this.databases) {
      List<Object> values1 = new ArrayList<>();
      values1.add("Kasper");
      List<Object> values2 = new ArrayList<>();
      values2.add("Sigrid");
      List<Object> values3 = new ArrayList<>();
      values3.add("Sigrid");

      db.execute("insert into test (text_col) values (?)", values1);
      db.execute("insert into test (text_col) values (?)", values2);
      db.execute("insert into test (text_col) values (?)", values3);

      Row row = new Row();
      row.put("text_col", "Per");

      db.table("test").where("text_col", "Sigrid").update(row);

      List<Row> rows = db.execute("select * from test");
      assertEquals("Kasper", rows.get(0).get("text_col"));
      assertEquals("Per", rows.get(1).get("text_col"));
      assertEquals("Per", rows.get(2).get("text_col"));
    }
  }

  /**
   * Test select statement building with a delete on the result.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testDelete() throws SQLException {
    for (Database db : this.databases) {
      for (int i = 0; i < 3; i++) {
        List<Object> values = new ArrayList<>();
        values.add(i);

        db.execute("insert into test (integer_col) values (?)", values);
      }

      db.table("test").delete();
      List<Row> rows = db.execute("select * from test");
      assertEquals(0, rows.size());
    }
  }

  /**
   * Test select statement building with a delete with where clause.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testDeleteWithWhere() throws SQLException {
    for (Database db : this.databases) {
      List<Object> values1 = new ArrayList<>();
      values1.add("Kasper");
      List<Object> values2 = new ArrayList<>();
      values2.add("Sigrid");
      List<Object> values3 = new ArrayList<>();
      values3.add("Sigrid");

      db.execute("insert into test (text_col) values (?)", values1);
      db.execute("insert into test (text_col) values (?)", values2);
      db.execute("insert into test (text_col) values (?)", values3);

      db.table("test").where("text_col", "Sigrid").delete();

      List<Row> rows = db.execute("select * from test");

      assertEquals(1, rows.size());
      assertEquals("Kasper", rows.get(0).get("text_col"));
    }
  }
}

