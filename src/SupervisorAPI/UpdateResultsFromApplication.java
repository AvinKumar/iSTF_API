package SupervisorAPI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author sayedmo
 */
public class UpdateResultsFromApplication {

    Connection connection = null;

    Statement statement = null;
    PreparedStatement prestatement = null;



    public void updateresults(String GID, int results) throws Exception {
        
            try {

                    //update into DB
                    statement.executeUpdate("UPDATE DM_TESTCASE SET RESULTS = '" + results + "' WHERE GID='" + GID + "'");

                } catch (SQLException e) {
                    
                    System.out.println("*******" + e.getCause());
                    if (prestatement != null) {
                        prestatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }

                }
            }
}