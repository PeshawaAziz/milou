package com.milou.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.milou.HibernateSessionFactory;
import com.milou.model.Email;
import com.milou.model.EmailRecipient;
import com.milou.model.User;

public class EmailService {
    private final SessionFactory sessionFactory = HibernateSessionFactory.get();
    private final Random random = new Random();
    private final String chars = "abcdefghijklmnopqrstuvwxyz0123456789";

    public String sendEmail(User sender, List<String> recipientEmails, String subject, String body) {
        List<User> recipients = new ArrayList<>();

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

    public List<String> getUnreadEmails(User user) {
        return sessionFactory.fromTransaction(session -> {
            Query<EmailRecipient> query = session.createQuery(
                    "from EmailRecipient er where er.recipient = :user and er.isRead = false order by er.email.sentDate desc",
                    EmailRecipient.class);
            query.setParameter("user", user);
            return query.list().stream()
                    .map(er -> "+ " + er.getEmail().getSender().getEmail() + " - " + er.getEmail().getSubject() + " ("
                            + er.getEmail().getCode() + ")")
                    .collect(Collectors.toList());
        });
    }

    public List<String> getAllEmails(User user) {
        return sessionFactory.fromTransaction(session -> {
            Query<EmailRecipient> query = session.createQuery(
                    "from EmailRecipient er where er.recipient = :user order by er.email.sentDate desc",
                    EmailRecipient.class);
            query.setParameter("user", user);
            return query.list().stream()
                    .map(er -> "+ " + er.getEmail().getSender().getEmail() + " - " + er.getEmail().getSubject() + " ("
                            + er.getEmail().getCode() + ")")
                    .collect(Collectors.toList());
        });
    }

    public List<String> getSentEmails(User user) {
        return sessionFactory.fromTransaction(session -> {
            Query<Email> query = session.createQuery(
                    "from Email e where e.sender = :user order by e.sentDate desc", Email.class);
            query.setParameter("user", user);
            List<Email> emails = query.list();
            List<String> result = new ArrayList<>();
            for (Email e : emails) {
                String recips = e.getRecipients().stream()
                        .map(er -> er.getRecipient().getEmail())
                        .collect(Collectors.joining(", "));
                result.add("+ " + recips + " - " + e.getSubject() + " (" + e.getCode() + ")");
            }
            return result;
        });
    }

    public String readEmailByCode(User user, String code) {
        return sessionFactory.fromTransaction(session -> {
            Email email = session.createQuery("from Email where code = :code", Email.class)
                    .setParameter("code", code)
                    .uniqueResult();
            if (email == null) {
                throw new IllegalArgumentException("Email not found.");
            }

            boolean isSender = email.getSender().equals(user);
            EmailRecipient er = session
                    .createQuery("from EmailRecipient er where er.email = :email and er.recipient = :user",
                            EmailRecipient.class)
                    .setParameter("email", email)
                    .setParameter("user", user)
                    .uniqueResult();
            if (!isSender && er != null && !er.isRead()) {
                er.setRead(true);
                session.merge(er);
            }

            return formatEmail(email, isSender);
        });
    }

    private String formatEmail(Email email, boolean isSender) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        sb.append("Code: ").append(email.getCode()).append("\n");

        if (isSender) {
            String recips = email.getRecipients().stream()
                    .map(EmailRecipient::getRecipient)
                    .map(User::getEmail)
                    .collect(Collectors.joining(", "));
            sb.append("Recipient(s): ").append(recips).append("\n");
        }

        sb.append("Subject: ").append(email.getSubject()).append("\n")
                .append("Date: ").append(sdf.format(email.getSentDate())).append("\n\n")
                .append(email.getBody());

        return sb.toString();
    }
}