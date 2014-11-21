/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey.database;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.sql.Connection;
import java.sql.SQLException;

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

    // Configure SQLite database.
    Properties sqliteConf = new Properties();
    sqliteConf.put("database", "test");

    // Initialize SQLite database.
    this.databases.add(new Database(Driver.SQLITE, sqliteConf));
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
    (new Database(Driver.SQLITE, new Properties())).getConnection();
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
    (new Database(Driver.MYSQL, new Properties())).getConnection();
  }

  /**
   * Test the connection of each database.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testConnection() throws SQLException {
    for (Database db: this.databases) {
      Connection connection = db.getConnection();
    }
  }

  /**
   * Test general SQL query execution of each database.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testQueryExecution() throws SQLException {
    for (Database db: this.databases) {
      // Create a test table in the databases with a single column.
      db.execute("create table test (test varchar(255))");

      // Insert a row into the newly created database table.
      db.execute("insert into test (test) values ('test')");

      // Select all rows in the test table.
      List<Row> res = db.execute("select test from test");

      // Verify that the result is what was inserted above.
      assertEquals(res.size(), 1);
      assertEquals(res.get(0).get("test"), "test");

      // Nuke the test table.
      db.execute("drop table test");
    }
  }

  /**
   * Test parametized SQL query execution of each database.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testParametizedQueryExecution() throws SQLException {
    for (Database db: this.databases) {
      // Create a test table in the databases with a single column.
      db.execute("create table test(test varchar(255))");

      // Create a list of values for use in a parametized query.
      List<Object> values = new ArrayList<>();
      values.add("test");

      // Insert a row into the newly created database table.
      db.execute("insert into test (test) values (?)", values);

      // Select all rows in the test table.
      List<Row> res = db.execute("select test from test");

      // Verify that the result is what was inserted above.
      assertEquals(res.size(), 1);
      assertEquals(res.get(0).get("test"), "test");

      // Nuke the test table.
      db.execute("drop table test");
    }
  }
}
