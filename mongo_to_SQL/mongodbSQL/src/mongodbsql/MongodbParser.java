package mongodbsql;
import configuration.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Michael Domingues <dominguesjust@gmail.com>
 *
 * Created on 6/nov/2014, 22:07:48 
 */
public class MongodbParser {

    private ComandBuilder cmdBuilder;
    private SQLParser sqlParser;
    
    public MongodbParser() {
        this.cmdBuilder = new ComandBuilder();
        this.sqlParser = new SQLParser();
    }


    public void readMongoDB()
    {
        BufferedReader br=null;
        StringBuilder sb = null;
        
        try {
            br = new BufferedReader(new FileReader(Configuration.MONGO_DB_FILE));
            sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                System.err.println("line: " + line);
                Command cmd = cmdBuilder.getCommand(line);
                sqlParser.processMongoDBComand(cmd);

                line = br.readLine();
            }
            String everything = sb.toString();
        } catch (IOException ex) {
            System.err.println("Exception while parsing mongoDB\n"+ex.getMessage());
        }
    }
    
    
}
