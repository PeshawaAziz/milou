package com.milou;

import java.util.Scanner;

import com.milou.service.UserService;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        while (true) {
            System.out.print("[L]ogin, [S]ign up: ");
            String choice = in.nextLine().trim().toUpperCase();
            try {
                switch (choice) {
                    case "L" -> {
                        // LOGIN
                    }
                    case "S" -> {
                        signup();
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
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

}