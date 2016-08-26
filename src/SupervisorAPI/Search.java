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
import ReadPropertiespkg.ReadProperties;
import static SupervisorAPI.Login.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
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
/*import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;*/

/**
 * This example demonstrates the use of a local HTTP context populated with
 * custom attributes.
 */
public class Search {

    public static int i = 0;
    public static int cou = 0;
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Search.class.getName());
    ReadProperties rp = new ReadProperties();


    public void ExecuteSearch(String path, Login login_obj,long waitTime) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();

       // log.info("GET request==>" + path);
        HttpGet httpget = new HttpGet(path);
        StringBuffer result = new StringBuffer();
        try // Adding this try block to catch various exceptions thrown in Archive Search execution. - Gokul
        {
            
            HttpResponse response = httpclient.execute(httpget, login_obj.localContext);
            Thread.sleep(waitTime);
            if (response.getStatusLine().getStatusCode() == 200) {
                log.info("Query Execution Succes : "
                        + response.getStatusLine().getStatusCode());
                                            System.out.println("Query executed successfully");
                                            System.out.println("============================\n");
            }
            else
            {
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                System.out.println(response.getStatusLine().getReasonPhrase());
            }
		
            Thread.sleep(2000);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

           // StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);

            }
           // System.out.println("Response Results--->" + result); if we want we can print this response result - Gokul
        }
        
        catch(NullPointerException npe)
        {
            System.out.println(npe.getMessage());
            System.out.println("_____________________________________________________");
            System.out.println();
            result.append("NullPointer Exception");
            System.out.println("_____________________________________________________");
             System.out.println();
        }
        
        catch(EOFException eofe)
        {
            System.out.println(eofe.getMessage());
            System.out.println("_____________________________________________________");
            System.out.println();
            result.append("End of File Exception");
            System.out.println("_____________________________________________________");
             System.out.println();
        }
        
       //System.out.println("Response Results--->" + result);

        BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(rp.getProperty("StorePath"))));
        //write contents of StringBuffer to a file
        bwr.write(result.toString());

        //flush the stream
        bwr.flush();

        //close the stream
        bwr.close();

        System.out.println("Content of StringBuffer written to File.\n");
		System.out.println("________________________________\n");

    }

}
