/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.List;

// SQL utilities
import java.sql.SQLException;

// JUnit assertions
import static org.junit.Assert.assertNotSame;

// JUnit annotations
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Schema class unit tests.
 *
 * @version 1.0.0
 */
public final class SchemaTest {
  /**
   * List of databases to test against.
   */
  private List<Database> databases;

  /**
   * Initialize databases before each test.
   */
  @Before
  public void before() {
    this.databases = DatabaseTest.initializeDatabases();
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
    }
  }

  /**
   * Test schema instantiation.
   */
  @Test
  public void testInstantiation() {
    for (Database db: this.databases) {
      Schema schema1 = new Schema(db);

      // Test that databases can create separate Schemas
      Schema schema2 = db.schema();
      Schema schema3 = db.schema();

      assertNotSame(schema2, schema3);
    }
  }

  /**
   * Test schema table creation.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testCreate() throws SQLException {
    for (Database db: this.databases) {
      // Create test table.
      db.schema().create("test")

        // Test data types.
        .text("textCol")
        .integer("integerCol")
        .doublePrecision("doubleCol")
        .floatingPoint("floatCol")
        .longInteger("longCol")
        .real("realCol")
        .numeric("numericCol")
        .bool("boolCol")
        .date("dateCol")
        .time("timeCol")
        .timestamp("timestampCol")
        .increments("incrementsCol")

        // Run the schema.
        .run();

      // Verify that the table and all columns were created.
      db.execute(
        "select textCol, integerCol, doubleCol, floatCol, longCol, realCol,"
      + " numericCol, boolCol, dateCol, timeCol, timestampCol, incrementsCol"
      + " from test"
      );
    }
  }

  /**
   * Test schema table creation with foreign keys.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testCreateWithForeignKeys() throws SQLException {
    for (Database db: this.databases) {
      // Clean up after earlier tests if they failed.
      db.execute("drop table if exists test2");
      db.execute("drop table if exists test1");

      db.schema().create("test1")
        .increments("column1")
        .run();

      db.schema().create("test2")
        .integer("column2")
        .foreignKey("column2", "test1", "column1")
        .run();

      db.execute("select column2 from test2");

      db.execute("drop table test2");
      db.execute("drop table test1");
    }
  }

  /**
   * Test schema table dropping.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testDrop() throws SQLException {
    for (Database db: this.databases) {
      // Create a test table.
      db.schema().create("test")
        .text("col")
        .run();

      // Drop the test table.
      db.schema().drop("test");

      // Re-create the test table. If it wasn't dropped above, this query will
      // fail. This is run as a raw SQL query as Schema does existance checking
      // before attempting to create tables. We however want to provoke an
      // error.
      db.execute("create table test (col text)");
    }
  }
}
