/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Reflection utilities
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

// SQL utilities
import java.sql.SQLException;

/**
 * The Model Query class is used for querying database rows related to models
 * and, in contrast to a raw {@link Query}, returns {@link Model}s rather than
 * {@link Row}s.
 *
 * @param <T> The type of model to query.
 *
 * @since 1.0.0 Initial release.
 */
public final class ModelQuery<T extends Model> {
  /**
   * The model to query.
   */
  private Class<T> type;

  /**
   * The query to build and perform.
   */
  private Query query;

  private Set<String> tables = new HashSet<>();

  /**
   * Initialize a model query.
   *
   * @param type The model subclass to query.
   */
  public ModelQuery(final Class<T> type) {
    this.type = type;
    this.query = Model.instantiate(type).query();
  }

  /**
   * Add a where clause to the query.
   *
   * @param column    The column to compare.
   * @param operator  The logical operator to use for the comparison.
   * @param value     The value to compare against.
   * @return          The current {@link ModelQuery} object, for chaining.
   */
  public ModelQuery where(
    final String column,
    final String operator,
    final Object value
  ) {
    this.query.where(column, operator, value);

    return this;
  }

  /**
   * Add a where clause to the query.
   *
   * @param column  The column to compare.
   * @param value   The value to compare against.
   * @return        The current {@link ModelQuery} object, for chaining.
   */
  public ModelQuery where(final String column, final Object value) {
    this.query.where(column, value);

    return this;
  }

  /**
   * Add a or where clause to the query.
   *
   * @param column    The column to compare.
   * @param operator  The logical operator to use for the comparison.
   * @param value     The value to compare against.
   * @return          The current {@link ModelQuery} object, for chaining.
   */
  public ModelQuery orWhere(
    final String column,
    final String operator,
    final Object value
  ) {
    this.query.orWhere(column, operator, value);

    return this;
  }

  /**
   * Add a or where clause to the query.
   *
   * @param column  The column to compare.
   * @param value   The value to compare against.
   * @return        The current {@link ModelQuery} object, for chaining.
   */
  public ModelQuery orWhere(final String column, final Object value) {
    this.query.orWhere(column, value);

    return this;
  }

  /**
   * Add an order by clause to the query.
   *
   * @param column    The column to order by.
   * @param direction The ordering direction. Either "asc" or "desc".
   * @return          The current {@link ModelQuery} object, for chaining.
   */
  public ModelQuery orderBy(final String column, final String direction) {
    this.query.orderBy(column, direction);

    return this;
  }

  /**
   * Add an order by clause to the query.
   *
   * @param column  The column to order by.
   * @return        The current {@link ModelQuery} object, for chaining.
   */
  public ModelQuery orderBy(final String column) {
    this.query.orderBy(column);

    return this;
  }

  /**
   * Add a limit clause to the query.
   *
   * @param limit The limit to add.
   * @return      The current {@link ModelQuery} object, for chaining.
   */
  public ModelQuery limit(final int limit) {
    this.query.limit(limit);

    return this;
  }

  /**
   * Add an offset clause to the query.
   *
   * @param offset  The offset to add.
   * @return        The current {@link ModelQuery} object, for chaining.
   */
  public ModelQuery offset(final int offset) {
    this.query.offset(offset);

    return this;
  }

  /**
   * Count the number of models.
   *
   * @return The number of models.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Number count() throws SQLException {
    return this.query.count();
  }

  /**
   * Get the largest value of a model field.
   *
   * @param field The field to find the largest value of.
   * @return      The largest value of the specified field.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Object max(final String field) throws SQLException {
    return this.query.max(field);
  }

  /**
   * Get the smallest value of a model field.
   *
   * @param field The field to find the smallet value of.
   * @return      The smallest value of the specified field.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Object min(final String field) throws SQLException {
    return this.query.min(field);
  }

  /**
   * Get the average value of a model field.
   *
   * @param field The field to find the average value of.
   * @return      The average value of the specified field.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Number avg(final String field) throws SQLException {
    return this.query.avg(field);
  }

  /**
   * Get the sum of a model field.
   *
   * @param field The field to find the sum of.
   * @return      The sum of the specified field.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Number sum(final String field) throws SQLException {
    return this.query.sum(field);
  }

  private Class getGenericType(final Field field) {
    ParameterizedType type = (ParameterizedType) field.getGenericType();

    return (Class) type.getActualTypeArguments()[0];
  }

  private void getRelations(final Class modelType) {
    Model outer = Model.instantiate(modelType);

    this.tables.add(outer.table());

    // Select the ID column of the model in the format "table_id".
    this.query.select(String.format("%s.id as %1$s_id", outer.table()));

    for (Field field: outer.getFields()) {
      String fieldName = field.getName();
      Class fieldType = field.getType();

      if (Model.class.isAssignableFrom(fieldType)) {
        Model inner = Model.instantiate(fieldType);

        if (!this.tables.contains(inner.table())) {
          this.query.join(
            inner.table(),
            String.format("%s.%s", outer.table(), fieldName),
            String.format("%s.%s", inner.table(), "id")
          );

          this.tables.add(inner.table());

          this.getRelations(fieldType);
        }
        else {
          this.query.join(
            outer.table(),
            String.format("%s.%s", inner.table(), "id"),
            String.format("%s.%s", outer.table(), fieldName)
          );
        }
      }

      if (List.class.isAssignableFrom(fieldType)) {
        fieldType = this.getGenericType(field);

        if (fieldType == this.type) {
          continue;
        }

        if (Model.class.isAssignableFrom(fieldType)) {
          this.getRelations(fieldType);

          continue;
        }
      }

      this.query.select(String.format(
        "%s.%s as %1$s_%2$s", outer.table(), fieldName.toLowerCase()
      ));
    }
  }

  private List<Model> setRelations(
    final Class modelType,
    final List<Row> rows
  ) {
    // Create a map for tracking model instances by their ID. When joining data,
    // the same instance of a model might appear several times in the query
    // response (e.g. the same post for several comments). The map will ensure
    // that only the first occurence of each unique model is instantiated.
    Map<Integer, Model> models = new LinkedHashMap<>();

    Model outer = Model.instantiate(modelType);

    for (Row row: rows) {
      // Grab the ID of the current model table from the row.
      int id = (int) row.get(String.format("%s_%s", outer.table(), "id"));

      if (models.containsKey(id)) {
        continue;
      }

      Model inner = Model.instantiate(modelType);

      // Set the current row on the model. Since each column in the response is
      // prefixed with the table name of the model, only columns specific to the
      // model will be set on it.
      inner.setRow(row);

      // Run through each of the fields of the model and look for further
      // relations.
      for (Field field: inner.getFields()) {
        Class fieldType = field.getType();

        boolean isList = false;

        // If the field being looked at is a list, get the generic type of the
        // list.
        if (List.class.isAssignableFrom(fieldType)) {
          fieldType = this.getGenericType(field);

          // Remember that the field type was a list.
          isList = true;
        }

        // If the field being looked at is the same type as the model being
        // queried, bail out. This is to avoid an infinite loop where two models
        // both have fields of oneanother's type, e.g. a post with a list of
        // comments and a comment that belongs to a post.
        if (fieldType == this.type) {
          continue;
        }

        if (Model.class.isAssignableFrom(fieldType)) {
          List<Model> value = this.setRelations(fieldType, rows);

          if (isList) {
            inner.setField(field.getName(), value);
          }
          else {
            inner.setField(field.getName(), value.get(0));
          }
        }
      }

      // Store the model in the map.
      models.put(id, inner);
    }

    return new ArrayList<Model>(models.values());
  }

  /**
   * Perform the query and return a list of matching models.
   *
   * @return A list of models.
   *
   * @throws SQLException In case of a SQL error.
   */
  public List<Model> get() throws SQLException {
    this.getRelations(this.type);

    return this.setRelations(this.type, this.query.get());
  }

  /**
   * Perform the query and get the first matching result.
   *
   * @return The first model that matches the query.
   *
   * @throws SQLException In case of a SQL error.
   */
  public Model first() throws SQLException {
    Row row = this.query.first();

    if (row == null) {
      return null;
    }

    Model model = Model.instantiate(this.type);
    model.setRow(row);

    return model;
  }
}
