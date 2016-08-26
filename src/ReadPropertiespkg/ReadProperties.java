package ReadPropertiespkg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ReadProperties {

    public final Properties configProp = new Properties();

    public ReadProperties() {

        try {
            FileInputStream in = new FileInputStream("conf/APITest.properties");
            //FileInputStream in = new FileInputStream("conf/SearchTest.properties");

            System.out.println("Read all properties from APITest Properties file");
            if (in == null) {
                System.out.println("Please check, unable to find APITest Properties file name");
            }

            configProp.load(in);

        } catch (IOException e) {
            System.out.println("Error in loading proerties" + e.getMessage());
        }
    }

    //Bill Pugh Solution for singleton pattern
    public static class LazyHolder {

        private static final ReadProperties INSTANCE = new ReadProperties();
    }

    public static ReadProperties getInstance() {
        return LazyHolder.INSTANCE;
    }

    public String getProperty(String key) {
        return configProp.getProperty(key);
    }

    public Set<String> getAllPropertyNames() {
        return configProp.stringPropertyNames();
    }

    public boolean containsKey(String key) {
        return configProp.containsKey(key);
    }

}
