package mongodbsql;

import java.util.Arrays;
import java.util.List;
import java.util.regex.*;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Michael Domingues <dominguesjust@gmail.com>
 *
 * Created on 6/nov/2014, 22:21:02
 */
public class ComandBuilder {

    private Command cmd = null;
    private final String RESTRICTION = "$";
    private boolean and = false;
    private boolean or = false;
    private boolean in = false;
    private boolean selectAll = true;
    private final String COLUMNS_IDENTIFIERS = ":1";
    private final String QUERY_RESTRICTION = ",";

    public ComandBuilder() {
    }

    public Command getCommand(String txt) {

        this.cmd = new Command();
        //Parse biggest part
        // String txt = "db.user.find({age: {$gte: 21}},{name: 1,_id: 1});";
        String arg_ments = null;

        String re1 = ".*?";	// Non-greedy match on filler
        String re2 = "(?:[a-z][a-z]+)";	// Uninteresting: word
        String re3 = ".*?";	// Non-greedy match on filler
        String re4 = "((?:[a-z][a-z]+))";	// Word 1
        String re5 = ".*?";	// Non-greedy match on filler
        String re6 = "((?:[a-z][a-z]+))";	// Word 2
        String re7 = "(\\(.*\\))";	// Round Braces 1

//        System.err.println("-------- Parsing line --------");
        Pattern p = Pattern.compile(re1 + re2 + re3 + re4 + re5 + re6 + re7, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(txt);
        if (m.find()) {
            String word1 = m.group(1);
            String word2 = m.group(2);
            String rbraces1 = m.group(3);
            arg_ments = rbraces1.toString();
            //Sets table name and type of query
            cmd.setTableName(word1);
            cmd.setType("SELECT");
//            System.err.println("-------- Parse done --------");
        }

        /**
         * Process arguments trimmed without spaces.
         */
        processArguments(arg_ments.replace(" ",""));

        return cmd;
    }

    /**
     * Parses each argument based on its restriction identifier.
     *
     * @param arg argument value.
     * @param restriction <li> true, with restriction identifier ($)
     * <li> false, without restriction identifier
     */
    private static void parseArgumentIntoCommand(String arg, boolean restriction, Command cmd) {
        /**
         * With $ identifier.
         */
        if (restriction) {
            String re1 = "((?:[a-z][a-z]+))";	// Word 
            String re2 = ".*?";	// Non-greedy match on filler
            String re3 = "(\\$)";	// Any Single Character 
            String re4 = "((?:[a-z][a-z]+))";	// Word 
            String re5 = ".*?";	// Non-greedy match on filler
            String re6 = getRegexFromOperator(StringUtils.substringAfterLast(arg, ":"));

            Pattern p = Pattern.compile(re1 + re2 + re3 + re4 + re5 + re6, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(arg);
            if (m.find()) {
                String word1 = m.group(1);
                String c1 = m.group(2);
                String word2 = m.group(3);
                String int1 = m.group(4);
     
                /**
                 * Set column condition.
                 */
                cmd.setColumnsConditions(word1.toString(), new Condition(c1.toString() + word2.toString(), int1.toString()));

//                System.out.print("\t\t\t(" + word1.toString() + ")" + "(" + c1.toString() + word2.toString() + ")" + "(" + int1.toString() + ")" + "\n");
            }
        } else {
            /**
             * Without $ identifier.
             */
            String re1 = "((?:[a-z][a-z]+))";	// Word 
            String re2 = ".*?";	// Non-greedy match on filler
            String re3 = getRegexFromOperator(StringUtils.substringAfterLast(arg, ":"));	// filter based on operator value

            Pattern p = Pattern.compile(re1 + re2 + re3, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(arg);
            if (m.find()) {
                String word1 = m.group(1);
                String int1 = m.group(2);

                cmd.setColumnsConditions(word1.toString(), new Condition("=", int1.toString()));

//                System.out.print("\t\t\t(" + word1.toString() + ")" + "(" + int1.toString() + ")" + "\n");
            }

        }
    }

    /**
     * Matches the operator type in the query against a regex.
     *
     * @param operatorValue operator value
     * @return regex to match the operator value
     */
    private static String getRegexFromOperator(String operatorValue) {
        final String BRACES = "[";
        final String WORD = "\"";
        final String QUOTATION_MARKS = "\'";

        try {
            //If is an Integer
            Integer.parseInt(operatorValue);
            return "(\\d+)";
        } catch (NumberFormatException e) {
            try {
                //If is a Float
                Float.parseFloat(operatorValue);
                return "([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";
            } catch (NumberFormatException ex) {
                //For $in operators
                if (operatorValue.contains(BRACES)) {
                    return "(\\[.*?\\])";
                } if(operatorValue.contains(WORD)) {
                    return "(\".*?\")";
                } if(operatorValue.contains(QUOTATION_MARKS)) {
                    return "(\\'.*?\\')";
                } else {
                    //Text by default
                    return "((?:[a-z][a-z]+))";
                }
            }
        }

    }

    /**
     * Process arguments from mongo query.
     * @param arg_ments arguments in brackets
     */
    private void processArguments(String arg_ments) {
        
//        System.err.println("-------- Parsing arguments --------");
        /**
         *
         * Detect big operators and clean string if they exist.
         */
        if (arg_ments.contains("and")) {
            and = true;
            arg_ments = StringUtils.substringBetween(arg_ments, "[", "]");
            cmd.setAndClause(and);
        }
        if (arg_ments.contains("or")) {
            or = true;
            arg_ments = StringUtils.substringBetween(arg_ments, "[", "]");
            cmd.setOrClause(or);
        }
        if (arg_ments.contains("in")) {
            in = true;
            cmd.setInClause(in);
        }
        if (arg_ments.contains(COLUMNS_IDENTIFIERS)) {
            selectAll = false;
        }
        if(and == false && or == false && arg_ments.contains(QUERY_RESTRICTION)){
            and = true;
            cmd.setAndClause(and);
        }

        /**
         * Clear outside brackets.
         */
        String[] argums = StringUtils.substringsBetween(arg_ments, "{", "}");

//        System.err.println("\t-------- 1st stage --------");
//        System.out.println("\tand "+and);
//        System.out.println("\tor "+or);
//        System.out.println("\tin "+in);


//        System.err.println("\t\t-------- 2nd stage --------");
        
        /**
         * Runs over each {} argument of db find method.
         */
        for (int i = 0; i < argums.length; i++) {

            /**
             * Clean even better the string. Until now, each argument has been
             * splitted the easiest way but it needs more cleaning.
             */
            String argument = StringUtils.remove(argums[i], "{");
            
            /**
             * Checks if each argument has Restriction and/or columns
             * identifier.
             *
             * Restriction identifier: $in -- $gte -- $lt Column identifier: :1
             */
            boolean hasRestriction = argument.contains(RESTRICTION);
            boolean hasQueryRestriction = argument.contains(COLUMNS_IDENTIFIERS);
            /**
             * Checks if this argument is a column identifier for query results.
             */
            if (hasQueryRestriction) {
                List<String> columns = Arrays.asList(argument.split("\\s*,\\s*"));

                for (String temp : columns) {
                    String columnClean = StringUtils.remove(temp, COLUMNS_IDENTIFIERS);
//                    System.out.println("\t\t\t Column: " + columnClean);
                    /**
                     * Set columns for be retrieved. 
                     */
                    cmd.setColumns(columnClean);
                }
            } else {
                /**
                 * Parse command based on its restriction.
                 */
                parseArgumentIntoCommand(argument, hasRestriction, cmd);
            }
        }
    }
}
