package dk.itu.donkey.fixture;

import dk.itu.donkey.Model;
import dk.itu.donkey.ModelTest;
import dk.itu.donkey.Row;

/**
 * Concrete model class for testing.
 */
public class ConcreteModel1 extends Model {
  /**
   * Float field (wrapped).
   */
  public Float floatWrapped;

  /**
   * Float field (primitive).
   */
  public float floatPrimitive;

  /**
   * Long field (wrapped).
   */
  public Long longWrapped;

  /**
   * Long field (primitive).
   */
  public long longPrimitive;

  /**
   * Boolean field (wrapped).
   */
  public Boolean booleanWrapped;

  /**
   * Boolean field (primitive).
   */
  public boolean booleanPrimitive;

  /**
   * Initialize a model.
   */
  public ConcreteModel1() {
    super("test2", ModelTest.db());
  }

  /**
   * Initialize a model from a database row.
   *
   * @param row The database row to initialize the model from.
   */
  public ConcreteModel1(final Row row) {
    super("test2", ModelTest.db(), row);
  }
}
