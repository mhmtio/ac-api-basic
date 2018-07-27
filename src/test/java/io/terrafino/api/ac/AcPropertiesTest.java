package io.terrafino.api.ac;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AcPropertiesTest {

    private static Properties properties;

    @BeforeClass
    public static void beforeClass() {
        properties = new Properties();
    }

    @Test
    public void canGetHost() throws Exception {
        properties.setProperty("host", "HOST");
        AcProperties acProperties = AcProperties.fromProperties(AcPropertiesTest.properties);
        assertThat(acProperties.getHost(), is("HOST"));
    }

    @Test
    public void canGetInstallation() throws Exception {
        properties.setProperty("installation", "INSTALLATION");
        AcProperties acProperties = AcProperties.fromProperties(AcPropertiesTest.properties);
        assertThat(acProperties.getInstallation(), is("INSTALLATION"));
    }

    @Test
    public void canGetUser() throws Exception {
        properties.setProperty("user", "USER");
        AcProperties acProperties = AcProperties.fromProperties(AcPropertiesTest.properties);
        assertThat(acProperties.getUser(), is("USER"));
    }

    @Test
    public void canGetPassword() throws Exception {
        properties.setProperty("password", "PASSWORD");
        AcProperties acProperties = AcProperties.fromProperties(AcPropertiesTest.properties);
        assertThat(acProperties.getPassword(), is("PASSWORD"));
    }

}