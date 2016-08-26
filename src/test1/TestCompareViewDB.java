/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1;
import DBComparison.CompareDBForView;
import SupervisorAPI.*;
import Database.*;
import atu.testng.reports.ATUReports;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebDriver;
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

/**
 *
 * @author srivathg
 */
public class TestCompareViewDB
{
    CompareDBForView cdbv = new CompareDBForView();
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TestDSSearchResults.class.getName());
    
    
    @Test
    public void testCompareDBMethod()
    {
        try
        {
       cdbv.executeDBView();
        }
        catch (Exception e)
        {
            System.out.println("-- - - - - - - - - - - - - - - - - -  Some problem in executin executeDBView");
            System.out.println(e.getMessage());
            log.info("Problem in executing executeDBView");
        }
    }
}
