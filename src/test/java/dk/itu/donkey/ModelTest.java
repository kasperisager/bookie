/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.List;

// SQL utilities
import java.sql.SQLException;

// JUnit assertions
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

// JUnit annotations
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// Model fixtures
import dk.itu.donkey.fixture.ConcreteModel1;
import dk.itu.donkey.fixture.ConcreteModel2;
import dk.itu.donkey.fixture.ConcreteModel3;
import dk.itu.donkey.fixture.ConcreteModel4;
import dk.itu.donkey.fixture.ConcreteModel5;

/**
 * Model class unit tests.
 *
 * @version 1.0.0
 */
public final class ModelTest {
  /**
   * Current database being tested against.
   */
  private static Database db;

  /**
   * List of database to test against.
   */
  private List<Database> databases;

  /**
   * Grab the current database being tested against.
   *
   * @return The current database being tested against.
   */
  public static Database db() {
    return ModelTest.db;
  }

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
      db.execute("drop table if exists test1");
      db.execute("drop table if exists test2");
    }
  }

  /**
   * Test model initialization.
   */
  @Test
  public void testInitialization() {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      ConcreteModel1 model = new ConcreteModel1();
    }
  }

  /**
   * Test model initialization from a database row.
   */
  @Test
  public void testInitializationFromRow() {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      Row row1 = new Row();
      row1.put("id", 1);
      row1.put("floatwrapped", 2123f);
      row1.put("floatprimitive", 3123f);
      row1.put("longwrapped", 2123L);
      row1.put("longprimitive", 3123L);
      row1.put("booleanwrapped", false);
      row1.put("booleanprimitive", true);

      ConcreteModel1 model1 = new ConcreteModel1();
      model1.setRow(row1);
      assertTrue(1 == model1.id());
      assertTrue(2123f == model1.floatWrapped);
      assertTrue(3123f == model1.floatPrimitive);
      assertTrue(2123L == model1.longWrapped);
      assertTrue(3123L == model1.longPrimitive);
      assertFalse(model1.booleanWrapped);
      assertTrue(model1.booleanPrimitive);

      Row row2 = new Row();
      row2.put("id", 2);
      row2.put("string", "test");
      row2.put("intwrapped", 100);
      row2.put("intprimitive", 200);
      row2.put("doublewrapped", 2.123);
      row2.put("doubleprimitive", 3.123);
      row2.put("model", model1);

      ConcreteModel2 model2 = new ConcreteModel2();
      model2.setRow(row2);
      assertTrue(2 == model2.id());
      assertEquals("test", model2.string);
      assertTrue(100 == model2.intWrapped);
      assertTrue(200 == model2.intPrimitive);
      assertTrue(2.123 == model2.doubleWrapped);
      assertTrue(3.123 == model2.doublePrimitive);
      assertEquals(model1, model2.model);
    }
  }

  /**
   * Test model initialization from a database row with a non-existing field.
   */
  @Test
  public void testInitializationFromRowWithNonExistingField() {
   for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      Row row = new Row();
      row.put("field", "test");
      row.put("nonexisting", "tset");

      ConcreteModel3 model = new ConcreteModel3();
      model.setRow(row);
      assertNull(model.id());
      assertEquals("test", model.field);
    }
  }

  /**
   * Test model initialization from a null database row.
   */
  @Test
  public void testInitializationFromNullRow() {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      Row row = null;

      ConcreteModel3 model = new ConcreteModel3();
      model.setRow(row);
      assertNull(model.field);
    }
  }

  /**
   * Test model initialization from a database row with a null field.
   */
  @Test
  public void testInitializationFromRowWithNullField() {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      Row row = new Row();
      row.put("field", null);

      ConcreteModel3 model = new ConcreteModel3();
      model.setRow(row);
      assertNull(model.field);
    }
  }

  /**
   * Test dynamic model field methods.
   */
  @Test
  public void testFieldMethods() {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      ConcreteModel3 model = new ConcreteModel3();

      model.setField("field", "Value2");
      assertEquals("Value2", model.field);

      // Should not blow up.
      model.setField("doesNotExist", "Herp");
    }
  }

  /**
   * Test model instantiation.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testModelInstantiation() throws SQLException {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      ConcreteModel3 model1 = Model.instantiate(ConcreteModel3.class);
      assertNotNull(model1);

      model1.field = "Test";
      model1.insert();
    }
  }

  /**
   * Test model instantiation with an illegal type.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testModelInstantiationWithWrongType() {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      Model.instantiate(String.class);
    }
  }

  /**
   * Test model insertion.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testInsert() throws SQLException {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      ConcreteModel1 model1 = new ConcreteModel1();

      model1.floatWrapped = 2123f;
      model1.floatPrimitive = 3123f;
      model1.longWrapped = 2123L;
      model1.longPrimitive = 3123L;
      model1.booleanWrapped = false;
      model1.booleanPrimitive = true;

      assertTrue(model1.insert());
      assertFalse(model1.insert());

      List<Row> rows1 = db.table(model1.table()).get();
      assertEquals(1, rows1.size());

      Row row1 = rows1.get(0);
      assertTrue(2123f == (Float) row1.get("floatwrapped"));
      assertTrue(3123f == (float) row1.get("floatprimitive"));
      assertTrue(2123L == (Long) row1.get("longwrapped"));
      assertTrue(3123L == (long) row1.get("longprimitive"));
      assertFalse((Boolean) row1.get("booleanwrapped"));
      assertTrue((boolean) row1.get("booleanprimitive"));

      ConcreteModel2 model2 = new ConcreteModel2();

      model2.string = "string";
      model2.intWrapped = 100;
      model2.intPrimitive = 200;
      model2.doubleWrapped = 2.123;
      model2.doublePrimitive = 3.123;
      model2.model = model1;
      model2.insert();

      Row row2 = db.table(model2.table()).first();
      assertEquals("string", row2.get("string"));
      assertTrue(100 == (Integer) row2.get("intwrapped"));
      assertTrue(200 == (int) row2.get("intprimitive"));
      assertTrue(2.123 == (Double) row2.get("doublewrapped"));
      assertTrue(3.123 == (double) row2.get("doubleprimitive"));
      assertTrue((int) model1.id() == (int) row2.get("model"));
    }
  }

  /**
   * Test model updating.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testUpdate() throws SQLException {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      ConcreteModel3 model = new ConcreteModel3();
      model.field = "test";
      assertFalse(model.update());
      model.insert();

      model.field = "tset";
      assertTrue(model.update());

      Row row = db.table(model.table()).first();
      assertEquals("tset", row.get("field"));
    }
  }

  /**
   * Test model upserting (insert/update).
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testUpsert() throws SQLException {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      ConcreteModel3 model = new ConcreteModel3();
      model.field = "test";
      model.upsert();

      Row row1 = db.table(model.table()).first();
      assertEquals("test", row1.get("field"));

      model.field = "tset";
      model.upsert();

      Row row2 = db.table(model.table()).first();
      assertEquals("tset", row2.get("field"));
      assertEquals(model.id(), row2.get("id"));
    }
  }

  /**
   * Test model deletion.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testDelete() throws SQLException {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      ConcreteModel3 model = new ConcreteModel3();
      model.field = "test";
      assertFalse(model.delete());
      model.insert();

      assertTrue(model.delete());
      assertFalse(model.delete());

      Row row = db.table(model.table()).first();
      assertEquals(null, row);
    }
  }

  /**
   * Test model querying.
   *
   * @throws SQLException In case of a SQL error.
   */
  @Test
  public void testModelQuerying() throws SQLException {
    for (Database db: this.databases) {
      // Set the database being tested.
      ModelTest.db = db;

      ConcreteModel4 model1 = new ConcreteModel4();
      model1.field = "Model1";
      model1.insert();

      ConcreteModel5 model2 = new ConcreteModel5();
      model2.field = "Model2";
      model2.model = model1;
      model2.insert();

      ConcreteModel5 model3 = new ConcreteModel5();
      model3.field = "Model3";
      model3.model = model1;
      model3.insert();

      List<ConcreteModel4> models = Model.findAll(ConcreteModel4.class);

      assertEquals(1, models.size());

      ConcreteModel4 model4 = models.get(0);
      assertEquals("Model1", model4.field);
      assertEquals(2, model4.models.size());

      ConcreteModel5 model5 = model4.models.get(0);
      assertEquals("Model2", model5.field);
      assertEquals(model4, model5.model);

      ConcreteModel5 model6 = model4.models.get(1);
      assertEquals("Model3", model6.field);
      assertEquals(model4, model6.model);
    }
  }
}
