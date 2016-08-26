/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Avin
 */

package SupervisorAPI;

import Database.databaseConnection;
//import Reporting.GetDate;
import Database.openkwysConnection;
import Log4j.PropConfigurator;
import ReadPropertiespkg.ReadProperties;
import atu.testng.reports.ATUReports;
import SupervisorAPI.MappingSearchFields;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import java.io.File;
import java.net.URLEncoder;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Listeners;

//@Listeners({ATUReportsListener.class, ConfigurationListener.class,
//    MethodListener.class})

public class CompareCompSearch {

    public ArrayList<String> kwys_search_List = new ArrayList<String>();
    static Logger log = Logger.getLogger(CompareCompSearch.class.getName());
    public static String SearchID = null;
    public static String path = null;
    public static String compSearchpath = null;
    public static String compSearchpath1 = null;
    public static String CompSearchName = null;
    public static String ID = null;
    public static String ViewName=null;
    public static String Summary=null;
    
    public static String databaseName=null;

    
    String httpgetrequestvalue, httpgetrequestvalue1, Finalhttpgetrequestvalue, searchidparam,TESTCASE,TESTCASEID;

    Connection connection = null;
    PreparedStatement prestatement;
    CompSearchModule cc = null;
    
    CompGetSavedSearchResults savedrs = new CompGetSavedSearchResults();
    
    Login l1 = null;
    ComprehensiveSearch CompSearch= null;
    databaseConnection sqldb=new databaseConnection();
    String CSRFToken = null;
    
    protected static WebDriver driver;
    ReadProperties rp = new ReadProperties();

    public void getGids(String feature) throws ClassNotFoundException {
        
        PropConfigurator.configure();
        openkwysConnection kwys = new openkwysConnection();
        try {
                kwys.openDatabaseConnection();
            
        //    ResultSet rs = kwys.statement_kwys.executeQuery("SELECT GID,TESTCASE FROM DM_TESTCASE");
/* --------------------------------------------------------------------------------------------------------                
Following is the query which is used in the code below: 
            ResultSet rs = kwys.statement_kwys.executeQuery(
                "SELECT DM_TESTCASE.GID, DM_TESTCASE.TESTCASE, DM_TESTCASE.RESULTS"
                + "FROM DM_TESTCASE"
                + "INNER JOIN DM_SCENARIO_REF"
                + "ON DM_TESTCASE.TEMPLATE_GID = DM_SCENARIO_REF.GID"
                + "INNER JOIN DM_MODELXML_REF"
                + "ON DM_SCENARIO_REF.MODEL_GID_REF=DM_MODELXML_REF.GID"
                + "AND DM_MODELXML_REF.FEATURENAME=\"" + feature + "\"");
                
             
-------------------------------------------------------------------------------------------------------- */

                
        ResultSet rs = kwys.statement_kwys.executeQuery("SELECT DM_TESTCASE.GID, DM_TESTCASE.TESTCASE, DM_TESTCASE.RESULTS FROM DM_TESTCASE INNER JOIN DM_SCENARIO_REF ON DM_TESTCASE.TEMPLATE_GID = DM_SCENARIO_REF.GID INNER JOIN DM_MODELXML_REF ON DM_SCENARIO_REF.MODEL_GID_REF=DM_MODELXML_REF.GID AND DM_MODELXML_REF.FEATURENAME=\"" + feature + "\"");
        
            while (rs.next()) {

                String temp = rs.getString("GID") + "," + "TESTCASE ::>" + rs.getString("TESTCASE");
                kwys_search_List.add(temp);
            }
            kwys.statement_kwys.close();
            kwys.connection_kwys.close();
            kwys = null;
        } catch (Exception e) {
            log.error("Error in Getting GIDS" + e.getMessage());
        }

    }

    public void compare_comp_search(String item) throws Exception {
        
        PropConfigurator.configure();
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);

        cc = null;
        l1 = null;
        int actual_result = 0;
        String actual_resulttmp = null;
        cc = new CompSearchModule();
        CompSearchName="RAPiSTFViews";
        
               
        openkwysConnection kwys = new openkwysConnection();
        try {
            kwys.openDatabaseConnection();
            log.info("GID::>" + item);
            String Query = "SELECT GID,TESTCASE FROM DM_TESTCASE WHERE GID = " + Integer.parseInt(item);
            log.info("Query::>" + Query);
            ResultSet rs = kwys.statement_kwys.executeQuery(Query);

            while (rs.next()) {        
                CompSearchName+=item;
                System.out.println("\n"+"Name of the Views is::"+CompSearchName+"\n");
                
                TESTCASE = rs.getString("TESTCASE");

                l1 = new Login();
                l1.login(rp.getProperty("LoginRequest"), rp.getProperty("username"), rp.getProperty("password"));
                CSRFToken = "CSRFTOKEN=" + cc.getCSRF( l1);
                
                path = rp.getProperty("SaveCompSearchRequest");
                path +=  CSRFToken + "" ;
                httpgetrequestvalue=MappingSearchFields.maptestcasesapi(TESTCASE);
                    //path +=httpgetrequestvalue+"&name="+CompSearchName+"&maxResults=10000"+"&randomSample=100&randomSelect=true"+"&searchType=REVIEW_QUEUE&global=true"+"&ondemand=asynchronous&policiesAndFlaggingOpen=false&randomSample=100&randomSelect=true&recipient=&sampledMGOption=1";
                    //path +=httpgetrequestvalue+"&name="+CompSearchName+"&maxResults=10000"+"&randomSample=100&randomSelect=true"+"&searchType=REVIEW_QUEUE&global=true"+"&ondemand=asynchronous&sampledMGOption=1";
                    //path +=httpgetrequestvalue+"&name="+CompSearchName+"&searchType=REVIEW_QUEUE&global=true"+"&ondemand=asynchronous";
                    //path +=httpgetrequestvalue+"&name="+CompSearchName+"&sampledMGOption=1"+"&accUsers-matchingType=2"+"&csType=IR";
                    //path +=httpgetrequestvalue;
                    //path+=Finalhttpgetrequestvalue;
                path +=httpgetrequestvalue+"&name="+CompSearchName;
                
                System.out.println ("------------------------------");
                System.out.println("\n"+"Printing HTTP Requests for saving Views::" +"\n" +path);
                System.out.println ("------------------------------");
                cc.ExecuteSearch(path, l1);
                System.out.println("~~~~~~~~~~Comprehensive Search Views is saved~~~~~~~~~~~" +"\n");
                System.out.println ("----------------------------------------------------------");
                
                //sql db connection
                 sqldb.dbConnect("jdbc:sqlserver:"+rp.getProperty("sqlhostanddb"), rp.getProperty("sqlusername"), rp.getProperty("sqlpassword"));
                 ResultSet rs2 = sqldb.statement.executeQuery("select ID from AuditSearch where Name='" + CompSearchName + "'");
                 //ResultSet rs2 = sqldb.statement.executeQuery("select ID from AuditSearch where Name='ViewName'");
                 rs2.next();
                 SearchID= rs2.getString("ID");
                 System.out.println("\n"+"SearchID::"+SearchID);

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~Following code is for initiating the Views~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
                compSearchpath = rp.getProperty("GetCompSearchResultRequest");
                compSearchpath +="runnow=true&reportID="+SearchID+"&csType=&substitute="; 
                System.out.println("\n"+"Printing HTTP Requests for initializing Views::" +"\n" +compSearchpath);
                savedrs.ExecuteView(item, SearchID, compSearchpath, l1);
                System.out.println("-------Initiated the created View(s)---------");
            
                              
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~                
                    // Verifying the View which got created       
            System.out.println("\nFetching View Name from Supervisor Database");
            sqldb.dbConnect("jdbc:sqlserver:"+rp.getProperty("sqlhostanddb"), rp.getProperty("sqlusername"), rp.getProperty("sqlpassword"));
            ResultSet rs3 = sqldb.statement.executeQuery("select Name from AuditSearch where ID='" + SearchID + "'");
            rs3.next();
            ViewName=rs3.getString("Name");
            log.info ("Actual Views Name::::" + ViewName);
            log.info ("Expected Views Name::::" + CompSearchName);
            
            System.out.println("\nFetching Views summary from Supervisor Database");
            sqldb.dbConnect("jdbc:sqlserver:"+rp.getProperty("sqlhostanddb"), rp.getProperty("sqlusername"), rp.getProperty("sqlpassword"));
            ResultSet rs4;
            rs4 = sqldb.statement.executeQuery("select Summary from AuditSearch where ID='" + SearchID + "'");
            rs4.next();
            Summary=rs4.getString("Summary");       
            log.info ("Printing Views summary::::"+ Summary);
                          
            if (ViewName.equals(CompSearchName)) {
                 System.out.println("Views Validation Passed");
                 ATUReports.setAuthorInfo("Supervisor Automation Team", GetDate.getdate(), "1.0");
                 ATUReports.add("Views Search Query ::>" + Summary, String.valueOf(ViewName), String.valueOf(CompSearchName), false);
                 
             }
             else {
                 System.out.println("Views Validation Failed");
                 ATUReports.setAuthorInfo("Supervisor Automation Team", GetDate.getdate(), "1.0");
                 ATUReports.add("Views Search Query ::>" + Summary, String.valueOf(ViewName), String.valueOf(CompSearchName), false);                 
                 Assert.fail("Actual Views Name::::" + ViewName +"\t"+"Expected Views Name" + CompSearchName);
             }
            }
            kwys.statement_kwys.close();
            kwys.connection_kwys.close();
            sqldb.CloseConnection();
            kwys = null;
        } catch (SQLException e) {
            log.error("Error in getting search query by gid" + e.fillInStackTrace());
        }
         catch (NoSuchElementException e) {
            System.out.println("Element not found" + e.getMessage());
        }
       
    }
    
//    public static void main(String[] args) throws Exception {
//        PropConfigurator.configure();
//        CompareCompSearch cc=new CompareCompSearch();
//        cc.compare_comp_search("3");
//    }

}
