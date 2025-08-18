package com.milou.service;

import org.hibernate.SessionFactory;

import com.milou.HibernateSessionFactory;
import com.milou.model.User;

public class UserService {
    private final SessionFactory sessionFactory = HibernateSessionFactory.get();

    public User signup(String name, String email, String password) {
        if (password.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters.");

        String normalizedEmail = normalizeEmail(email);

        return sessionFactory.fromTransaction(session -> {
            User existing = session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", normalizedEmail)
                    .uniqueResult();
            if (existing != null)
                throw new IllegalArgumentException("Email already exists.");

            User user = new User(name, normalizedEmail, password);
            session.persist(user);

            return user;
        });
    }

    public User login(String email, String password) {
        String normalizedEmail = normalizeEmail(email);

        return sessionFactory.fromTransaction(session -> {
            User user = session.createQuery("from User where email = :email and password = :password", User.class)
                    .setParameter("email", normalizedEmail)
                    .setParameter("password", password)
                    .uniqueResult();
            if (user == null)
                throw new IllegalArgumentException("Invalid email or password.");

            return user;
        });
    }

    public User findByEmail(String email) {
        String normalizedEmail = normalizeEmail(email);

        return sessionFactory
                .fromTransaction(session -> session.createQuery("from User where email = :email", User.class)
                        .setParameter("email", normalizedEmail)
                        .uniqueResult());
    }

    private String normalizeEmail(String input) {
        if (!input.contains("@"))
            return input + "@milou.com";
        return input;
    }
}