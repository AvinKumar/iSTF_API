/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SupervisorAPI;

import ReadPropertiespkg.ReadProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import Database.databaseConnection;
import  Database.opensearchkwysConnection.*;
import Log4j.PropConfigurator;
import DBComparison.CompareDB;
import Database.opensearchkwysConnection;

/**
 *
 * @author srivathg
 */
public class InsertGroupi18Values 
{
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InsertGroupi18Values.class.getName());
    public Connection conn=null;
    public Statement mystatement;
    String actual_result="";
    
    ResultSet rs;
    /* Connect to SQL Server
         Execute the Query and get the ID of different groups in different variables      
         Close the connection    */
    
    //ReadProperties rp=new ReadProperties();
    
    
    public void getSQLValues(String db_connect_string,String db_userid,String db_password)
    {
        try 
        {
         Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
          conn = DriverManager.getConnection(db_connect_string,
                  db_userid, db_password);
         System.out.println("Inside InsertGroupValues class;connected to SQLServer");
         log.info("Inside InsertGroupValues class;connected to SQLServer");
         //statement = conn.createStatement();
         //statement.setQueryTimeout(30);
                        
        } 
        catch (SQLException sqe)
        {
         // log.error("Error Message:"+e.getMessage());
          System.out.println("Some problem in connecting to DB");
          log.error("Some problem in connecting to DB");
          System.out.println(sqe.getMessage());
        }
        catch (ClassNotFoundException cnf)
        {
            System.out.println(cnf.getMessage());
            log.error("Some problem in connecting to DB");
        }
        
    }
    
    public String executeGroupQuery(String grpvalue,String db_name)
    {
        try
        {           
           PreparedStatement ps = conn.prepareStatement("SELECT ID FROM ["+db_name+"].[dbo].USERGROUP WHERE NAME = ?");
           ps.setString(1,grpvalue);
           rs = ps.executeQuery();
           while (rs.next())
           {
               actual_result=rs.getString("ID");
               System.out.println("Actual Group value name for chinese is "+grpvalue);
               System.out.println("Actual chinese mailbox id result is "+actual_result);
               log.info("Actual result is "+actual_result);
           }
        }
        catch (SQLException sqe)
        {
            System.out.println(sqe.getMessage());
            log.error(sqe.getMessage());
        }
        return actual_result;
    }
    
    public String executeRevGroupQuery(String revgrpvalue,String db_name)
    {
        try
        {           
           PreparedStatement ps = conn.prepareStatement("SELECT ID FROM ["+db_name+"].[dbo].[SCUser] where Username = ?");
           ps.setString(1,revgrpvalue);
           rs = ps.executeQuery();
           while (rs.next())
           {
               actual_result=rs.getString("ID");
               System.out.println("Actual result is "+actual_result);
               log.info("Actual result is "+actual_result);
           }
        }
        catch (SQLException sqe)
        {
            System.out.println(sqe.getMessage());
            log.error(sqe.getMessage());
        }
        return actual_result;
    }
    
    public String executeReviewerQuery(String reviewervalue,String db_name)
    {
        try
        {           
           PreparedStatement ps = conn.prepareStatement("SELECT ID FROM ["+db_name+"].[dbo].[SCUser] where DisplayName = ?");
           ps.setString(1,reviewervalue);
           rs = ps.executeQuery();
           while (rs.next())
           {
                actual_result=rs.getString("ID");
                System.out.println("Actual result is "+actual_result);
                log.info("Actual result is "+actual_result);
           }
        }
        catch (SQLException sqe)
        {
            System.out.println(sqe.getMessage());
            log.error(sqe.getMessage());
        }
        return actual_result;
    }
    
    public String executePolicyQuery(String fpolicyvalue,String db_name)
    {
        try
        {           
           PreparedStatement ps = conn.prepareStatement("SELECT ID FROM ["+db_name+"].[dbo].[SCRule] where Name = ?");
           ps.setString(1,fpolicyvalue);
           rs = ps.executeQuery();
           while (rs.next())
           {
                actual_result=rs.getString("ID");
                System.out.println("Actual result is "+actual_result);
                log.info("Actual result is "+actual_result);
           }
        }
        catch (SQLException sqe)
        {
                System.out.println(sqe.getMessage());
                log.error(sqe.getMessage());
        }
        return actual_result;
    }
    
        public String executeSupervisorMsgIDQuery(String msgstr,String db_name)
    {
        try
        {           
           PreparedStatement ps = conn.prepareStatement("SELECT ID FROM ["+db_name+"].[dbo].[singlecast] where Subject = ?");
           ps.setString(1,msgstr);
           rs = ps.executeQuery();
           while (rs.next())
           {
                actual_result=rs.getString("ID");
                System.out.println("Actual result is "+actual_result);
                log.info("Actual result is "+actual_result);
           }
        }
        catch (SQLException sqe)
        {
                System.out.println(sqe.getMessage());
                log.error(sqe.getMessage());
        }
        return actual_result;
    }
    
   
    
    public void connectSearchSqliteDB(String sqlite_dbname,String [] insert_value) throws ClassNotFoundException,SQLException
    {
        System.out.println("Inside connectSearchSqliteDB method");
        System.out.println("Sqlite database name is "+sqlite_dbname);
        Connection connection_searchdb = null;
        //Statement statement_searchdb;
        PreparedStatement searchps;
        
        Statement statement_for_testcaseupdate;
        PreparedStatement updatetestcase;
        ResultSet tcrs;
        
        //ReadProperties searchrp = new ReadProperties();
        Class.forName("org.sqlite.JDBC");
     /*   try 
        {
            connection_searchdb = DriverManager.getConnection("jdbc:sqlite:" + sqlite_dbname);
            System.out.println("Connected to SQLite DB");
            statement_for_testcaseupdate = connection_searchdb.createStatement();
        }
        catch(Exception e)
        {
            System.out.println("Problem in connecting to SQLite DB");
            System.out.println(e.getMessage());
        }*/
        try
        {
                connection_searchdb = DriverManager.getConnection("jdbc:sqlite:" + sqlite_dbname);
                System.out.println("Connected to SQLite DB");
                statement_for_testcaseupdate = connection_searchdb.createStatement();
                searchps=connection_searchdb.prepareStatement("UPDATE Chinese SET MailboxGroup=?");
                searchps.setString(1, insert_value[0]);
                searchps.execute();
                System.out.println("Updated SQLite DB Chinese with new MailboxGroup");
                searchps=null;
                              
                searchps=connection_searchdb.prepareStatement("UPDATE Japanese SET MailboxGroup=?");
                searchps.setString(1, insert_value[1]);
                searchps.execute();
                System.out.println("Updated SQLite DB Japanese with new MailboxGroup");
                searchps=null;
               
               /* searchps=connection_searchdb.prepareStatement("UPDATE French SET MailboxGroupValue=?");
                searchps.setString(1, insert_value[2]);
                searchps.execute();
                System.out.println("Updated SQLite DB with new ReviewGroupValue");
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE GroupValues SET MailboxGroupValue=?");
                searchps.setString(1, insert_value[3]);
                searchps.execute();
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE GroupValues SET MailboxGroupValue=?");
                searchps.setString(1, insert_value[4]);
                searchps.execute();
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE GroupValues SET MailboxGroupValue=?");
                searchps.setString(1, insert_value[5]);
                searchps.execute();
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE GroupValues SET SAMPLEDBY=?");
                searchps.setString(1, insert_value[6]);
                searchps.execute();
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE GroupValues SET SAMPLED2BY=?");
                searchps.setString(1, insert_value[7]);
                searchps.execute();
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE TextValues SET FlagPolicyValue=?");
                searchps.setString(1, insert_value[8]);
                searchps.execute();
                System.out.println("Updated SQLite DB with new FlagPolicyValue");
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE TextValues SET FLAGPOLICY2VALUE=?");
                searchps.setString(1, insert_value[9]);
                searchps.execute();
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE TextValues SET MATCHPOLICYVALUE=?");
                searchps.setString(1, insert_value[10]);
                searchps.execute();
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE TextValues SET MATCHPOLICY2VALUE=?");
                searchps.setString(1, insert_value[11]);
                searchps.execute();
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE TextValues SET SUPERVISORMESSAGECONTENT=?");
                searchps.setString(1, insert_value[12]);
                searchps.execute();
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE WildTextValues SET WSUPERVISORMSGCONTENT=?");
                searchps.setString(1, insert_value[12]);
                searchps.execute();
                searchps=null;
               
                searchps=connection_searchdb.prepareStatement("UPDATE WildTextValuesTwo SET WSUPERVISORMSGCONTENT=?");
                searchps.setString(1, insert_value[12]);
                searchps.execute();
                searchps=null;
                
                searchps=connection_searchdb.prepareStatement("UPDATE SpaceText SET SupervisorContent=?");
                searchps.setString(1, insert_value[12]);
                searchps.execute();
                searchps=null;
                
                searchps=connection_searchdb.prepareStatement("UPDATE QuoteTextt SET SupervisorContent=?");
                searchps.setString(1, insert_value[12]);
                searchps.execute();
                searchps=null; */
                
                //statement_for_testcaseupdate = connection_searchdb.createStatement();
                tcrs = statement_for_testcaseupdate.executeQuery("SELECT GID,TESTCASE FROM DM_TESTCASE");
                updatetestcase=connection_searchdb.prepareStatement("UPDATE DM_TESTCASE SET TESTCASE=? WHERE GID=?");
                
                String temp_source_chsmailboxvalue="<ChineseMailboxValue_value_combobox>(.*)</ChineseMailboxValue_value_combobox>";
                String temp_replace_chsmailboxvalue="<ChineseMailboxValue_value_combobox>"+insert_value[0]+"</ChineseMailboxValue_value_combobox>";
                
                String temp_source_jpnmailboxvalue="<JapaneseMailboxValue_value_combobox>(.*)</JapaneseMailboxValue_value_combobox>";
                String temp_replace_jpnmailboxvalue="<JapaneseMailboxValue_value_combobox>"+insert_value[1]+"</JapaneseMailboxValue_value_combobox>";
                
               /* String temp_source_reviewerGroupvalue="<ReviewerGroupValue_value_combobox>(.*)</ReviewerGroupValue_value_combobox>";
                String temp_replace_reviewerGroupvalue="<ReviewerGroupValue_value_combobox>"+insert_value[2]+"</ReviewerGroupValue_value_combobox>";
                
                String temp_source_reviewer2Groupvalue="<AdditionalReviewerGroupValue_value_combobox>(.*)</AdditionalReviewerGroupValue_value_combobox>";
                String temp_replace_reviewer2Groupvalue="<AdditionalReviewerGroupValue_value_combobox>"+insert_value[3]+"</AdditionalReviewerGroupValue_value_combobox>";
                
                String temp_source_reviewervalue="<Reviewers_value_combobox>(.*)</Reviewers_value_combobox>";
                String temp_replace_reviewervalue="<Reviewers_value_combobox>"+insert_value[4]+"</Reviewers_value_combobox>";
                
                String temp_source_reviewer2value="<AdditionalReviewers_value_combobox>(.*)</AdditionalReviewers_value_combobox>";
                String temp_replace_reviewer2value="<AdditionalReviewers_value_combobox>"+insert_value[5]+"</AdditionalReviewers_value_combobox>";
                
                String temp_source_Sampledbyvalue="<SampledBy_value_combobox>(*)</SampledBy_value_combobox>";
                String temp_replace_Sampledbyvalue="<SampledBy_value_combobox>"+insert_value[6]+"</SampledBy_value_combobox>";
                
                String temp_source_Sampledby2value="<AdditionalSampledBy_value_combobox>(*)</AdditionalSampledBy_value_combobox>";
                String temp_replace_Sampledby2value="<AdditionalSampledBy_value_combobox>"+insert_value[7]+"</AdditionalSampledBy_value_combobox>";
                
                String temp_source_FlaggedPolicyvalue="<FlaggedPolicy1_value_combobox>(.*)</FlaggedPolicy1_value_combobox>";
                String temp_replace_FlaggedPolicyvalue="<FlaggedPolicy1_value_combobox>"+insert_value[8]+"</FlaggedPolicy1_value_combobox>";
                
                String temp_source_Flagged2Policyvalue="<FlaggedPolicy2_value_combobox>(.*)</FlaggedPolicy2_value_combobox>";
                String temp_replace_Flagged2Policyvalue="<FlaggedPolicy2_value_combobox>"+insert_value[9]+"</FlaggedPolicy2_value_combobox>";
                
                String temp_source_MatchedPolicyvalue="<MatchPolicy_value_webedit>(.*)</MatchPolicy_value_webedit>";
                String temp_replace_MatchedPolicyvalue="<MatchPolicy_value_webedit>"+insert_value[10]+"</MatchPolicy_value_webedit>";
                
                String temp_source_MatchedPolicy2value="<AdditionalMatchPolicy_value_webedit>(.*)</AdditionalMatchPolicy_value_webedit>";
                String temp_replace_MatchedPolicy2value="<AdditionalMatchPolicy_value_webedit>"+insert_value[11]+"</AdditionalMatchPolicy_value_webedit>";
                
                String temp_source_supervisorIdValue="<AdvancedSupervisorMsgText_entry_webedit>(.*)</AdvancedSupervisorMsgText_entry_edit>";
                String temp_replace_supervisorIdValue="<AdvancedSupervisorMsgText_entry_webedit>"+insert_value[12]+"</AdvancedSupervisorMsgText_entry_edit>"; */
                
                while(tcrs.next())
               {
                    String mytestcasestring =tcrs.getString("TESTCASE");
                    String mytestcasegid=tcrs.getString("GID");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_chsmailboxvalue, temp_replace_chsmailboxvalue);
                    System.out.println("Replaced first occurance of chinese mailbox value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_jpnmailboxvalue, temp_replace_jpnmailboxvalue);
                    System.out.println("Replaced first occurance of Additional mailbox value under DM_TESTCASE");
                    
                  /*  mytestcasestring=mytestcasestring.replaceFirst(temp_source_reviewerGroupvalue, temp_replace_reviewerGroupvalue);
                    System.out.println("Replaced first occurance of reviewergroup value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_reviewer2Groupvalue, temp_replace_reviewer2Groupvalue);
                    System.out.println("Replaced first occurance of Additional reviewergroup value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_reviewervalue, temp_replace_reviewervalue);
                    System.out.println("Replaced first occurance of reviewer value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_reviewer2value, temp_replace_reviewer2value);
                    System.out.println("Replaced first occurance of Additional reviewer value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_Sampledbyvalue,temp_replace_Sampledbyvalue);
                    System.out.println("Replaced first occurance of Sampled by value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_Sampledby2value,temp_replace_Sampledby2value);
                    System.out.println("Replaced first occurance of Additional Sampled by value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_FlaggedPolicyvalue, temp_replace_FlaggedPolicyvalue);
                    System.out.println("Replaced first occurance of FlagPolicy value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_Flagged2Policyvalue, temp_replace_Flagged2Policyvalue);
                    System.out.println("Replaced first occurance of Additional FlagPolicy value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_MatchedPolicyvalue, temp_replace_MatchedPolicyvalue);
                    System.out.println("Replaced first occurance of Matched Policy value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_MatchedPolicy2value, temp_replace_MatchedPolicy2value);
                    System.out.println("Replaced first occurance of Additional Matched Policy value under DM_TESTCASE");
                    
                    mytestcasestring=mytestcasestring.replaceFirst(temp_source_supervisorIdValue, temp_replace_supervisorIdValue);
                    System.out.println("Replaced first occurance of Additional Matched Policy value under DM_TESTCASE"); */
                 
                    updatetestcase.setString(1, mytestcasestring);
                    updatetestcase.setString(2, mytestcasegid);
                    updatetestcase.executeUpdate();
                    System.out.println("InsertGroupValueClass-->Updated DM_TESTCASE table with new Group values");
                    log.info("InsertGroupValueClass-->Updated DM_TESTCASE table with new Group values");
               }
               
               //UPDATE GROUPVALUES SET <variable-1 for group name>='value'
        }
        catch (Exception ee)
        {
            System.out.print("Inside InsertGroupValues class-->Some problem in updating Group values of SQLite DB \t");
            log.error("Inside InsertGroupValues class-->Some problem in updating Group values of SQLite DB \t");
            System.out.println(ee.getMessage());
        }
    }
    
    public void updateValues(String db_connect_string,String db_name,String db_userid,String db_password,String sqlite_dbname, String [] group_value)
    {
        System.out.println("Inside updateValues Method of InsertGroupValues Class");
        String [] sql_values = new String[2];
        try
        {
            getSQLValues(db_connect_string, db_userid, db_password);
            System.out.println("After InsertGroupValues Class-->getSQLValues method");
            log.info("After InsertGroupValues Class-->getSQLValues method");
            
            sql_values[0]=executeGroupQuery(group_value[0],db_name); // Chinese MailboxGroup
            sql_values[1]=executeGroupQuery(group_value[1],db_name); // Japanese MailboxGroup
            
          /*  sql_values[2]=executeGroupQuery(group_value[2],db_name); // French MailboxGroup
            sql_values[3]=executeGroupQuery(group_value[3],db_name); // Italia MailboxGroup
            
            sql_values[4]=executeGroupQuery(group_value[4],db_name); // Japanese MailboxGroup
            sql_values[5]=executeGroupQuery(group_value[5],db_name); // Korean MailboxGroup
            
            sql_values[6]=executeGroupQuery(group_value[6],db_name); // Portugese MailboxGroup
            sql_values[7]=executeGroupQuery(group_value[7],db_name); // Russian MailboxGroup
            
            sql_values[8]=executeGroupQuery(group_value[8],db_name); // Spanish MailboxGroup
            sql_values[9]=executeGroupQuery(group_value[9],db_name); // Some other MailboxGroup */
            

            
            System.out.println("Populated sql_values Intermediate array");
            log.info("Populated sql_values Intermediate array");
            
            connectSearchSqliteDB(sqlite_dbname,sql_values);
            System.out.println("After the execution of connectSearchSqliteDB Method");
        }
        catch(SQLException sqe)
        {
            System.out.println("Some problem in updating Sqlite DB-- SQLException");
            log.error("Some problem in updating Sqlite DB-SQLException");
            System.out.println(sqe.getMessage());
        }
        catch(Exception eee)
        {
            System.out.println("Some problem in updating Sqlite DB -- Some Exception");
            log.error("Some problem in updating Sqlite DB - Some Exception");
            System.out.println(eee.getMessage());
        }
    }    
}
