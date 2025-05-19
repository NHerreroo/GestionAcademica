package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Map;

public class AcademiaDAOFactory {

    private static final String PERSISTENCE_UNIT_NAME = "GestionAcademica";
    private static EntityManagerFactory emf;

    public static AcademiaDAO getAcademiaDAO() {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            Map<String, Object> properties = emf.getProperties();
            
            // Get provider type with default value "jpa" if not specified
            String provider = (String) properties.get("jakarta.persistence.provider");
            if (provider == null) {
                // Try legacy property name as fallback
                provider = (String) properties.get("hibernate.connection.provider_class");
                if (provider == null) {
                    provider = "jpa"; // Default to JPA if no provider specified
                }
            }
            
            if ("jdbc".equalsIgnoreCase(provider)) {
                String url = (String) properties.get("jakarta.persistence.jdbc.url");
                if (url == null) {
                    // Try legacy property name as fallback
                    url = (String) properties.get("hibernate.connection.url");
                }
                return new AcademiaDAOImplJDBC(url);
            } else {
                return new AcademiaDAOImplJPA(emf);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al crear DAO: " + e.getMessage(), e);
        }
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}