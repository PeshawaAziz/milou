package com.milou;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.milou.model.User;
import com.milou.service.EmailService;
import com.milou.service.UserService;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final EmailService emailService = new EmailService();
    private static User currentUser;

    public static void main(String[] args) {
        while (true) {
            System.out.print("[L]ogin, [S]ign up: ");
            String choice = in.nextLine().trim().toUpperCase();
            try {
                switch (choice) {
                    case "L" -> {
                        login();
                    }
                    case "S" -> {
                        signup();
                    }
                    default -> System.out.println("Invalid choice.");
                }
                if (currentUser != null)
                    userMenu();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private static void login() {
        System.out.print("Email: ");
        String email = in.nextLine().trim();
        System.out.print("Password: ");
        String password = in.nextLine().trim();
        currentUser = userService.login(email, password);
        System.out.println("Welcome back, " + currentUser.getName() + "!");
    }

    private static void signup() {
        System.out.print("Name: ");
        String name = in.nextLine().trim();
        System.out.print("Email: ");
        String email = in.nextLine().trim();
        System.out.print("Password: ");
        String password = in.nextLine().trim();
        userService.signup(name, email, password);
        System.out.println("Your new account is created.");
        System.out.println("Go ahead and login!");
    }

    private static void userMenu() {
        // print unread emails

        while (true) {
            System.out.print("[S]end, [V]iew, [R]eply, [F]orward: ");
            String choice = in.nextLine().trim().toUpperCase();
            try {
                switch (choice) {
                    case "S" -> {
                        sendEmail();
                    }
                    case "V" -> {
                        viewEmails();
                    }
                    case "R" -> {
                        replyEmail();
                    }
                    case "F" -> {
                        // FORWARD
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void sendEmail() {
        System.out.print("Recipient(s): ");
        List<String> recipients = Arrays.asList(in.nextLine().trim().split(","));
        System.out.print("Subject: ");
        String subject = in.nextLine().trim();
        System.out.print("Body: ");
        String body = in.nextLine().trim();
        String code = emailService.sendEmail(currentUser, recipients, subject, body);
        System.out.println("Successfully sent your email.");
        System.out.println("Code: " + code);
    }

    private static void viewEmails() {
        System.out.print("[A]ll emails, [U]nread emails, [S]ent emails, Read by [C]ode: ");
        String choice = in.nextLine().trim().toUpperCase();
        switch (choice) {
            case "A" -> {
                System.out.println("All Emails:");
                emailService.getAllEmails(currentUser).forEach(System.out::println);
            }
            case "U" -> {
                System.out.println("Unread Emails:");
                emailService.getUnreadEmails(currentUser).forEach(System.out::println);
            }
            case "S" -> {
                System.out.println("Sent Emails:");
                emailService.getSentEmails(currentUser).forEach(System.out::println);
            }
            case "C" -> {
                System.out.print("Code: ");
                String code = in.nextLine().trim();
                String content = emailService.readEmailByCode(currentUser, code);
                System.out.println(content);
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void replyEmail() {
        System.out.print("Code: ");
        String code = in.nextLine().trim();
        System.out.print("Body: ");
        String body = in.nextLine().trim();
        String newCode = emailService.replyToEmail(currentUser, code, body);
        System.out.println("Successfully sent your reply to email " + code + ".");
        System.out.println("Code: " + newCode);
    }

}