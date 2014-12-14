/**
 * Copyright (C) 2014 Kasper Kronborg Isager and Sigrið Gyldenkærne Dalsgarð.
 */
package dk.itu.bookie;

// JavaFX utilities
import java.util.List;

// Reflection utilities
import java.lang.reflect.Field;

// JavaFX collections
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// JavaFX properties
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

// Donkey utilities
import dk.itu.donkey.Database;
import dk.itu.donkey.Model;

/**
 * Fxmodel class.
 *
 * @version 1.0.0
 */
public class FXModel extends Model {
  /**
   * Initialize an FX Model.
   *
   * @param table The name of the table to use for the model.
   * @param db    The database to use for persisting the model.
   */
  public FXModel(final String table, final Database db) {
    super(table, db);
  }

  /**
   * Given a field, get its type.
   *
   * @param field The field whose type to get.
   * @return      The type of the field.
   */
  @Override
  protected Class<?> getFieldType(final Field field) {
    Class<?> fieldType = field.getType();

    if (StringProperty.class.isAssignableFrom(fieldType)) {
      return String.class;
    }
    else if (IntegerProperty.class.isAssignableFrom(fieldType)) {
      return Integer.class;
    }
    else if (DoubleProperty.class.isAssignableFrom(fieldType)) {
      return Double.class;
    }
    else if (FloatProperty.class.isAssignableFrom(fieldType)) {
      return Float.class;
    }
    else if (LongProperty.class.isAssignableFrom(fieldType)) {
      return Long.class;
    }
    else if (BooleanProperty.class.isAssignableFrom(fieldType)) {
      return Boolean.class;
    }
    else if (ObjectProperty.class.isAssignableFrom(fieldType)) {
      return this.getGenericType(field);
    }
    else {
      return fieldType;
    }
  }

  /**
   * Parse an incoming field value.
   *
   * @param field The field whose value to parse.
   * @param value The value to parse.
   * @return      The parsed value.
   */
  @Override
  protected Object parseIncomingFieldValue(
    final Field field,
    final Object value
  ) {
    Class<?> fieldType = field.getType();

    if (StringProperty.class.isAssignableFrom(fieldType)) {
      StringProperty stringProperty = new SimpleStringProperty();
      stringProperty.setValue((String) value);
      return stringProperty;
    }
    else if (IntegerProperty.class.isAssignableFrom(fieldType)) {
      IntegerProperty integerProperty = new SimpleIntegerProperty();
      integerProperty.setValue((Integer) value);
      return integerProperty;
    }
    else if (DoubleProperty.class.isAssignableFrom(fieldType)) {
      DoubleProperty doubleProperty = new SimpleDoubleProperty();
      doubleProperty.setValue((Double) value);
      return doubleProperty;
    }
    else if (FloatProperty.class.isAssignableFrom(fieldType)) {
      FloatProperty floatProperty = new SimpleFloatProperty();
      floatProperty.setValue((Float) value);
      return floatProperty;
    }
    else if (LongProperty.class.isAssignableFrom(fieldType)) {
      LongProperty longProperty = new SimpleLongProperty();
      longProperty.setValue((Long) value);
      return longProperty;
    }
    else if (BooleanProperty.class.isAssignableFrom(fieldType)) {
      BooleanProperty booleanProperty = new SimpleBooleanProperty();
      booleanProperty.setValue((Boolean) value);
      return booleanProperty;
    }
    else if (ObjectProperty.class.isAssignableFrom(fieldType)) {
      ObjectProperty objectProperty = new SimpleObjectProperty();
      objectProperty.setValue(value);
      return objectProperty;
    }
    else if (ObservableList.class.isAssignableFrom(fieldType)) {
      return FXCollections.observableArrayList((List) value);
    }
    else {
      return value;
    }
  }

  /**
   * Parse an outgoing field value.
   *
   * @param field The field whose value to parse.
   * @param value The value to parse.
   * @return      The parsed value.
   */
  @Override
  protected Object parseOutgoingFieldValue(
    final Field field,
    final Object value
  ) {
    Class<?> fieldType = field.getType();

    if (Property.class.isAssignableFrom(fieldType)) {
      return ((Property) value).getValue();
    }
    else {
      return value;
    }
  }
}
