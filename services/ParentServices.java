package Midterm.services;

import Midterm.database.Database;
import Midterm.entities.Parent;
import Midterm.entities.Student;

import java.util.Scanner;

public class ParentServices {
    private static ParentServices instance;

    private ParentServices() {}

    public static synchronized ParentServices getInstance() {
        if (instance == null) {
            instance = new ParentServices();
        }
        return instance;
    }

    public void addParentDetails(String username, String email,String password) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter child's email: ");
        String childEmail = scanner.nextLine();
        Parent parent = new Parent(username,password, name, email, childEmail);
        Database.getParents().put(username, parent);
        Database.getAccounts().put(username, parent);
        System.out.println("Parent details saved.");
    }
    public Student findStudentByParent(Parent parent) {
        String childEmail = parent.getChildEmail();

        Student child = Database.getStudents().values().stream()
                .filter(student -> student.getEmail().equals(childEmail))
                .findFirst()
                .orElse(null);

        if (child == null) {
            System.out.println("Child not found with email: " + childEmail);
        }
        return child;
    }

}
