/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBComparison;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import java.io.File;
import java.net.URL;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;
import java.util.ArrayList;
import Database.databaseConnection;

/**
 *
 * @author srivathg
 */
public class CompareDB
{
     ReadProperties rp = new ReadProperties(); // Object for reading the properties file
    static Logger log = Logger.getLogger(CompareDB.class.getName()); //Logger Object
 //   public Connection connection_compareDb;
  //  public Statement statement_compareDb;
    public final Properties configProp = new Properties();
    String [] queries;
    static databaseConnection sqldb=new databaseConnection();
    public Connection conn=null;
    public Statement statement=null;

    public String termQuery=null;
    public String auditRelatedQuery=null;
    public String sampledMailboxGroupCountQuery=null;
    
   /*    The following function is used to read the queries from the text file present
         in the given path (Which is the method parameter here); It throws exception and 
		 error in log, either the given path is not a valid folder or not able to open; 
		 It returns a String array containing corresponding queries from different text files
		 */

   public String[] readQueries(String queryPath) throws IOException 
    {
        //FileInputStream in = new FileInputStream("DBQueries")
        
        
        try
        {
            File dbQueryFolder = new File(queryPath);
            int noOfFiles=0;
            if(dbQueryFolder.isDirectory())
            {   
                System.out.println("Yes it is a folder");
               noOfFiles=dbQueryFolder.listFiles().length;
               queries=new String[noOfFiles];
               System.out.println("No of Files is"+noOfFiles);
               File[] queryfiles = dbQueryFolder.listFiles();
               for (int i = 0; i < queryfiles.length; i++)
               {
                        System.out.println(queryfiles[i]);
                        File file = queryfiles[i];
                        if (file.isFile() && file.getName().endsWith(".txt")) 
                        {
                            String query = FileUtils.readFileToString(file);
                            queries[i]=query;
                         } 
                }
                
            }
            else
            {
                log.error("It seems it is not a folder");
            }
        }
        catch(Exception e)
        {
                System.out.println("Some problem in reading the folder");
              log.error("Error in process: "+e.getMessage());
        }
         return queries;
    }
	
	/* connectCompareDB method takes Connection String, userid and password as parameters and connects to dB
		Pre-Requisite is the jar file SQLServerDriver to be present in lib folder
       */    
   public void connectCompareDB(String db_connect_string,String db_userid,String db_password)  throws ClassNotFoundException
    {
        try {
         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
          conn = DriverManager.getConnection(db_connect_string,
                  db_userid, db_password);
         System.out.print("connected");
         statement = conn.createStatement();
         statement.setQueryTimeout(30);
                        
      } catch (SQLException e) {
         // System.out.println("PRoblem in connecting to DB");
        //  System.out.print(e.getMessage());
          log.error("Error Message:"+e.getMessage());
      }
    } 
	
	/* createDatabase method takes name as the argument and creates databases in already connected SQL Server. It will be a 
		plain database without any tables.
		*/
    
    public void createDatabase(String dbName) throws ClassNotFoundException,SQLException
    {
        System.out.println("Inside CreateDB function");
        try
        {
            String dbCreation = "CREATE DATABASE "+dbName;
      //      System.out.println("The actual string is " + dbCreation);
           statement = conn.createStatement();
           statement.executeUpdate(dbCreation);
            log.info("Database"+dbName+" created successfully");
           //  System.out.println("Database"+dbName+" created successfully");
        }
        catch(SQLException se)
        {
           // System.out.println("Problem in creating database");
            log.info("Problem in " + dbName+" Database Creation");
             log.error("Error Message:"+se.getMessage());
        }
    }
    
	// This method is just for creating a table based on given query. This is not directly used, but getting called from executeDBOne,executeDBTwo
	
    public void createTable(String tableQuery) throws ClassNotFoundException, SQLException
    {
        try
        {
            statement.executeUpdate(tableQuery);
            log.info("Table created successfully");
            
        }
        catch(SQLException se)
                {
                    log.info("Problem in Table Creation");
                    se.printStackTrace();
                }
    }
    
	/* This specific method is used to create various tables based on the queries present on text files.
		Each item in the array is a table create statement. So no of tables getting created is equal to
		 array length which is equievalent to no of text files present in the folder;
		 
		 This is for RAP
		*/
	
    public void executeDBOne() throws IOException,ClassNotFoundException,SQLException,InterruptedException
    {
        CompareDB cdb=new CompareDB();
        int arrayLength=cdb.readQueries("DBQueries/RAP").length; //Gets number of queries present, to determine the no of tables
        String[] mystr=new String[arrayLength]; //String Array declration
        mystr=cdb.readQueries("DBQueries/RAP"); // Reading the qu
       //cdb.connectCompareDB("jdbc:sqlserver://16.150.56.142;databaseName=GOLD_NP;","sa","p@ssw0rd");
      // cdb.connectCompareDB("jdbc:sqlserver://16.150.56.142;","sa","p@ssw0rd");
      // sqldb.dbConnect("jdbc:sqlserver:"+rp.getProperty("sqlhostanddb"), rp.getProperty("sqlusername"), rp.getProperty("sqlpassword"));
        cdb.connectCompareDB("jdbc:sqlserver:"+rp.getProperty("Database_Location"), rp.getProperty("DB_username"), rp.getProperty("DB_pwd"));
        System.out.println("After connection..from main");
      //  cdb.createDatabase("COMPARED_RAP"); // We can enable this line, if we need to create New Database
        Thread.sleep(3000);
        for(int i=0;i<arrayLength;i++)
        {
            cdb.createTable(mystr[i]);
        } 
        
    }
	
	// Same as executeDBOne, but this one for Nightly Push 
    
     public void executeDBTwo() throws IOException,ClassNotFoundException,SQLException,InterruptedException
    {
        CompareDB cdb=new CompareDB();
        int arrayLength=cdb.readQueries("DBQueries/Nightly").length;
        String[] mystr=new String[arrayLength];
        mystr=cdb.readQueries("DBQueries/Nightly");
        cdb.connectCompareDB("jdbc:sqlserver:"+rp.getProperty("Database_Location"), rp.getProperty("DB_username"), rp.getProperty("DB_pwd"));
        System.out.println("After connection..from main");
       // cdb.createDatabase("COMPARED_NP"); // We can enable this line, if we need to create New Database
        Thread.sleep(3000);
        for(int i=0;i<arrayLength;i++)
        {
            cdb.createTable(mystr[i]);
        } 
        
    }
}
