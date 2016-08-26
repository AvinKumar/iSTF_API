package test1;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package test;
import SupervisorAPI.Login;
//import SupervisorAPI.CompareDSearch;
import SupervisorAPI.*;
import atu.testng.reports.ATUReports;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ReadPropertiespkg.ReadProperties;
//import static SupervisorAPI.CompareDSearch.l1;
import static SupervisorAPI.ReviewQueueSearch.l1;
import Database.ChangeArchiveStartDate;
import Database.opensearchkwysConnection;

/**
 *
 * @author Sayed
 */
 
// @Listeners({ATUReportsListener.class, ConfigurationListener.class,
//    MethodListener.class})  --commented by Avin

public class TestReviewQueueResults
 {

  //CompareDSearch chps = new ArchiveSearchTwo();
  ReviewQueueSearch chps = new ReviewQueueSearch();
  InsertGroupValues igv = new InsertGroupValues();
  ReadProperties rp = new ReadProperties();
  static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TestReviewQueueResults.class.getName());
  
  @BeforeSuite
  public void bsuite() throws Exception
  {
       try
       {
        System.out.println("Inside the before suite method");
        chps.putCurrentDate();
        System.out.println("Put Current Date method got executed");
       }
       catch (Exception e)
       {
           System.out.println("Some problem in ReviewSearch putCurrentDate");
           System.out.println(e.getMessage());
           log.info(e.getMessage());
       }
      try
       {
 String [] input_group_value=new String[13];
        input_group_value[0]=rp.getProperty("MAILBOXGROUPVALUE");
        input_group_value[1]=rp.getProperty("MAILBOXGROUP2VALUE");
        input_group_value[2]=rp.getProperty("REVIEWGROUPVALUE");
        input_group_value[3]=rp.getProperty("REVIEWGROUP2VALUE");
        input_group_value[4]=rp.getProperty("REVIEWERVALUE");
        input_group_value[5]=rp.getProperty("REVIEWER2VALUE");
        input_group_value[6]=rp.getProperty("SAMPLEDBY");
        input_group_value[7]=rp.getProperty("SAMPLED2BY");
        input_group_value[8]=rp.getProperty("FLAGGEDPOLICY");
        input_group_value[9]=rp.getProperty("FLAGGED2POLICY");
        input_group_value[10]=rp.getProperty("MATCHEDPOLICY");
        input_group_value[11]=rp.getProperty("MATCHED2POLICY");
        input_group_value[12]=rp.getProperty("SUPMSGIDMSG");

            igv.updateValues("jdbc:sqlserver:"+rp.getProperty("Database_Location"),rp.getProperty("databaseName"), rp.getProperty("DB_username"), rp.getProperty("DB_pwd"),rp.getProperty("ReviewQueue_SearchDB_Location"),input_group_value);
       }
      
       catch (Exception e)
       {
           System.out.println("Some problem in ReviewSearch InsertGroupValues");
           System.out.println(e.getMessage());
           log.info(e.getMessage());
       }  
  }
  
  @BeforeTest
   public void logintoSupervisor()
   {
        ReadProperties rp = new ReadPropertiespkg.ReadProperties();
		//Login l1;
      l1 = new Login();
        try {
            l1.login(rp.getProperty("LoginRequest"), rp.getProperty("reviewqueue_search_username"), rp.getProperty("reviewqueue_search_password"));
		System.out.println("################Successfully logged in from TestNG java file with @BeforeTest############");
                                System.out.println();
        } catch (Exception ex) {
            System.out.println("Exception in logging to Supervisor system" + ex.getMessage());
            System.out.println();
        }
	}
      
    @DataProvider(name = "test1")
    public Object[][] GetSearchQueries() throws Exception {
        chps.getGids();
        Object[][] obj = new Object[chps.kwys_search_List.size()][2];
        for (int i = 0; i < chps.kwys_search_List.size(); i++) {
            String temp_ar[] = chps.kwys_search_List.get(i).split(",");
            obj[i] = new Object[]{chps.kwys_search_List.get(i),"Test for Metadata Verifiaction for Document " +i};
            obj[i] = new Object[]{temp_ar[0], temp_ar[1]};
           // System.out.println(obj[i]);
        }
        return obj;
    }

    @Test(dataProvider = "test1")
    public void testReviewQueueSearchResults(String parm1, Object parm2) throws Exception {
        System.out.println("Inside the TestReviewQueueSearch method");
        chps.compare_hped_kwys(parm1);
       // Assert.assertEquals(true, true, cmd.metadata_equall);
    }
        
 }