/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SupervisorAPI;

//import Reporting.GetDate;
import Database.openkwysConnection;
import ReadPropertiespkg.ReadProperties;
import atu.testng.reports.ATUReports;
import SupervisorAPI.MappingSearchFields;
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


/**
 *
 * @author $hpedservice
 */
public class CompareDSearch {

    public ArrayList<String> kwys_search_List = new ArrayList<String>();
    static Logger log = Logger.getLogger(CompareDSearch.class.getName());
    public static String subject = null;
    public static String typeone = null;
    public static String withindays = null;
    public static String path = null;
	public static Login l1;
    String httpgetrequestvalue, TESTCASE;
    public int RESULTS=0;
    public String res;
    // openkwysConnection kwys = null;
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
                                openkwysConnection kwys = new openkwysConnection();
                                kwys.openDatabaseConnection();
                               //Statement statement=null;
                                String temp="UPDATE WithinPastValue SET PastDateValue="+"'"+mydate+"'";
                                log.info(temp);
                                kwys.statement_kwys.executeUpdate(temp);
                                 kwys.connection_kwys.close();
                                 kwys = null;
                        }
                        catch(Exception e)
                        {
                            log.info("Problem with updating WithinPastValue table");
                            log.info(e.getMessage());
                        }
	}
        

    public void getGids(String feature) throws ClassNotFoundException {
        openkwysConnection kwys = new openkwysConnection();
        try {
            kwys.openDatabaseConnection();

           
            ResultSet rs = kwys.statement_kwys.executeQuery("SELECT DM_TESTCASE.GID, DM_TESTCASE.TESTCASE, DM_TESTCASE.RESULTS FROM DM_TESTCASE INNER JOIN DM_SCENARIO_REF ON DM_TESTCASE.TEMPLATE_GID = DM_SCENARIO_REF.GID INNER JOIN DM_MODELXML_REF ON DM_SCENARIO_REF.MODEL_GID_REF=DM_MODELXML_REF.GID AND DM_MODELXML_REF.FEATURENAME=\"" + feature + "\"");
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

    public void compare_hped_kwys(String item) throws Exception {

        Document htmlFile = null;

        cc = null;
       // l1 = null;
        int actual_ressult = 0;
        String actual_ressulttmp = null;
        cc = new Search();
        
        //httpgetrequest = new MappingSearchFields();
        
        openkwysConnection kwys = new openkwysConnection();
        try {
            kwys.openDatabaseConnection();
            log.info("GID******" + item);
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
                path+="ehr=0&emn=0&endTime=0:0&shr=0&smn=0&startTime=0:0"; // added by Gokul for making 
                //accUsers-matchingType=2& value in path need not be added for archive message search
                // test case free of the above eight parameters free
                httpgetrequestvalue=MappingSearchFields.maptestcasesapi(TESTCASE);
                    path +=httpgetrequestvalue;
                System.out.println("\nPrinting path:\n" +path);
                cc.ExecuteSearch(path, l1,3000);
				Thread.sleep(400);
                
           //     driver.navigate().to(rp.getProperty("StorePath"));
                
                htmlFile = Jsoup.parse(new File(rp.getProperty("StorePath")), "UTF-8");  
                Element pagingvalue=htmlFile.getElementById(rp.getProperty("SearchCountLocator"));
                String value= pagingvalue.ownText();
                String [] actual_ressulttmp1=value.split(" ");
                System.out.println("Paging value is "+value+" and "+actual_ressulttmp1[3]);
                NumberFormat usformat =NumberFormat.getNumberInstance(java.util.Locale.US);
                actual_ressult=usformat.parse(actual_ressulttmp1[3]).intValue();
                System.out.println("Actual result is "+actual_ressult);

                //get the search count from application
                //actual_ressulttmp = driver.findElement(By.id(rp.getProperty("SearchCountLocator"))).getText();
              //  String [] actual_ressulttmp1=actual_ressulttmp.split(" ");
                ////String lastWord = actual_ressulttmp.substring(actual_ressulttmp.lastIndexOf(" ") + 1);
                //String lastWord = actual_ressulttmp1[3];
               // System.out.println("********"+lastWord+"******"+actual_ressult);
               // NumberFormat usformat =NumberFormat.getNumberInstance(java.util.Locale.US);
                //actual_ressult = Integer.parseInt(lastWord);
                //actual_ressult=usformat.parse(lastWord).intValue();
                
                if(rp.getProperty("UpdateResultsFromApplication").equals("yes"))
                {
                    kwys.statement_kwys.executeUpdate("UPDATE DM_TESTCASE SET RESULTS = '" + actual_ressult + "' WHERE GID='" + item + "'");
                }

                log.info("Search Query ::>" + TESTCASE + " " + RESULTS);
                log.info("Actual Results::::" + actual_ressult);
                log.info("Expected Results" + RESULTS);
                Assert.assertEquals(actual_ressult, RESULTS);

                if (actual_ressult != RESULTS) {
                    ATUReports.setAuthorInfo("Supervisor Automation Team", GetDate.getdate(), "1.0");
                    ATUReports.add("Search Query ::>" + TESTCASE, String.valueOf(RESULTS), String.valueOf(actual_ressult), false);
                    Assert.fail("Actual Results::::" + actual_ressult + "Expected Results" + RESULTS);
                } else {
                    ATUReports.setAuthorInfo("Supervisor Automation Team", GetDate.getdate(), "1.0");
                    ATUReports.add("Search Query ::>" + TESTCASE, String.valueOf(RESULTS), String.valueOf(actual_ressult), false);

                }
                
                 Thread.sleep(10000);
            }

           Thread.sleep(1000); 
            kwys.statement_kwys.close();
            kwys.connection_kwys.close();

            kwys = null;
        } catch (SQLException e) {
            log.error("Error in getting search query by gid" + e.fillInStackTrace());
        }
         catch (NoSuchElementException e) {
            System.out.println("Element not found" + e.getMessage());
        }

    }
}
