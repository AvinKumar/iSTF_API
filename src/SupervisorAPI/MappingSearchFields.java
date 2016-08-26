/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SupervisorAPI;

//import GenerateKWYG.Properties1;
import Log4j.PropConfigurator;
import ReadPropertiespkg.ReadProperties;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author yanamalp
 */
public class MappingSearchFields {

    public static Map<String, String> Locators = new HashMap<String, String>();
    // public static   ArrayList<String> Verifications=new ArrayList<String>();
    public static boolean verfiy = false;
    public static int inc = 0;

   /* public static void main(String ar[]) throws ClassNotFoundException, SQLException, IOException {
        
        PropConfigurator.configure();

        Connection connection = null;
        Statement statement = null;
        ReadProperties rp = new ReadProperties();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + rp.getProperty("searchdb_Location"));

            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT GID,TESTCASE FROM DM_TESTCASE");
            while (rs.next()) {

                maptestcasesapi(rs.getString("TESTCASE"));

                System.out.println("Done for" + rs.getString("GID"));
                System.out.println("**********************************************************");

            }
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
        } catch (SQLException e) {

        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

    } */

    public static String maptestcasesapi(String Testcase) throws IOException {
        System.out.println("_________________________________________________________________");
        System.out.println("Inside MappingTestCaseAPI Method");
        System.out.println("_________________________________________________________________");
        PropConfigurator.configure();
        
        Map<String, String> Locators1;
        Map<String, Map<String, String>> mapping_list = new HashMap<>();
        //  Locators1 = GetLocators("C:\\Users\\yanamalp\\Desktop\\DS_Search.xml");
        String typeone = null, typeonevalue = null, within = null, withinvalue = null, httpgetrequest = null;
        StringBuilder out = new StringBuilder();
        String paramstring;
        File f = null;
        ReadProperties rp = new ReadProperties();
        String tempLocation = rp.getProperty("tempfile_Location");

        // creates temporary file
        f = File.createTempFile("tmp", ".xml", new File(tempLocation));

        // prints absolute path
        //    System.out.println("File path: "+f.getAbsolutePath());
        FileWriter fw = new FileWriter(f);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        //  System.out.println("****Testcases_path"+Testcase);
        fw.write(Testcase);
        fw.close();
        //fw=null;

        String path = f.getAbsolutePath();
        //System.out.println("Path----------------------------- is " +path);
        Locators.clear();
        Locators1 = GetLocators(path);

        for (String key : Locators1.keySet()) {
            // System.out.println("%%%%%%%%%%%%%%key%%%%%%%%%%%%%%%%%" + key);
            String temp_ar[] = key.split("_");

            String parent = temp_ar[0];
            String child = temp_ar[1];
            String UI_object = temp_ar[2];

            if (!mapping_list.containsKey(parent)) {
                // System.out.println("parent"+parent);
                Map<String, String> innerMap = mapping_list.get(key);
                if (innerMap == null) {
                    mapping_list.put(parent, innerMap = new HashMap<>()); // Java version >= 1.7
                }
                innerMap.put(child + ":" + UI_object, Locators1.get(key));

                mapping_list.put(parent, innerMap);
            } else {
                Map<String, String> innerMap = mapping_list.get(parent);
                innerMap.put(child + ":" + UI_object, Locators1.get(key));
                mapping_list.put(parent, innerMap);
                //  mapping_list.put(parent, mapping_list.get(parent)+ "^"+child +":"+ UI_object + ":"+ Locators1.get(key));
            }
        }

        Locators.clear();
        Map<String, String> mapping_api_list = GetLocators(rp.getProperty("MappingFile"));
        Map<String, String> temp_mapping_list = new HashMap<String, String>();

        for (String api_key : mapping_api_list.keySet()) {
            if (mapping_list.get(api_key) != null) {

                String Key = api_key;

                String keyvalue = mapping_list.get(api_key).toString();
                // keyvalue = keyvalue.replaceAll("\\{", "").replaceAll("\\}", "");
                String[] value = keyvalue.split("=");

                temp_mapping_list.put(Key + "," + mapping_api_list.get(Key), value[1].substring(0, value[1].length() - 1));

            }
        }

        for (String api_key : temp_mapping_list.keySet()) {
 //if (mapping_list.get(mapping_api_list.get(api_key)) != null) {

            String[] actual_apikey = api_key.split(",");

//            if (temp_mapping_list.get(api_key) != null) {
//            // System.out.println("***"+api_key);
           // System.out.println("Key ::+ " + actual_apikey[1] + "  ::" + temp_mapping_list.get(api_key)); // It was printed before but commented by gokul

            paramstring = "&" + actual_apikey[1] + "=" + temp_mapping_list.get(api_key);
            out = out.append(paramstring);

        }

       // System.out.print("accUsers-matchingType=2" + out.toString());

        return out.toString();

    }

    public static Map<String, String> GetLocators(String Testcases_path) {
        
        PropConfigurator.configure();
        
        //   ReadTestCases.verfiy=verification;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc;

            // deletes file when the virtual machine terminate
            doc = documentBuilder.parse(new File(Testcases_path));
            //   doc = documentBuilder.parse(f);
            getData(null, doc.getDocumentElement());

        } catch (Exception exe) {
            exe.printStackTrace();
        }
        Map<String, List<String>> actions_exp = new HashMap<String, List<String>>();

        return Locators;

    }

    public static void getData(Node parentNode, Node node) {
        
        PropConfigurator.configure();

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE: {
                if (node.toString().contains("Verification")) {
                    System.out.println();
                    System.out.println("******" + node.toString());
                    System.out.println();
                    verfiy = true;
                }
                if (node.hasChildNodes()) {
                    NodeList list = node.getChildNodes();
                    int size = list.getLength();

                    for (int index = 0; index < size; index++) {
                        getData(node, list.item(index));
                    }
                }

                break;
            }

            case Node.TEXT_NODE: {
                String data = node.getNodeValue();

                if (data.trim().length() > 0) {
                    Locators.put(parentNode.getNodeName(), node.getNodeValue());
                }
                break;
            }

        }
    }
}
