package com.milou;

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
                        // SEND
                    }
                    case "V" -> {
                        // VIEW
                    }
                    case "R" -> {
                        // REPLY
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
}