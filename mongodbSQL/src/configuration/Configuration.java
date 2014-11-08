package configuration;

import java.io.File;

/**
 *
 * @author Michael Domingues <dominguesjust@gmail.com>
 *
 * Created on 6/nov/2014, 22:22:20 
 */
public class Configuration {
    
    public final static String MONGO_DB_FILE = "db_input.txt";
    public final static String SQL_DB_FILE = "db_output.txt";
    
    public static void deleteOutput(){
        new File(SQL_DB_FILE).delete();
    }

}
