package dk.itu.donkey.fixture;

import dk.itu.donkey.Model;
import dk.itu.donkey.ModelTest;

/**
 * Concrete model class for testing.
 */
public class ConcreteModel2 extends Model {
  /**
   * String field.
   */
  public String string;

  /**
   * Integer field (wrapped).
   */
  public Integer intWrapped;

  /**
   * Integer field (primitive).
   */
  public int intPrimitive;

  /**
   * Double field (wrapped).
   */
  public Double doubleWrapped;

  /**
   * Double field (primitive).
   */
  public double doublePrimitive;

  /**
   * Model subclass field.
   */
  public ConcreteModel1 model;

  /**
   * Initialize a model.
   */
  public ConcreteModel2() {
    super("test1", ModelTest.db());
  }
}

