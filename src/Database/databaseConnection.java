/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Database;
import Log4j.PropConfigurator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import java.util.Date;
/**
 *
 * @author PYanamalamanda
 */
public class databaseConnection {
    public Connection conn=null;
    public Statement statement=null;
   //  static Logger log = Logger.getLogger(databaseConnection.class.getName());

    public void dbConnect(String db_connect_string, String db_userid,String db_password) throws  Exception
    {
        
        PropConfigurator.configure();
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
         
            System.out.print("DB Connection Successfull" +"\n"); //Edited by Avin 
            statement = conn.createStatement();
            statement.setQueryTimeout(30);
                    
            } catch (SQLException e) {
            System.out.print(e.getMessage());
        // log.error(" Error in Connecting Database" + e.getMessage());
            }
   }


 
  public void   CloseConnection()
    {
        
        PropConfigurator.configure();
        
      try {

            if (statement != null) { statement.close(); }
            if(conn != null) conn.close();
         }
      catch (Exception e)
      {
        }//log.error("Error in closing Connection and statement" + e.getMessage());}
   }



//   public static void main(String[] args)  throws Exception
//   {
//      PropConfigurator.configure();
//      databaseConnection connServer = new databaseConnection();
//      connServer.dbConnect("jdbc:sqlserver://16.103.8.89;databaseName=AeD;", "sa",
//               "P@ssw0rd");
//      String s=connServer.getProjectList();
//      System.out.print(s);
//    //  connServer.CloseConnection();
//   }

}
