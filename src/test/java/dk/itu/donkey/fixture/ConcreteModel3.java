package dk.itu.donkey.fixture;

import dk.itu.donkey.Model;
import dk.itu.donkey.ModelTest;

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
}
