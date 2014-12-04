/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.List;

// Reflection utilities
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

// SQL utilities
import java.sql.SQLException;

/**
 * The Model class is an object-relational mapper that enables seamless and easy
 * persistance of data-models to any of the supported database systems.
 *
 * <p>
 * It enables automatic database schema definition in subclasses by convention
 * of public fields. A CRUD interface is provided out-of-the-box so simple
 * data-models needn't define anything but their own fields in order to benefit
 * from persistance to a database.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Object-relational_mapping">
 *      Wikipedia - Object-relational mapping</a>
 *
 * @since 1.0.0 Initial release.
 */
public abstract class Model {
  /**
   * The name of the table to use for the model.
   */
  private String table;

  /**
   * The database to use for persisting the model.
   */
  private Database db;

  /**
   * The ID of the model.
   *
   * The ID acts as the primary key for all models.
   */
  private Integer id;

  /**
   * Initialize a model.
   */
  public Model() {
    super();
  }

  /**
   * Initialize a model.
   *
   * @param table The name of the table to use for the model.
   * @param db    The database to use for persisting the model.
   */
  public Model(final String table, final Database db) {
    this();
    this.table = table.toLowerCase().trim().replaceAll(" +", "_");
    this.db = db;
  }

  /**
   * Get the generic type of a field.
   *
   * @param field The field to inspect.
   * @return      The generic type of the field.
   */
  public static final Class getGenericType(final Field field) {
    ParameterizedType type = (ParameterizedType) field.getGenericType();

    return (Class) type.getActualTypeArguments()[0];
  }

  /**
   * Get a public field from the model.
   *
   * @param label The name of the field.
   * @return      The field.
   */
  public final Field getField(final String label) {
    try {
      return this.getClass().getField(label);
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * Get all public fields (direct and inherited) declared in the model.
   *
   * @return An array of public fields declared in the model.
   */
  public final Field[] getFields() {
    return this.getClass().getFields();
  }

  /**
   * Set the value of a public field in the model.
   *
   * @param label The name of the field.
   * @param value The value of the field.
   */
  public final void setField(final String label, final Object value) {
    try {
      this.getField(label).set(this, value);
    }
    catch (Exception e) {
      return;
    }
  }

  /**
   * Get the table name of a model.
   *
   * @return The table name of the model.
   */
  public final String table() {
    return this.table;
  }

  /**
   * Get the ID of a model.
   *
   * @return The ID of the model.
   */
  public final Integer id() {
    return this.id;
  }

  /**
   * Set the ID of a model.
   *
   * This is only called when a model is queried from or inserted into a
   * database in which case it's necessary to initialize an empty model from
   * a table row or set the ID of the newly inserted model.
   *
   * @param id The ID to set.
   */
  private void id(final Integer id) {
    this.id = id;
  }

  /**
   * Define the database schema of a model.
   *
   * @throws SQLException In case of a SQL error.
   */
  private void defineSchema() throws SQLException {
    Schema schema = this.db.schema();
    schema.create(this.table);

    // All models must have an ID.
    schema.increments("id");

    for (Field field: this.getFields()) {
      String name = field.getName();
      Class type = field.getType();
      String column = name.toLowerCase();

      // String type
      if (type == String.class) {
        schema.text(column);
      }
      // Integer type (wrapped + primitive)
      else if (type == Integer.class || type == int.class) {
        schema.integer(column);
      }
      // Double type (wrapped + primitive)
      else if (type == Double.class || type == double.class) {
        schema.doublePrecision(column);
      }
      // Float type (wrapped + primitive)
      else if (type == Float.class || type == float.class) {
        schema.floatingPoint(column);
      }
      // Long type (wrapped + primitive)
      else if (type == Long.class || type == long.class) {
        schema.longInteger(column);
      }
      // Boolean type (wrapped + primitive)
      else if (type == Boolean.class || type == boolean.class) {
        schema.bool(column);
      }
      // Model subclass
      else if (Model.class.isAssignableFrom(type)) {
        Model model = this.instantiate(type);

        schema.integer(column);
        schema.foreignKey(column, model.table(), "id");
      }
      // List subclass
      else if (List.class.isAssignableFrom(type)) {
        Class genericType = this.getGenericType(field);

        if (Model.class.isAssignableFrom(genericType)) {
          continue;
        }

        throw new IllegalArgumentException(
          "Lists can only hold other Models"
        );
      }
      else {
        throw new IllegalArgumentException(
          "Unsupported data type for column: " + name
        );
      }
    }

    schema.run();
  }

  /**
   * Given a type, attempt instantiating a new model.
   *
   * @param type  The type of model to instantiate.
   * @return      The instantiated model.
   */
  public static final Model instantiate(final Class type) {
    try {
      return (Model) type.newInstance();
    }
    catch (ClassCastException e) {
      // Catch casting exceptions since this indicates that a wrong type was
      // passed to the method. Throw an illegal argument exception instead.
      throw new IllegalArgumentException("Type must be subclass of Model");
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * Get the Row representation of the model.
   *
   * @return The row representation of the model.
   */
  public final Row getRow() {
    Row row = new Row();

    for (Field field: this.getFields()) {
      String name = field.getName();
      Class type = field.getType();

      Object value = null;

      try {
        value = field.get(this);
      }
      catch (Exception e) {
        continue;
      }

      if (Model.class.isAssignableFrom(type)) {
        value = ((Model) value).id();
      }

      if (List.class.isAssignableFrom(type)) {
        continue;
      }

      row.put(name.toLowerCase(), value);
    }

    return row;
  }

  /**
   * Apply a Row representation to an empty model.
   *
   * @param row The Row representation to apply to the model.
   */
  public final void setRow(final Row row) {
    if (row == null || this.id != null) {
      return;
    }

    Integer id = (Integer) row.get("id");

    if (id == null) {
      id = (Integer) row.get(String.format("%s_id", this.table()));
    }

    if (id != null && id > 0) {
      this.id(id);
    }

    for (Field field: this.getFields()) {
      String name = field.getName();
      String column = name.toLowerCase();
      Object value = row.get(column);

      if (value == null) {
        value = row.get(String.format(
          "%s_%s", this.table(), column
        ));
      }

      this.setField(name, value);
    }
  }

  /**
   * Perform a query against the database table of a model.
   *
   * @return A {@link Query} object initialized to the table of the model.
   */
  public final Query query() {
    return this.db.table(this.table);
  }

  /**
   * Initialize a new model query.
   *
   * @param type  The type of model to find.
   * @param <T>   The type of model to find.
   * @return      A model query initialized to the type.
   */
  public static final <T extends Model> ModelQuery<T> find(
    final Class<T> type
  ) {
    return new ModelQuery<T>(type);
  }

  /**
   * Find a single model by its ID.
   *
   * @param type  The type of model to find.
   * @param <T>   The type of model to find.
   * @param id    The ID of the model.
   * @return      The model if found, otherwise null.
   *
   * @throws SQLException In case of a SQL error.
   */
  public static final <T extends Model> Model find(
    final Class<T> type,
    final int id
  ) throws SQLException {
    List<Model> models = Model.find(type).where("id", id).get();

    if (!models.isEmpty()) {
      return models.get(0);
    }
    else {
      return null;
    }
  }

  /**
   * Find all models of a given type.
   *
   * @param type  The type of models to find.
   * @param <T>   The type of models to find.
   * @return      A list of models.
   *
   * @throws SQLException In case of a SQL error.
   */
  public static final <T extends Model> List<T> findAll(
    final Class<T> type
  ) throws SQLException {
    return Model.find(type).get();
  }

  /**
   * Insert the model into the database.
   *
   * @return Boolean indicating whether or not the query was performed.
   *
   * @throws SQLException In case of a SQL error.
   */
  public final boolean insert() throws SQLException {
    if (this.id != null) {
      return false;
    }

    this.defineSchema();

    List<Row> rows = this.query().insert(this.getRow());

    if (rows != null && !rows.isEmpty()) {
      Number id = (Number) rows.get(0).get(
        this.db.grammar().generatedAutoIncrementRow()
      );

      this.id(id.intValue());
    }

    return true;
  }

  /**
   * Update the model in the database.
   *
   * @return Boolean indicating whether or not the query was performed.
   *
   * @throws SQLException In case of a SQL error.
   */
  public final boolean update() throws SQLException {
    if (this.id == null) {
      return false;
    }

    this.query().where("id", this.id).update(this.getRow());

    return true;
  }

  /**
   * Insert or update the model in the database.
   *
   * @return Boolean indicating whether or not the query was performed.
   *
   * @throws SQLException In case of a SQL error.
   */
  public final boolean upsert() throws SQLException {
    return (this.id == null) ? this.insert() : this.update();
  }

  /**
   * Delete the model from the database.
   *
   * @return Boolean indicating whether or not the query was performed.
   *
   * @throws SQLException In case of a SQL error.
   */
  public final boolean delete() throws SQLException {
    if (this.id == null) {
      return false;
    }

    this.query().where("id", this.id).delete();
    this.id(null);

    return true;
  }
}
