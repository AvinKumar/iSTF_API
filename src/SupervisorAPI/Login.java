package SupervisorAPI;

import Log4j.PropConfigurator;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

public class Login {

    static Logger log = Logger.getLogger(Login.class.getName());
    public CookieStore cookieStore = null;
    public HttpClientContext localContext = null;

    public void login(String url, String userName, String password) throws Exception {
        
        PropConfigurator.configure();
        
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {

            cookieStore = new BasicCookieStore();
//            cookieStore.addCookie(JSESSIONID);
            localContext = HttpClientContext.create();
            localContext.setCookieStore(cookieStore);
            List<NameValuePair> params = new LinkedList<NameValuePair>();
            //params.add(new BasicNameValuePair("%3FFcookiesEnabled", "true"));
            params.add(new BasicNameValuePair("cookiesEnabled", "true"));
            params.add(new BasicNameValuePair("login", "Login"));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("substitute", ""));
            params.add(new BasicNameValuePair("username", userName));

            String paramString = URLEncodedUtils.format(params, "utf-8");

            // method=login&password=admin&username=admin
            url += paramString;

            //url=URLDecoder.decode( url, "UTF-8" );
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget, localContext);

            if (response.getStatusLine().getStatusCode() == 200) {
                log.info("Login Succes : "
                        + response.getStatusLine().getStatusCode());
            }

        } catch (Exception e) {
            log.error("Exception in Login" + e.getMessage());
        }

    }

}
