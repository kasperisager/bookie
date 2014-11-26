/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// SQL utilities
import java.sql.Connection;
import java.sql.SQLException;

// JUnit assertions
import static org.junit.Assert.assertEquals;

// JUnit annotations
import org.junit.After;
import org.junit.Test;

/**
 * Database class unit tests.
 *
 * @version 1.0.0
 */
public final class DatabaseTest {
  /**
   * List of databases to test aginst.
   */
  private List<Database> databases = new ArrayList<>();

  /**
   * Run after each test.
   *
   * @throws SQLException In case of a SQL error.
   */
  @After
  public void after() throws SQLException {
    for (Database db: this.databases) {
      db.execute("drop table if exists test");
    }
  }

  /**
   * Test initialization of databases.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testInitialization() throws SQLException {
    // Configure MySQL database.
    Properties mysqlConf = new Properties();
    mysqlConf.put("database", "test");
    mysqlConf.put("user", "travis");

    // Initialize MySQL database.
    this.databases.add(new Database(Driver.MYSQL, mysqlConf));

    // Configure PostgreSQL database.
    Properties postgresqlConf = new Properties();
    postgresqlConf.put("database", "test");
    postgresqlConf.put("user", "postgres");

    // Initialize PostgreSQL database.
    this.databases.add(new Database(Driver.POSTGRESQL, postgresqlConf));

    // Configure SQLite database.
    Properties sqliteConf = new Properties();
    sqliteConf.put("database", "test");

    // Initialize SQLite database.
    this.databases.add(new Database(Driver.SQLITE, sqliteConf));
  }

  /**
   * Test initialization of PostgreSQL database with empty properties.
   *
   * This should throw an exception as a PostgreSQL database cannot be
   * intialized without a database name.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPostgresqlWithEmptyProperties() throws SQLException {
    (new Database(Driver.POSTGRESQL, new Properties())).connect();
  }

  /**
   * Test initialization of MySQL database with empty properties.
   *
   * This should throw an exception as a MySQL database cannot be intialized
   * without a database name.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMysqlWithEmptyProperties() throws SQLException {
    (new Database(Driver.MYSQL, new Properties())).connect();
  }

  /**
   * Test initialization of SQLite database with empty properties.
   *
   * This should throw an exception as a SQLite database cannot be intialized
   * without a database name.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSqliteWithEmptyProperties() throws SQLException {
    (new Database(Driver.SQLITE, new Properties())).connect();
  }

  /**
   * Test the connection of each database.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testConnection() throws SQLException {
    for (Database db: this.databases) {
      Connection connection = db.connect();
    }
  }

  /**
   * Test SQL query execution of each database.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testQueryExecution() throws SQLException {
    for (Database db: this.databases) {
      // Test table creation.
      db.execute("create table test (test varchar(255))");

      List<Object> values1 = new ArrayList<>();
      values1.add("test");

      // Test row insertion.
      db.execute("insert into test (test) values (?)", values1);
      List<Row> res1 = db.execute("select test from test");
      assertEquals(1, res1.size());
      assertEquals("test", res1.get(0).get("test"));

      List<Object> values2 = new ArrayList<>();
      values2.add("tset");
      values2.add("test");

      // Test updating.
      db.execute("update test set test = ? where test = ?");
      List<Row> res2 = db.execute("select test from test", values2);
      assertEquals(1, res2.size());
      assertEquals("tset", res2.get(0).get("test"));

      List<Object> values3 = new ArrayList<>();
      values3.add("tset");

      // Test row deletion.
      db.execute("delete from person where test = ?", values3);
      List<Row> res3 = db.execute("select test from test");
      assertEquals(0, res3.size());

      // Test table deletion.
      db.execute("drop table test");
    }
  }
}
