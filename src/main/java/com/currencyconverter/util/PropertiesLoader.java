package main.java.com.currencyconverter.util;

import main.java.com.currencyconverter.exceptions.CurrencyConverterException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertiesLoader {
    private static final Logger log = Logger.getLogger(PropertiesLoader.class.getName());

    public static Properties loadProperties(String resourceFileName) throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream(resourceFileName);
        configuration.load(inputStream);
        inputStream.close();
        return configuration;
    }

    public static String getFromProperties(String property) {
        try {
            return loadProperties("application.properties").getProperty(property);
        } catch (IOException e) {
            throw new CurrencyConverterException("Error Loading " + property);
        }
    }
}
