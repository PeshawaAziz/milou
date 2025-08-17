package com.milou.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.milou.HibernateSessionFactory;
import com.milou.model.Email;
import com.milou.model.EmailRecipient;
import com.milou.model.User;

public class EmailService {
    private final SessionFactory sessionFactory = HibernateSessionFactory.get();
    private final Random random = new Random();
    private final String chars = "abcdefghijklmnopqrstuvwxyz0123456789";

    public String sendEmail(User sender, ArrayList<String> recipientEmails, String subject, String body) {
        ArrayList<User> recipients = new ArrayList<>();

        for (String email : recipientEmails) {
            User user = new UserService().findByEmail(email.trim());
            if (user == null)
                throw new IllegalArgumentException("User " + email + " not found.");

            recipients.add(user);
        }

        if (recipients.isEmpty())
            throw new IllegalArgumentException("No recipients specified.");

        return sessionFactory.fromTransaction(session -> {
            Email email = new Email();
            email.setSender(sender);
            email.setSubject(subject);
            email.setBody(body);
            email.setSentDate(new Date());
            email.setCode(generateUniqueCode(session));
            session.persist(email);
            for (User r : recipients) {
                EmailRecipient er = new EmailRecipient(email, r);
                session.persist(er);
            }
            return email.getCode();
        });
    }

    private String generateUniqueCode(Session session) {
        while (true) {
            StringBuilder sb = new StringBuilder(6);

            for (int i = 0; i < 6; i++)
                sb.append(chars.charAt(random.nextInt(chars.length())));

            String code = sb.toString();
            Email email = session.createQuery("from Email where code = :code", Email.class)
                    .setParameter("code", code)
                    .uniqueResult();
            if (email == null)
                return code;
        }
    }
}