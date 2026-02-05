package com.learn.util;

import com.learn.entity.User;
import com.learn.exception.HibernateConfigurationException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    private HibernateUtil() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Properties properties = ConfigLoader.loadHibernateProperties();

                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .applySettings(properties)
                        .build();

                MetadataSources sources = new MetadataSources(registry);
                sources.addAnnotatedClass(User.class);

                Metadata metadata = sources.buildMetadata();
                sessionFactory = metadata.buildSessionFactory();
                logger.info("Hibernate SessionFactory created successfully");
            } catch (RuntimeException e) {
                logger.error("Error creating SessionFactory", e);
                throw new HibernateConfigurationException("Hibernate configuration error", e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            logger.info("Hibernate SessionFactory closed");
        }
    }
}