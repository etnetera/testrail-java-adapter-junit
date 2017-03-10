package cz.etnetera.testrail.adapter.junit;

import java.io.IOException;
import java.util.Properties;

/**
 *  Singleton to access Testrail related properties. First tries to load value from System properties, than resources/testrail.properties.
 */
public class TRPropertyLoader {

    private static TRPropertyLoader loader;

    public static TRPropertyLoader getInstance(){
        if (loader == null) loader = new TRPropertyLoader();
        return loader;
    }

    private TRPropertyLoader() {
    }
    
    private Properties systemProperties = System.getProperties();

    private Properties fileProperties = new Properties();

    public String getProperty(String key) {
        if (fileProperties.isEmpty()) {
            try {
                fileProperties.load(TRPropertyLoader.class.getClassLoader().getResourceAsStream("testrail.properties"));
            } catch (IOException e) {
                throw new RuntimeException("Error while loading file resources/testrail.properties");
            }
        }
        String value = systemProperties.getProperty(key) != null ? System.getProperty(key) : fileProperties.getProperty(key);
        if (value == null) throw new NullPointerException(key + " was not specified neither in resources/testrail.properties nor as system property");
        return value;
    }

    public String getUrl() {
        return getProperty("testrail.url");
    }

    public String getUsername() {
        return getProperty("testrail.username");
    }

    public String getPassword() {
        return getProperty("testrail.password");
    }

    public int getProjectid() {
        return Integer.valueOf(getProperty("testrail.projectid"));
    }


}
