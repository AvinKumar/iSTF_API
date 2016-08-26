


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1;
import SupervisorAPI.CompareCompSearch;
import SupervisorAPI.Login;
import SupervisorAPI.*;
import atu.testng.reports.ATUReports;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ReadPropertiespkg.ReadProperties;
import org.testng.ITestContext;
//import static SupervisorAPI.CompareCompSearch.l1;
//import static SupervisorAPI.CompareCompSearch.CSRFToken;


/**
 *
 * @author Avin
 */
//@Listeners({ATUReportsListener.class, ConfigurationListener.class,
//    MethodListener.class})

public class TestCompSearchResults {

    CompareCompSearch chps = new CompareCompSearch();
// static   
//              {
//System.setProperty("atu.reporter.config", "D:\\atu.properties");
//}

//--------------------------------------------------------------------------------------------------------------    
// Enable the below code iff you want to call the login method only once. 
// To make this method work few more changes needs to be made in CompareCompSearch.java
    
//	@BeforeTest
//	public void logintoSupervisor()
//   {
//        ReadProperties rp = new ReadPropertiespkg.ReadProperties();
//		//Login l1;
//		l1 = new Login();
//        try {
//            l1.login(rp.getProperty("LoginRequest"), rp.getProperty("username"), rp.getProperty("password"));
//			System.out.println("################Successfully logged in############");
//        } catch (Exception ex) {
//            System.out.println("Exception in logging to Supervisor system" + ex.getMessage());
//        }
//	}
        
//----------------------------------------------------------------------------------------------------------------

    @DataProvider(name = "test1")
    public Object[][] GetViewsCases(ITestContext context) throws Exception {
    
/* Need to rework on this. I need to have only one single db having test cases from all 12 different types of Views. 
   I need to make this code work in such a way that it there will be test classes for all the 12 views type. 
   Also during generation of test cases I need to make sure that each type has been named as feature. 
*/
        //ViewsFeature is the one which is present in the FeatureName column of DM_MasterModelxml_REF table.
        String ViewsFeature = context.getCurrentXmlTest().getParameter("ViewsFeature");  	
    	if(ViewsFeature == null) {ViewsFeature="Views";} //Use this when executing All types of Views
        
   //     if(ViewsFeature == null) {ViewsFeature="GlobalOnDemandInteractiveAllProcessedViews";} //Use this when executing specific type of Views
        chps.getGids(ViewsFeature);
        
        Object[][] obj = new Object[chps.kwys_search_List.size()][2];
        for (int i = 0; i < chps.kwys_search_List.size(); i++) {
            String temp_ar[] = chps.kwys_search_List.get(i).split(",");
            obj[i] = new Object[]{chps.kwys_search_List.get(i),"Test for Metadata Verification for Document " +i};
            obj[i] = new Object[]{temp_ar[0], temp_ar[1]};
            System.out.println(obj[i]);
        }
        return obj;
    }

    @Test(dataProvider = "test1")
    public void testCompareCompSearchResults(String parm1, Object parm2) throws Exception {
        chps.compare_comp_search(parm1);
       // Assert.assertEquals(true, true, cmd.metadata_equall);

    }

   
}
