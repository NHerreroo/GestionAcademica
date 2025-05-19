package dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AcademiaDAOFactory {
    
    private static final String CONFIG_FILE = "/config.properties";
    private static String persistenceProvider;
    
    static {
        try (InputStream input = AcademiaDAOFactory.class.getResourceAsStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            prop.load(input);
            persistenceProvider = prop.getProperty("persistence.provider", "jdbc").toLowerCase();
        } catch (IOException ex) {
            System.err.println("Error al cargar el archivo de configuración. Usando JDBC por defecto.");
            persistenceProvider = "jdbc";
        }
    }
    
    public static AcademiaDAO getAcademiaDAO() {
        switch (persistenceProvider) {
            case "jpa":
                return new AcademiaDAOImplJPA();
            case "jdbc":
            default:
                return new AcademiaDAOImplJDBC();
        }
    }
}