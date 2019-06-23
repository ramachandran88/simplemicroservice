package com.ram.db;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.ram.domain.Account;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.Properties;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class DbModule extends AbstractModule {

    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(DbModule.class);

    public void configure() {

    }

    @Provides
    @Singleton
    public SessionFactory provideSessionFactory() {
        try {
            Properties properties = new Properties();
            //provide the required properties
            properties.load(getClass().getClassLoader().getResourceAsStream("db.properties"));

            //create a configuration
            Configuration configuration = new Configuration();
            //provide all properties to this configuration
            configuration.setProperties(properties);
            //add classes which are mapped to database tables.
            configuration.addAnnotatedClass(Account.class);
            //initialize session factory
            return configuration.buildSessionFactory();
        }
        catch (Exception e){
            logger.error("error is ", e);
            return null;
        }
    }

    @Provides
    public EntityManager provideEntityManager(SessionFactory sessionFactory) {
        EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
        if (entityManager == null) {
            ENTITY_MANAGER_CACHE.set(entityManager = sessionFactory.createEntityManager());
        }
        return entityManager;
    }

}
