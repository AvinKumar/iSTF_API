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
import static SupervisorAPI.AllProcessi18NSearch.l1;
import Database.ChangeArchiveStartDate;
import Database.opensearchkwysConnection;
import SupervisorAPI.InsertGroupi18Values;

/**
 *
 * @author Sayed
 */
 
// @Listeners({ATUReportsListener.class, ConfigurationListener.class,
//    MethodListener.class})  --commented by Avin

public class TestAllProcessi18NSearchResults
 {

  //CompareDSearch chps = new ArchiveSearchTwo();
     AllProcessi18NSearch chps = new AllProcessi18NSearch();
     InsertGroupi18Values igv = new InsertGroupi18Values();
     ReadProperties rp = new ReadProperties();
     static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TestAllProcessi18NSearchResults.class.getName());
  
  @BeforeSuite
  public void bsuite() throws Exception
  {
       try
       {
        System.out.println("Inside the before suite method");
        chps.putCurrentDate();
        System.out.println("Put Current Date method got executed");
       // System.out.println("This is the actual before class body");
       }
       
       catch (Exception e)
       {
           System.out.println("Some problem in AllProcessi18N putCurrentDate or putArchiveCurrentDate");
           System.out.println(e.getMessage());
           log.info(e.getMessage());
       }
       try
       {
        String [] input_group_value=new String[2];
        input_group_value[0]=rp.getProperty("CHINESEMAILBOXGROUPVALUE"); // used for BVT
        input_group_value[1]=rp.getProperty("JAPANESEMAILBOXGROUPVALUE"); // used for BVT
       /* input_group_value[2]=rp.getProperty("FRENCHMAILBOXGROUPVALUE");
        input_group_value[3]=rp.getProperty("GERMANMAILBOXGROUPVALUE");
        input_group_value[4]=rp.getProperty("ITALIAMAILBOXGROUPVALUE");
        input_group_value[5]=rp.getProperty("JAPANESEMAILBOXGROUPVALUE");
        input_group_value[6]=rp.getProperty("KOREANMAILBOXGROUPVALUE");
        input_group_value[7]=rp.getProperty("PORTUGESEMAILBOXGROUPVALUE");
        input_group_value[8]=rp.getProperty("RUSSIANMAILBOXGROUPVALUE");
        input_group_value[9]=rp.getProperty("SPANISHMAILBOXGROUPVALUE"); */

        
        igv.updateValues("jdbc:sqlserver:"+rp.getProperty("Database_Location"), rp.getProperty("databaseName"), rp.getProperty("DB_username"), rp.getProperty("DB_pwd"),rp.getProperty("I18N_SearchDB_Location"),input_group_value);
       }
      
       catch (Exception e)
       {
           System.out.println("Some problem in AllProcessi18NSearch InsertGroupValues");
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
            l1.login(rp.getProperty("LoginRequest"), rp.getProperty("allprocessi18n_search_username"), rp.getProperty("allprocessi18n_search_password"));
			System.out.println("################Successfully logged in############");
        } catch (Exception ex) {
            System.out.println("Exception in looging to Supervisor system" + ex.getMessage());
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
            System.out.println(obj[i]);
        }
        return obj;
    }

    @Test(dataProvider = "test1")
    public void testAllProcessi18NSearchResults(String parm1, Object parm2) throws Exception {
        System.out.println("Inside the TestAllProcessi18NSearch method");
        chps.compare_hped_kwys(parm1);
       // Assert.assertEquals(true, true, cmd.metadata_equall);
    }
        
    /*@AfterSuite
    
    public void AClass()
    {
        System.out.println("This is @Aftersuite statement");
    } */
 }