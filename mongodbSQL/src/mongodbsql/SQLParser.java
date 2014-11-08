package mongodbsql;

import configuration.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Michael Domingues <dominguesjust@gmail.com>
 *
 * Created on 6/nov/2014, 22:21:34 
 */
public class SQLParser {

    private final String SELECT = "SELECT";
    private final String FROM = "FROM";
    private final String WHERE = "WHERE";
    private final String IN = "IN";
    private final String OR = "OR";
    private final String AND = "AND";
    private final String COMMA = ",";
    private final String EMPTY_SPACE = " ";
    private final String ALL = "*";
    
    public SQLParser() {
    }

    /**
     * Converts Mongo DB query to a SQL one.
     * @param cmd mongo DB command
     */
    void processMongoDBComand(Command cmd) {
        
        String sqlQuery = "";
        
        /**
         * Insert type of query
         */
        if(cmd.getType().equals(SELECT))
        {
            sqlQuery += (SELECT + EMPTY_SPACE);
        }
        
        /**
         * Insert columns to filter.
         */
        ArrayList<String> columns = cmd.getColumns();
        if(columns.isEmpty() == false) {
            
            int i = 1;
            for (String value : columns) {
                sqlQuery += value;
                if(i < columns.size()) {
                    sqlQuery += COMMA;
                } else {
                    sqlQuery += EMPTY_SPACE;
                }
                i++;
            }
        } else {
            /**
             * No columns to filter, so selects all columns.
             */
            sqlQuery += (ALL + EMPTY_SPACE);
        }
        
        /**
         * From which table and with Where clause.
         */
        sqlQuery += (FROM + EMPTY_SPACE + cmd.getTableName() + EMPTY_SPACE + WHERE + EMPTY_SPACE);
        
        /**
         * Parses type of operator
         */
        HashMap<String, Condition> columnsConditions = cmd.getColumnsConditions();
        Iterator it = columnsConditions.entrySet().iterator(); 
        int i = 1;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            Condition cond = (Condition) pairs.getValue();
            
            String commandValue = cond.getValue();
            
            /**
             * Converts IN braces to curved parentheses.
             */
            if(cond.getRestriction().contains(IN.toLowerCase())) {
                commandValue = commandValue.replaceAll("\\[", "\\(").replaceAll("\\]", "\\)");
            }
            sqlQuery += pairs.getKey() + EMPTY_SPACE + getQualifier(cond.getRestriction()) + EMPTY_SPACE + commandValue + EMPTY_SPACE;
            
            /**
             * Assesses AND and OR clause for aggregation.
             */
            if(i < columnsConditions.size()) {
                if(cmd.isAndClause()){
                    sqlQuery += AND+EMPTY_SPACE;
                }
                if(cmd.isOrClause()){
                    sqlQuery += OR+EMPTY_SPACE;
                }
            } // No need for else
                    
            i++;
            it.remove(); // avoids a ConcurrentModificationException
        }
        
        /**
         * Write SQL query to file.
         */
        writeSQLQuery(sqlQuery);
    }
    
    /**
     * Write translated sql query to output file.
     * @param text translated query.
     */
    private void writeSQLQuery(String text) {
        try (
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Configuration.SQL_DB_FILE,true)))) {
            out.println(text);
        } catch (IOException e) {
            System.err.println("Exception while writting to SQL file: "+e.getMessage());
        }
    }
    
    /**
     * Matches identifier for a qualifier to be used in the query.
     * @param tag identifier used in mongodb.
     * @return qualifier for sqldb
     */
    private String getQualifier(String tag) {
        switch (tag) {
            case "$or":
                return "OR";
            case "$and":
                return "AND";
            case ",":
                return "AND";
            case "$lt":
                return "<";
            case "$lte":
                return "<=";
            case "$gt":
                return ">";
            case "$gte":
                return ">=";
            case "$ne":
                return "!=";
            case "$in":
                return "IN";
            case "=":
                return "=";                
        }

        return null;
    }
}
