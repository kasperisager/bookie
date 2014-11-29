/**
 * Copyright (C) 2014 Kasper Kronborg Isager.
 */
package dk.itu.donkey;

// General utilities
import java.util.ArrayList;
import java.util.List;

// SQL utilities
import java.sql.SQLException;

/**
 * Model query class.
 *
 * @version 1.0.0
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

  /**
   * Initialize a model query.
   *
   * @param type The model subclass to query.
   */
  public ModelQuery(final Class<T> type) {
    this.type = type;

    try {
      this.query = ((T) type.newInstance()).query();
    }
    catch (ClassCastException e) {
      // Catch casting exceptions since this indicates that a wrong type was
      // passed to the method. Throw an illegal argument exception instead.
      throw new IllegalArgumentException("Type must be subclass of Model");
    }
    catch (Exception e) {
      return;
    }
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
   */
  public Number count() throws SQLException {
    return this.query.count();
  }

  /**
   * Get the largest value of a model field.
   *
   * @param field The field to find the largest value of.
   * @return      The largest value of the specified field.
   */
  public Object max(final String field) throws SQLException {
    return this.query.max(field);
  }

  /**
   * Get the smallest value of a model field.
   *
   * @param field The field to find the smallet value of.
   * @return      The smallest value of the specified field.
   */
  public Object min(final String field) throws SQLException {
    return this.query.min(field);
  }

  /**
   * Get the average value of a model field.
   *
   * @param field The field to find the average value of.
   * @return      The average value of the specified field.
   */
  public Number avg(final String field) throws SQLException {
    return this.query.avg(field);
  }

  /**
   * Get the sum of a model field.
   *
   * @param field The field to find the sum of.
   * @return      The sum of the specified field.
   */
  public Number sum(final String field) throws SQLException {
    return this.query.sum(field);
  }

  /**
   * Perform the query and return a list of matching models.
   *
   * @return A list of models.
   *
   * @throws SQLException In case of a SQL error.
   */
  public List<Model> get() throws SQLException {
    List<Model> models = new ArrayList<>();
    List<Row> rows = this.query.get();

    for (Row row: rows) {
      try {
	Model model = this.type.newInstance();
	model.setRow(row);
	models.add(model);
      }
      catch (Exception e) {
	continue;
      }
    }

    return models;
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

    try {
      Model model = this.type.newInstance();
      model.setRow(row);

      return model;
    }
    catch (Exception e) {
      return null;
    }
  }
}
