package test1;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package test;
import SupervisorAPI.Login;
import SupervisorAPI.CompareDSearch;
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
import static SupervisorAPI.CompareDSearch.l1;
import org.testng.ITestContext;

/**
 *
 * @author Sayed
 */
 
// @Listeners({ATUReportsListener.class, ConfigurationListener.class,
//    MethodListener.class})

public class TestDSSearchResults {
	


  CompareDSearch chps = new CompareDSearch();
  static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TestDSSearchResults.class.getName());
 // protected static WebDriver driver;
// static   
//              {
//System.setProperty("atu.reporter.config", "D:\\atu.properties");
//}
 /*   @BeforeMethod
     public void bclass() throws Exception
     {
         
      chps.initializeDriver();
     } */
  
  @BeforeSuite
  public void bsuite() throws Exception
  {
       try
       {
        System.out.println("Inside the before suite method");
        chps.putCurrentDate();
       // System.out.println("This is the actual before class body");
       }
       
       catch (Exception e)
       {
           System.out.println("Some problem in putCurrentDate");
           System.out.println(e.getMessage());
       }
  }
  
  @BeforeTest
   public void logintoSupervisor()
   {
        ReadProperties rp = new ReadPropertiespkg.ReadProperties();
		//Login l1;
      l1 = new Login();
        try {
            l1.login(rp.getProperty("LoginRequest"), rp.getProperty("username"), rp.getProperty("password"));
			System.out.println("################Successfully logged in############");
        } catch (Exception ex) {
            System.out.println("Exception in looging to hped system" + ex.getMessage());
        }
	}
      
    @DataProvider(name = "test1")
    
// ~~~~~~~~~~added by Avin ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
     public Object[][] GetSearchQueries(ITestContext context) throws Exception {
 
/* Need to rework on this. I need to have only one single db having test cases from all types of Search. 
   I need to make this code work in such a way that it there will be test classes for all the 3 types of Search. 
   Also during generation of test cases I need to make sure that each type has been named as feature. 
*/
        //SearchFeature is the one which is present in the FeatureName column of DM_MasterModelxml_REF table.        
        
        
        String SearchFeature = context.getCurrentXmlTest().getParameter("SearchFeature"); 
    	if(SearchFeature == null) {SearchFeature="Search";} //Use this when executing All types of Search
        
 //       if(SearchFeature == null) {SearchFeature="AllProcessedSearch";} //Use this when executing specific type of Search
        chps.getGids(SearchFeature);
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~        
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
    public void testCompareDSSearchResults(String parm1, Object parm2) throws Exception {
        System.out.println("Inside the testCompare method");
        chps.compare_hped_kwys(parm1);
       // Assert.assertEquals(true, true, cmd.metadata_equall);

    }
    
   /* @AfterMethod
    public void ac() throws IOException
    {
        chps.quitphantomjs();
    } */
    
    @AfterSuite
    
    public void AClass()
    {
        System.out.println("This is @Aftersuite statement");
    }
}
