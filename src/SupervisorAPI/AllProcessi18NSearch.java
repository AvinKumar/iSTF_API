/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SupervisorAPI;

//import Reporting.GetDate;
import Database.opensearchkwysConnection;
import ReadPropertiespkg.ReadProperties;
import atu.testng.reports.ATUReports;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import java.io.File;
import java.io.IOException;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Listeners;
//import Database.ChangeArchiveStartDate;
import Database.changeString;
import Database.databaseConnection;




/**
 *
 * @author $hpedservice
 */
public class AllProcessi18NSearch
{

    public ArrayList<String> kwys_search_List = new ArrayList<String>();
    static Logger log = Logger.getLogger(AllProcessi18NSearch.class.getName());
    public static String subject = null;
    public static String typeone = null;
    public static String withindays = null;
    public static String path = null;
    public static Login l1;
    String httpgetrequestvalue, TESTCASE;
    public int RESULTS=0;
    public String res;
    String[] actual_ressulttmp1;
    // opensearchkwysConnection kwys = null;
    Connection connection = null;
    PreparedStatement prestatement;
    Search cc = null;
    //Login l1 = null;
    
    
    //public static String subject,typeone,withindays,path;
    protected static WebDriver driver;
    ReadProperties rp = new ReadProperties();
    
     

    
	
    // To enter current date on WithinPastValue table
	public void putCurrentDate() throws Exception
	{
                        try
                        {
                                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                Date date = new Date();
                                String mydate=dateFormat.format(date);
                                mydate=mydate.replace("/", "%2F");
                                log.info(mydate);
                                opensearchkwysConnection kwys = new opensearchkwysConnection();
                                kwys.openDatabaseConnection(rp.getProperty("I18N_SearchDB_Location"));
                                System.out.println("####################################################");
                               //Statement statement=null;
                                String temp="UPDATE WithinPastValue SET PastDateValue="+"'"+mydate+"'";
                                log.info(temp);
                                System.out.println("Temp update statement is : "+temp);
                                kwys.statement_kwys.executeUpdate(temp);
                                kwys.connection_kwys.close();
                                kwys = null;
                        }
                        catch(Exception e)
                        {
                            log.info("Problem with updating WithinPastValue table on i18N database");
                            log.info(e.getMessage());
                            System.out.println(e.getMessage());
                        }
	}
        
        
        	/*public void putArchiveSearchStartDate() throws Exception
	{
                        try
                        {
                                /*DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                Date date = new Date();
                                String mydate=dateFormat.format(date);
                                mydate=mydate.replace("/", "%2F");
                                log.info(mydate);
                                opensearchkwysConnection kwys = new opensearchkwysConnection();
                                kwys.openDatabaseConnection();
                               //Statement statement=null;
                                String temp="UPDATE WithinPastValue SET PastDateValue="+"'"+mydate+"'";
                                log.info(temp);
                                kwys.statement_kwys.executeUpdate(temp);
                                kwys.connection_kwys.close();
                                kwys = null; */
                              /*  ChangeArchiveStartDate cadObj = new ChangeArchiveStartDate();
                                String tdate = cadObj.getDate();
                                cadObj.updateArchiveSearchDateOnSQLiteDB(tdate);
                        }
                        catch(Exception e)
                        {
                            log.info("Problem with updating WithinPastValue table");
                            log.info(e.getMessage());
                        } 
	} */

    public void getGids() throws ClassNotFoundException 
    {
        opensearchkwysConnection kwys = new opensearchkwysConnection();
        try {
            kwys.openDatabaseConnection(rp.getProperty("I18N_SearchDB_Location"));

           
            ResultSet rs = kwys.statement_kwys.executeQuery("SELECT GID,TESTCASE FROM DM_TESTCASE");
            while (rs.next()) {

                String temp = rs.getString("GID") + "," + "TESTCASE ::-->" + rs.getString("TESTCASE");
                kwys_search_List.add(temp);
            }
            kwys.statement_kwys.close();
            kwys.connection_kwys.close();

            kwys = null;
        } catch (Exception e) {
            log.error("Error in Getting GIDS" + e.getMessage());
        }

    }

    public void compare_hped_kwys(String item) throws Exception 
    {
        System.out.println("Inside i8N - Compare_hped_kwys method");
        Document htmlFile = null;
        String value="";
        cc = null;
       // l1 = null;
        int actual_ressult = 0;
        String actual_ressulttmp = null;
        cc = new Search();
        Date endDate = new Date();
        DateFormat endDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String myendDate=endDateFormat.format(endDate);
        myendDate=myendDate.replace("/", "%2F");
        //httpgetrequest = new MappingSearchFields();
        
        opensearchkwysConnection kwys = new opensearchkwysConnection();
        try {
            kwys.openDatabaseConnection(rp.getProperty("I18N_SearchDB_Location"));
            //log.info("GID******" + item);
            String Query = "SELECT GID,TESTCASE,RESULTS FROM DM_TESTCASE WHERE GID = " + Integer.parseInt(item);
            log.info("GID******" + Query);
            ResultSet rs = kwys.statement_kwys.executeQuery(Query);

            while (rs.next()) {
                TESTCASE = rs.getString("TESTCASE");
                res = rs.getString("results");
                if(res != null && !res.isEmpty()) {
                    RESULTS = Integer.parseInt(rs.getString("results"));
                } else {
                }
                
              //  l1 = new Login(); Commented by Gokul , to avoid login for every test case; It is placed under @Beforetest
              //  l1.login(rp.getProperty("LoginRequest"), rp.getProperty("username"), rp.getProperty("password"));

                path = rp.getProperty("SearchRequest");
                path+="ehr=0&emn=0&endTime=0%3A0&shr=0&smn=0&startTime=0%3A0&language=en&endDate="+myendDate+"&sortByAsc=false&sortField=dateHeader&useActRep=false"; // added by Gokul for making This is unique to i18N search.
                //accUsers-matchingType=2& value in path need not be added for archive message search
                httpgetrequestvalue=MappingSearchFields.maptestcasesapi(TESTCASE);
                    path +=httpgetrequestvalue;
                System.out.println("\nPrinting path:\n" +path);
                cc.ExecuteSearch(path, l1,4000);  //The no of parameters changed. Added long waitTime as 3rd parameter to control the wait time for each search type separately
                actual_ressulttmp1=null; // Added by gokul fo re-initializing this string array
           //     driver.navigate().to(rp.getProperty("StorePath"));
                try
                {
                    htmlFile = Jsoup.parse(new File(rp.getProperty("StorePath")), "UTF-8");  
                    Element pagingvalue=htmlFile.getElementById(rp.getProperty("SearchCountLocator"));
                    value= pagingvalue.ownText();
                    actual_ressulttmp1=value.split(" ");
                    System.out.println("Paging value is "+value+" and "+actual_ressulttmp1[3]);
                    NumberFormat usformat =NumberFormat.getNumberInstance(java.util.Locale.US);
                    actual_ressult=usformat.parse(actual_ressulttmp1[3]).intValue();
                    System.out.println("Actual result is "+actual_ressult);
                }
                catch(Exception e)
                {
                    System.out.println("Some problem in parsing resulting demo file in compare_hped_kwys method");
                    System.out.println(e.getMessage());
                    log.info("Some problem in parsing resulting demo file in compare_hped_kwys method");
                    log.info(e.getMessage());
                    actual_ressult=-1;
                }


                
                if(rp.getProperty("UpdateResultsFromApplication").equals("yes"))
                {
                    System.out.println("Inside the updateresultsfromapplication logic");
                    kwys.statement_kwys.executeUpdate("UPDATE DM_TESTCASE SET RESULTS = '" + actual_ressult + "' WHERE GID='" + item + "'");
                }

                log.info("Search Query ::>" + TESTCASE + " " + RESULTS);
                log.info("Actual Results---->" + actual_ressult);
                log.info("Expected Results---->" + RESULTS);
                //Assert.assertEquals(actual_ressult, RESULTS); //commenting it out so that searchQueryinResults will get executed.-Gokul

                if (actual_ressult != RESULTS) {
                    ATUReports.setAuthorInfo("Supervisor Automation Team", GetDate.getdate(), "1.0");
                    ATUReports.add("Search Query ::>" + searchQueryinResults(TESTCASE), String.valueOf(RESULTS), String.valueOf(actual_ressult), false);
                    Assert.fail("Actual Results::::" + actual_ressult + "Expected Results" + RESULTS);
                } else {
                    ATUReports.setAuthorInfo("Supervisor Automation Team", GetDate.getdate(), "1.0");
                    ATUReports.add("Search Query ::>" + searchQueryinResults(TESTCASE), String.valueOf(RESULTS), String.valueOf(actual_ressult), false);

                }
                
                 Thread.sleep(5000);
            }

           Thread.sleep(1000); 
            kwys.statement_kwys.close();
            kwys.connection_kwys.close();

            kwys = null;
        } 
        
        catch (SQLException e) 
        {
            log.error("Error in getting search query by gid" + e.fillInStackTrace());
        }
        
        catch (NoSuchElementException e) 
        {
            System.out.println("Element not found" + e.getMessage());
        }
         
    }
    
     public String searchQueryinResults(String inputTestCase)
    {
        int firstindex_value=0;
        int secindex_value=0;
        String temp_key;
        String temp_value;
        String temp_final="";
         StringBuffer final_tc= new StringBuffer();
        //final_tc="";
        System.out.println("Inside searchQueryinResults method");
      
        //System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
       // System.out.println(inputTestCase);
        //System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        try
        {
           temp_key="";
           temp_value="";
           temp_final="";
            for (String retval: inputTestCase.split("><"))
            {   
                if(!(retval.isEmpty()) && (retval.compareTo("<root")!=0))
                {  
                    for (String sec_retval:retval.split(">"))
                    {   
                        if(!(sec_retval.isEmpty()) && (sec_retval.compareTo("<root")!=0))
                        {    
                                if(!sec_retval.contains("<"))
                                {
                                    secindex_value=sec_retval.indexOf('_');
                                    temp_key=sec_retval.substring(0,secindex_value)+" = ";
                                    
                                }
                                else
                                {
                                    firstindex_value=sec_retval.indexOf('<');
                                    temp_value=sec_retval.substring(0, firstindex_value);
                                    temp_final=temp_key+temp_value;
                                    if(temp_final.contains("="))
                                    {  
                                      if(!((temp_final.contains("CSTypeValue"))||(temp_final.contains("DateRangeWithin"))||(temp_final.contains("UseAdvancedAllProcess"))||(temp_final.contains("UseExtendedAllProcess")) ||(temp_final.contains("AccountableUsers"))||(temp_final.contains("MatchingType"))||(temp_final.contains("OpenAttachmentName"))||(temp_final.contains("AdvancedTextFlag")||(temp_final.contains("CaseFlag1"))||(temp_final.contains("CaseFlag2"))||(temp_final.contains("ActionStatusFlagged "))||(temp_final.contains("MessageSizeFlag"))||(temp_final.contains("AdditionalMatchPolicy"))||(temp_final.contains("PolicyAndFlag"))||(temp_final.contains("PolicyFieldInclusionFlag "))||(temp_final.contains("WithNoLabel ")))))
                                     {
                                            final_tc=final_tc.append(temp_final);
                                            final_tc=final_tc.append("||");
                                         // System.out.println("Now the final_tc ------------------> "+final_tc.toString());
                                       }
                                    }
                                    else
                                    {
                                       
                                    }
                                }
                        }
                        else
                        {
                            continue;
                        }
                    }
                }
                else
                {
                    System.out.println("In first else part of searchQueryinResults method");
                    continue;
                }
            }
        }
        catch(IndexOutOfBoundsException e)
        {
           System.out.print("Some problem in searchQueryinResults method"+"\t");
           System.out.println(e.getMessage());
        }
                  return(final_tc.toString());
     }
}
