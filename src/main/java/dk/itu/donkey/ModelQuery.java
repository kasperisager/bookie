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
   * The table to query.
   */
  private String table;

  /**
   * The query to build and perform.
   */
  private Query query;

  /**
   * Keep track of tables that have been joined into the query.
   */
  private Set<String> tables = new HashSet<>();

  /**
   * Initialize a model query.
   *
   * @param type The model subclass to query.
   */
  public ModelQuery(final Class<T> type) {
    Model model = Model.instantiate(type);

    this.type = type;
    this.query = model.query();
    this.table = model.table();
  }

  /**
   * Prefix a column with the table name of the model being queried if needed.
   *
   * <p>
   * If the column has already been prefixed with another table name, the
   * column will simply pass through without being touched.
   *
   * @param column  The column to prefix.
   * @return        The prefixed column.
   */
  private String prefixColumn(final String column) {
    if (!column.matches(".*\\..*")) {
      return String.format("%s.%s", this.table, column);
    }
    else {
      return column;
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
    this.query.where(this.prefixColumn(column), operator, value);

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
    this.query.where(this.prefixColumn(column), value);

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
    this.query.orWhere(this.prefixColumn(column), operator, value);

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
    this.query.orWhere(this.prefixColumn(column), value);

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
    this.query.orderBy(this.prefixColumn(column), direction);

    return this;
  }

  /**
   * Add an order by clause to the query.
   *
   * @param column  The column to order by.
   * @return        The current {@link ModelQuery} object, for chaining.
   */
  public ModelQuery orderBy(final String column) {
    this.query.orderBy(this.prefixColumn(column));

    return this;
  }

  /**
   * Recursively traverse a model and join in its relations on the current
   * query object.
   *
   * @param type The model type to traverse.
   */
  private void getRelations(final Class type) {
    Model outer = Model.instantiate(type);

    // Remember that this model has already been added as a relation.
    this.tables.add(outer.table());

    // Select the ID column of the model in the format "table_id".
    this.query.select(String.format("%s.id as %1$s_id", outer.table()));

    for (Field field: outer.getFields()) {
      String fieldName = field.getName();
      Class fieldType = field.getType();

      boolean isList = false;

      // If the field being looked at is a list, get the generic type of the
      // list.
      if (List.class.isAssignableFrom(fieldType)) {
        fieldType = Model.getGenericType(field);

        // Remember that the field type was a list.
        isList = true;
      }

      if (Model.class.isAssignableFrom(fieldType)) {
        Model inner = Model.instantiate(fieldType);

        // If the model hasn't already been added as a relation, join it into
        // the query if it represents a single field, e.g. a comment belonging
        // to a post, and look for further relations...
        if (!this.tables.contains(inner.table())) {
          if (!isList) {
            this.query.join(
              inner.table(),
              String.format("%s.%s", outer.table(), fieldName),
              String.format("%s.%s", inner.table(), "id")
            );

            //select ... from showtimes join movies on showtimes.movie = movies.id

            // Remember that this table has already been added as a relation.
            this.tables.add(inner.table());
          }

          // Look for further relations.
          this.getRelations(fieldType);
        }
        // ...otherwise, assume that the model is a relation of an already
        // joined model. This will be the case in a two-way relation (either
        // One-to-One or One-to-Many) and so a reverse join is performed if the
        // field isn't a list, e.g. joining a single post with a list of
        // comments.
        else if (!isList) {
          this.query.join(
            outer.table(),
            String.format("%s.%s", inner.table(), "id"),
            String.format("%s.%s", outer.table(), fieldName)
          );

          //select ... from showtimes join tickets on showtimes.id = tickets.showtime
        }
      }
      else {
        // Prefix all the columns of the model with its table name to ensure
        // that non-unique columns can be differentiated if other data is
        // joined in. I.e. people.name becomes people_name.
        this.query.select(String.format(
          "%s.%s as %1$s_%2$s", outer.table(), fieldName.toLowerCase()
        ));
      }
    }
  }

  /**
   * Recursively traverse a model and initialize its relations based on a
   * database response.
   *
   * @param context The context of the relation.
   * @param type    The type of model to traverse.
   * @param rows    The database rows to use for initializing the models.
   * @return        A list of models initialized with their relations.
   */
  private List<Model> setRelations(
    final Class context,
    final Class type,
    final List<Row> rows
  ) {
    // Create a map for tracking model instances by their ID. When joining data,
    // the same instance of a model might appear several times in the query
    // response (e.g. the same post for several comments). The map will ensure
    // that only the first occurence of each unique model is instantiated.
    Map<Integer, Model> models = new LinkedHashMap<>();

    // Map model IDs to their associated rows. E.g. a list of posts joined with
    // their comments would result in a map of post IDs mapped to the database
    // rows containing the comments associated with that post ID.
    Map<Integer, List<Row>> modelRows = new LinkedHashMap<>();

    // Partition the rows according to the specified type.
    for (Row row: rows) {
      Model model = Model.instantiate(type);

      // Grab the ID of the current model table from the row.
      Integer id = (Integer) row.get(
        String.format("%s_%s", model.table(), "id")
      );

      if (id == null) {
        continue;
      }

      // List of database rows associated with a given model.
      List<Row> subRows;

      if (!models.containsKey(id)) {
        subRows = new ArrayList<>();
        subRows.add(row);

        models.put(id, model);

        // Set the current row on the model. Since each column in the response
        // is prefixed with the table name of the model, only columns specific
        // to the model will be set on it.
        model.setRow(row);
      }
      else {
        subRows = modelRows.get(id);
        subRows.add(row);
      }

      modelRows.put(id, subRows);
    }

    for (Model model: models.values()) {
      // Run through each of the fields of the model and look for further
      // relations.
      for (Field field: model.getFields()) {
        String fieldName = field.getName();
        Class fieldType = field.getType();

        boolean isList = false;

        // If the field being looked at is a list, get the generic type of the
        // list.
        if (List.class.isAssignableFrom(fieldType)) {
          fieldType = Model.getGenericType(field);

          // Remember that the field type was a list.
          isList = true;
        }

        if (Model.class.isAssignableFrom(fieldType)) {
          // If the field is of the same type as the context, bail out. This is
          // to avoid an infinite loop where two models both have fields of
          // oneanother's type, e.g. a post with a list of comments and a
          // comment that belongs to a post.
          if (fieldType == context) {
            continue;
          }

          List<Model> value = this.setRelations(
            type, fieldType, modelRows.get(model.id())
          );

          if (isList) {
            model.setField(fieldName, value);
          }
          else {
            model.setField(fieldName, value.get(0));
          }
        }
      }
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

    return this.setRelations(null, this.type, this.query.get());
  }
}
