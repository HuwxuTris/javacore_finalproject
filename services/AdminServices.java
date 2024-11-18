package Midterm.services;

import java.util.Scanner;

public class AdminServices {
    private static AdminServices instance;
    private AdminServices() {
    }

    public static synchronized AdminServices getInstance() {
        if (instance == null) {
            instance = new AdminServices();
        }
        return instance;
    }
    public void createTeacherAccount(Scanner scanner) {
        AccountServices accountServices = AccountServices.getInstance();
        System.out.println("Welcome to Register:");
        System.out.print("Enter teacher's username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        accountServices.registerUser(username, password, email, 1);
    }
}
