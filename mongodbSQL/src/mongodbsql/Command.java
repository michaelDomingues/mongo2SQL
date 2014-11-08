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
    private HashMap<String,Condition> columnsConditions = null;
    private String type = "";
    private boolean whereClause;
    private boolean andClause;
    private boolean orClause;
    private boolean inClause;
   
    public Command() {
        this.columns = new ArrayList<>();
        this.columnsConditions = new HashMap<>();
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumnsConditions(String column, Condition cond) {
        this.columnsConditions.put(column, cond);
    }

    public void setColumns(String column) {
        this.columns.add(column);
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public void setWhereClause(boolean whereClause) {
        this.whereClause = whereClause;
    }

    public void setAndClause(boolean andClause) {
        this.andClause = andClause;
    }

    public void setOrClause(boolean orClause) {
        this.orClause = orClause;
    }

    public void setInClause(boolean inClause) {
        this.inClause = inClause;
    }

    public String getTableName() {
        return tableName;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public HashMap<String, Condition> getColumnsConditions() {
        return columnsConditions;
    }

    public String getType() {
        return type;
    }

    public boolean isWhereClause() {
        return whereClause;
    }

    public boolean isAndClause() {
        return andClause;
    }

    public boolean isOrClause() {
        return orClause;
    }

    public boolean isInClause() {
        return inClause;
    }
    
}
class Condition {

    private String value;
    private boolean hasRestriction;
    private String restriction;

    public Condition(String restr, String val) {
        this.value = val;
        this.restriction = restr;
        this.hasRestriction = true;
        
        if(restr == null){
            this.hasRestriction = false;
        }
    }

    public String getValue() {
        return value;
    }

    public String getRestriction() {
        return restriction;
    }

    public boolean hasRestriction() {
        return hasRestriction;
    }
    
    
}
