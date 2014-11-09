package mongodbsql;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Michael Domingues <dominguesjust@gmail.com>
 *
 * Created on 6/nov/2014, 23:18:15
 */
public class Command {

    private String tableName = "";
    private ArrayList<String> columns = null;
    private HashMap<String, Condition> columnsConditions = null;
    private String type = "";
    private boolean whereClause;
    private boolean andClause;
    private boolean orClause;
    private boolean inClause;

    public Command() {
        this.columns = new ArrayList<>();
        this.columnsConditions = new HashMap<>();
    }

    /**
     * Sets table name for the target of Select.
     *
     * @param tableName table name.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Sets each column condition for the query. E.g.: ... name = 'John'
     *
     * @param column column name
     * @param cond restriction to be applied based on the operator.
     */
    public void setColumnsConditions(String column, Condition cond) {
        this.columnsConditions.put(column, cond);
    }

    /**
     * Sets columns to be presented on the query result.
     *
     * @param column column of the table with tableName.
     */
    public void setColumns(String column) {
        this.columns.add(column);
    }

    /**
     * Type of query. E.g.: Select or Update or ...
     *
     * @param type type of query
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * If it has a where clause.
     *
     * @param whereClause <li> true, it with where clause
     * <li> false, if not.
     */
    public void setWhereClause(boolean whereClause) {
        this.whereClause = whereClause;
    }

    /**
     * If it has a and clause.
     *
     * @param andClause <li> true, if with and clause
     * <li> false, if not.
     */
    public void setAndClause(boolean andClause) {
        this.andClause = andClause;
    }

    /**
     * If it has a or clause.
     *
     * @param orClause <li> true, if with or clause
     * <li> false, if not.
     */
    public void setOrClause(boolean orClause) {
        this.orClause = orClause;
    }

    /**
     * If it has a in clause.
     *
     * @param inClause <li> true, if with in clause
     * <li> false, if not.
     */
    public void setInClause(boolean inClause) {
        this.inClause = inClause;
    }

    /**
     * Gets table name for SQL query builder.
     *
     * @return table name.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Gets columns for target of the query.
     *
     * @return list of columns to be presented on the result.
     */
    public ArrayList<String> getColumns() {
        return columns;
    }

    /**
     * Gets conditions per each column
     *
     * @return column-restriction map based result.
     */
    public HashMap<String, Condition> getColumnsConditions() {
        return columnsConditions;
    }

    /**
     * Get type of query.
     *
     * @return type of query
     */
    public String getType() {
        return type;
    }

    /**
     * Return if has a where clause
     *
     * @return <li> true, if with where clause
     * <li> false, if not
     */
    public boolean isWhereClause() {
        return whereClause;
    }

    /**
     * Return if has a and clause
     *
     * @return <li> true, if with and clause
     * <li> false, if not
     */
    public boolean isAndClause() {
        return andClause;
    }

    /**
     * Return if has a or clause
     *
     * @return <li> true, if with or clause
     * <li> false, if not
     */
    public boolean isOrClause() {
        return orClause;
    }

    /**
     * Return if has a in clause
     *
     * @return <li> true, if with in clause
     * <li> false, if not
     */
    public boolean isInClause() {
        return inClause;
    }
}

/**
 * Condition applied foreach column on a given query.
 *
 * @author Michael Domingues <dominguesjust@gmail.com>
 *
 *  * Created on 6/nov/2014, 00:05:12
 */
class Condition {

    private String value;
    private boolean hasRestriction;
    private String restriction;

    public Condition(String restr, String val) {
        this.value = val;
        this.restriction = restr;
        this.hasRestriction = true;

        if (restr == null) {
            this.hasRestriction = false;
        }
    }

    /**
     * Gets value of the given operator. E.g: >= 20 (20)
     *
     * @return value of the operator.
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets restriction of the given operator E.g: >= 20 (>=)
     *
     * @return restriction of the operator.
     */
    public String getRestriction() {
        return restriction;
    }

    /**
     * Returnif it has a restriction.
     *
     * @return <li> true, if with a restriction
     * <li> false, if not
     */
    public boolean hasRestriction() {
        return hasRestriction;
    }
}
