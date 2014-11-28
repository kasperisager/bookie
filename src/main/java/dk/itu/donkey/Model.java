/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.List;

// Reflection utilities
import java.lang.reflect.Field;

// SQL utilities
import java.sql.SQLException;

/**
 * Model class.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Object-relational_mapping">
 *      Wikipedia - Object-relational mapping</a>
 *
 * @version 1.0.0
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
    this.table = table.toLowerCase().trim().replaceAll(" +", "_");
    this.db = db;
  }

  /**
   * Initialize a model from a database row.
   *
   * @param table The name of the table to use for the model.
   * @param db    The database to use for persisting the model.
   * @param row   The database row to use for intializing the model.
   */
  public Model(final String table, final Database db, final Row row) {
    this(table, db);
    this.setRow(row);
  }

  /**
   * Get a public field from the model.
   *
   * @param label The name of the field.
   * @return      The field.
   */
  private Field getField(final String label) {
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
  private Field[] getFields() {
    return this.getClass().getFields();
  }

  /**
   * Set the value of a public field in the model.
   *
   * @param label The name of the field.
   * @param value The value of the field.
   */
  private void setField(final String label, final Object value) {
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
      Class<?> type = field.getType();
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
        try {
          Model model = (Model) type.newInstance();

          schema.integer(column);
          schema.foreignKey(column, model.table(), "id");
        }
        catch (Exception e) {
          continue;
        }
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
   * Get the Row representation of the model.
   *
   * @return The row representation of the model.
   */
  private Row getRow() {
    Row row = new Row();

    for (Field column: this.getFields()) {
      String name = column.getName();
      Object value = null;

      try {
        value = column.get(this);
      }
      catch (Exception e) {
        continue;
      }

      if (value instanceof Model) {
        value = ((Model) value).id();
      }

      if (value != null) {
        row.put(name.toLowerCase(), value);
      }
    }

    return row;
  }

  /**
   * Apply a Row representaion to an empty model.
   *
   * @param row The Row representaion to apply to the model.
   */
  private void setRow(final Row row) {
    if (row == null || this.id != null) {
      return;
    }

    Integer id = (Integer) row.get("id");

    if (id != null && id > 0) {
      this.id(id);
    }

    for (Field field: this.getFields()) {
      String column = field.getName();
      Object value = row.get(column.toLowerCase());

      if (value == null) {
        continue;
      }

      if (value instanceof Model) {
        this.setField(column, (Model) value);
      }
      else {
        this.setField(column, value);
      }
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

    if (!rows.isEmpty()) {
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
    if (this.id == null) {
      return this.insert();
    }
    else {
      return this.update();
    }
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
