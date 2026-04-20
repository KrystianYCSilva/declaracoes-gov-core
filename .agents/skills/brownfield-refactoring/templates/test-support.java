/*
 * Add these methods to the project's EXISTING HibernateUtil class.
 * This allows tests to swap the production DB config for H2.
 *
 * Usage in tests:
 *   @BeforeAll
 *   static void setup() {
 *       HibernateUtil.initForTest("hibernate-test.cfg.xml");
 *   }
 */

// --- Add to HibernateUtil.java ---

public static synchronized void initForTest(String configFile) {
    shutdown();
    Configuration cfg = new Configuration().configure(configFile);
    sessionFactory = cfg.buildSessionFactory();
}

public static synchronized void shutdown() {
    if (sessionFactory != null && !sessionFactory.isClosed()) {
        sessionFactory.close();
    }
    sessionFactory = null;
}


/*
 * HibernateTestSupport.java — Base class for entity integration tests.
 * Creates its own SessionFactory (does NOT use HibernateUtil).
 * Each test runs in a transaction that is rolled back automatically.
 *
 * Place at: src/test/java/{your-package}/support/HibernateTestSupport.java
 *
 * Usage:
 *   class MyEntityTest extends HibernateTestSupport {
 *       @Test void testPersist() {
 *           MyEntity e = new MyEntity();
 *           e.setName("test");
 *           session.persist(e);
 *           session.flush();
 *           assertNotNull(e.getId());
 *       }
 *   }
 */

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

public abstract class HibernateTestSupport {

    protected static SessionFactory sf;
    protected Session session;

    @BeforeAll
    static void initH2() {
        sf = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
    }

    @AfterAll
    static void closeH2() {
        if (sf != null) sf.close();
    }

    @BeforeEach
    void openSession() {
        session = sf.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void rollback() {
        if (session != null && session.getTransaction().isActive()) {
            session.getTransaction().rollback();
        }
        if (session != null) session.close();
    }
}
