package com.milou;

import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);

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
                        // SIGNUP
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}