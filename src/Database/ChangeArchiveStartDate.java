/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import SupervisorAPI.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang3.time.DateUtils;
import Log4j.PropConfigurator;
import ReadPropertiespkg.ReadProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author $srivathg
 * @Purpose: To change the <startdate> value in Archive Search 2 i.e., less than 90 days to currentdate-89 days
 */
public class ChangeArchiveStartDate 
{
    public Connection archivesearch_idol_db = null;
    public Statement statement_kwys;
    PreparedStatement archivesearch_ps;
    //Method to get the current date - 89 days
    public String getDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        
        Date past_eightynine_days=DateUtils.addDays(new Date(),-89); // using commons.lang3.time.DateUtils
        String temp_date=dateFormat.format(past_eightynine_days);
        temp_date=temp_date.replace("/", "%2F");
       // System.out.println("################################################");
       // System.out.println("$$$$$$$$$$$$$-----------------"+temp_date+"---------------------------$$$$$$$$$$$$$$$$");
        //System.out.println("################################################");
        //return dateFormat.format(past_eightynine_days);
        return temp_date.toString();
        
    }
        
    
     public void updateArchiveSearchDateOnSQLiteDB(String date_value) throws ClassNotFoundException, SQLException {
        PropConfigurator.configure();
        int i=1;
        ReadProperties rp = new ReadProperties();
        Class.forName("org.sqlite.JDBC");
        try {
            archivesearch_idol_db = DriverManager.getConnection("jdbc:sqlite:" + rp.getProperty("Archive_IdolSearchDB_Location"));
            System.out.println("After connecting to Archive_IdolSearchDB_Location");
            archivesearch_idol_db.setAutoCommit(false);
            statement_kwys = archivesearch_idol_db.createStatement();
            statement_kwys.setQueryTimeout(30);  // set timeout to 30 sec.
        } catch (SQLException e) {
           System.out.println("Some problem in connecting to Archive_IdolSearchDB_Location");
        }
        ResultSet rs_for_archivesearch =statement_kwys.executeQuery("SELECT GID,TESTCASE FROM DM_TESTCASE");
        archivesearch_ps=archivesearch_idol_db.prepareStatement("UPDATE DM_TESTCASE SET TESTCASE=? WHERE GID=?");
        
        while (rs_for_archivesearch.next())
        {
                System.out.println("Inside while loop");
                String myTestcaseString =rs_for_archivesearch.getString("TESTCASE");
                String myTestcasegid=rs_for_archivesearch.getString("GID");
                String replacement_string = "<StartDate_value_webedit>";
                replacement_string=replacement_string.concat(date_value);
                replacement_string=replacement_string.concat("</StartDate_value_webedit>");
                //System.out.println("Replacement String is "+replacement_string);
                //System.out.println(myTestcaseString);
                myTestcaseString=myTestcaseString.replaceFirst("<StartDate_value_webedit>(.*)</StartDate_value_webedit>", replacement_string);
                System.out.println(myTestcaseString);
                archivesearch_ps.setString(1, myTestcaseString);
                archivesearch_ps.setString(2, myTestcasegid);
               try
               {
                        archivesearch_ps.executeUpdate();                       
               }
               catch(SQLException e)
               {
                   System.out.println("Inside the catch block");
                   e.getMessage();
               }
              myTestcaseString=null;
              myTestcasegid=null;
               i++;
        }
                System.out.println("Total test cases updated = "+i);
                archivesearch_idol_db.commit();
                archivesearch_idol_db.close();

    }
    /* public static void main(String args[]) throws ClassNotFoundException,SQLException
    {
        System.out.println("Inside the main ");
        ChangeArchiveStartDate gd=new ChangeArchiveStartDate();
        String target_date=new String();
        target_date=gd.getDate().toString();
        gd.updateArchiveSearchDateOnSQLiteDB(target_date);
        System.out.println("After all execution");
    } */
}
