/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

//import  Database.openkwysConnection;
import ReadPropertiespkg.ReadProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import Database.databaseConnection;
import static Database.openkwysConnection.log;
import Log4j.PropConfigurator;

/**
 *
 * @author srivathg
 */
public class changeString
{
    ReadProperties rp=new ReadProperties();
    int i=1;
    public void connectDB() throws ClassNotFoundException,SQLException
    {
        PropConfigurator.configure();
        
        Connection connection_kwys = null;
         Statement statement_kwys;
         PreparedStatement ps;
       ReadProperties rp = new ReadProperties();
        Class.forName("org.sqlite.JDBC");
        try {
            connection_kwys = DriverManager.getConnection("jdbc:sqlite:" + rp.getProperty("KWYS_Location"));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
         System.out.println("Got connected");
         System.out.println("The database name is "+rp.getProperty("KWYS_Location"));
         statement_kwys = connection_kwys.createStatement();
           ResultSet rs =statement_kwys.executeQuery("SELECT GID,TESTCASE FROM DM_TESTCASE");
          ps=connection_kwys.prepareStatement("UPDATE DM_TESTCASE SET TESTCASE=? WHERE GID=?");
           while (rs.next())  {
                System.out.println("Inside while loop");
                String mystring =rs.getString("TESTCASE");
                String mygid=rs.getString("GID");
               // System.out.print(mygid+"--->");
               mystring=mystring.replaceFirst("ALL_PROCESSED", "1");
               System.out.println(mystring);
               ps.setString(1, mystring);
               ps.setString(2, mygid);
           //   String temp="UPDATE DM_TESTCASE SET TESTCASE= "+"'"+mystring+"'"+" WHERE GID="+i;
           //   System.out.println("temp string is "+temp);
            ps.executeUpdate();
             // kwys.statement_kwys.executeUpdate(temp);
              //mystring=null;
              //mygid=null;
              i++;
               //rs.next();
 
               }
               // kwys.statement_kwys.executeUpdate(temp);
                System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("i======="+i);
            } 
                              //   kwys.connection_kwys.close();
    
    public static void main(String args[]) throws Exception
    {
        changeString st=new changeString();
        st.connectDB();
    }
}


