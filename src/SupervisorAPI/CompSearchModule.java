package SupervisorAPI;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sayed
 */
import Database.databaseConnection;
import Log4j.PropConfigurator;
import ReadPropertiespkg.ReadProperties;
import static SupervisorAPI.Login.log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/*import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;*/

/**
 * This example demonstrates the use of a local HTTP context populated with
 * custom attributes.
 */
public class CompSearchModule {

    public static int i = 0;
    public static int cou = 0;
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CompSearchModule.class.getName());
    ReadProperties rp = new ReadProperties();
    ComprehensiveSearch CompSearch= new ComprehensiveSearch();
    
    databaseConnection sqldb=new databaseConnection();

    public void ExecuteSearch(String path, Login login_obj) throws Exception {
        
        PropConfigurator.configure();

        CloseableHttpClient httpclient = HttpClients.createDefault();

       
       // path=URLEncoder.encode(path,"utf-8");
         log.info("GET request==>" + path);
        HttpGet httpget = new HttpGet(path);
        //HttpPost htpost=new HttpPost(path);
        HttpResponse response = httpclient.execute(httpget, login_obj.localContext);
        if (response.getStatusLine().getStatusCode() == 200) {
            log.info("Query Execution Succes : "
                    + response.getStatusLine().getStatusCode());
        }
        
//        sqldb.dbConnect("jdbc:sqlserver:"+rp.getProperty("sqlhostanddb"), rp.getProperty("sqlusername"), rp.getProperty("sqlpassword"));
//        CompSearch.CompSearch(item);
//        System.out.println("Item in search Module"+item);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);

        }
        System.out.println("Response Results:\n" + result);

//        BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(rp.getProperty("StorePath"))));
//        //write contents of StringBuffer to a file
//        bwr.write(result.toString());
//
//        //flush the stream
//        bwr.flush();
//
//        //close the stream
//        bwr.close();
//
//        System.out.println("Content of StringBuffer written to File.");

    }

    String getCSRF( Login login_obj) throws Exception
     {
         PropConfigurator.configure();
         CloseableHttpClient httpclient = HttpClients.createDefault();

       // log.info("GET request==>" + path);
        String CSRFToken = null;
        String responseString = null;
        String strlogin[] = rp.getProperty("url").split("logon");
        String strviewReq = strlogin[0] + "views";
      //  HttpGet httpget = new HttpGet(path);
        HttpGet httpget = new HttpGet(strviewReq);
        HttpResponse response = httpclient.execute(httpget, login_obj.localContext);
        
        response.getHeaders(strviewReq);
        if (response.getStatusLine().getStatusCode() == 200) {
            log.info("Query Execution Succes : "
                    + response.getStatusLine().getStatusCode());
             responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            
            
        }
      
          Document doc = Jsoup.parseBodyFragment(responseString);
          Element  body = doc.body();
          try
          {
             CSRFToken = body.children().html().split("CSRFTOKEN")[2].split("value=")[1].split("\n")[0].substring(1, 17);
          }          
          catch(IndexOutOfBoundsException ex)
          {
            log.error("Error in getting search query by gid" + ex.getMessage());
            System.out.println("Element not found" + ex.getMessage());
          }
          catch(Exception e)
          {
              log.error("Error in getting search query by gid" + e.getMessage());
              System.out.println("CSRFToken not found" + e.getMessage());
          }
          return CSRFToken;
    }
    
    
}
