package dk.itu.donkey.fixture;

import dk.itu.donkey.Model;
import dk.itu.donkey.ModelTest;

/**
 * Concrete model class for testing.
 */
public class ConcreteModel5 extends Model {
  /**
   * String field.
   */
  public String field;

  /**
   * Model relation.
   */
  public ConcreteModel4 model;

  /**
   * Initialize a model.
   */
  public ConcreteModel5() {
    super("test1", ModelTest.db());
  }
}
