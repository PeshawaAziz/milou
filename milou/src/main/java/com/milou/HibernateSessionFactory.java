package com.milou;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
    private static SessionFactory sessionFactory = null;

    public static SessionFactory get() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure("resources/hibernate.cfg.xml")
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}