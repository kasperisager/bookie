package dk.itu.donkey.fixture;

import dk.itu.donkey.Model;
import dk.itu.donkey.ModelTest;
import dk.itu.donkey.Row;

/**
 * Concrete model class for testing.
 */
public class ConcreteModel3 extends Model {
  /**
   * String field.
   */
  public String field;

  /**
   * Initialize a model.
   */
  public ConcreteModel3() {
    super("test1", ModelTest.db());
  }

  /**
   * Initialize a model from a database row.
   *
   * @param row The database row to initialize the model from.
   */
  public ConcreteModel3(final Row row) {
    super("test1", ModelTest.db(), row);
  }
}

