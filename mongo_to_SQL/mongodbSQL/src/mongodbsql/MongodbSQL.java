package mongodbsql;
import configuration.*;

/**
 *
 * @author Michael Domingues <dominguesjust@gmail.com>
 */
public class MongodbSQL {

    public static void main(String[] args) {
        
        /**
         * Delets output SQL file
         */
        Configuration.deleteOutput();
        
        /**
         * Inits all program with the mongoDB parser.
         */
        MongodbParser mp = new MongodbParser();
        mp.readMongoDB();
    }
}
