package io.terrafino.api.ac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AcProperties {

    private static Logger log = LoggerFactory.getLogger(AcProperties.class);

    private String host;
    private String installation;
    private String user;
    private String password;

    public static AcProperties fromResource(String filename) {
        try (InputStream inputStream = AcProperties.class.getClassLoader().getResourceAsStream(filename)) {
            Properties prop = new Properties();
            prop.load(inputStream);
            return fromProperties(prop);
        } catch (IOException ex) {
            log.error("Could not read AC properties from file " + filename + "!", ex);
        }
        return null;
    }

    public static AcProperties fromProperties(Properties properties) {
        AcProperties instance = new AcProperties();
        instance.host = properties.getProperty("host");
        instance.installation = properties.getProperty("installation");
        instance.user = properties.getProperty("user");
        instance.password = properties.getProperty("password");
        return instance;
    }

    public String getHost() {
        return host;
    }

    public String getInstallation() {
        return installation;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
