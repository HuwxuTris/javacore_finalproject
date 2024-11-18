package Midterm.services;

import Midterm.database.Database;
import Midterm.entities.Notification;
import Midterm.entities.Speciality;
import Midterm.entities.Student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentServices {
    private static StudentServices instance;
    private StudentServices() {
    }

    public static synchronized StudentServices getInstance() {
        if (instance == null) {
            instance = new StudentServices();
        }
        return instance;
    }
    public void addStudentDetails(String username, String email,String password) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter origin: ");
        String origin = scanner.nextLine();

        Speciality speciality = null;
        while (speciality == null) {
            System.out.println("Enter speciality (Choose from): ");
            for (Speciality s : Speciality.values()) {
                System.out.println("- " + s);
            }
            String specialityInput = scanner.nextLine().toUpperCase();

            try {
                speciality = Speciality.valueOf(specialityInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid speciality. Please enter a valid option.");
            }
        }

        // Prompt for date of birth
        LocalDate dob = null;
        while (dob == null) {
            System.out.print("Please enter your date of birth (YYYY-MM-DD): ");
            String dobInput = scanner.nextLine();

            try {
                dob = LocalDate.parse(dobInput, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please enter the date in the format YYYY-MM-DD.");
            }
        }

        // Create the Student object with all required information
        Student student = new Student(username, password, email, name, origin, speciality, dob);

        // Add the student to the Database
        Database.getStudents().put(username, student);

        System.out.println("Student details saved.");
    }

    public void printInfo(Student student){
        System.out.println("=====================");
        System.out.println("Id " + student.getId());
        System.out.println("Name " + student.getName());
        System.out.println("Origin " + student.getOrigin());
        System.out.println("Username " + student.getUsername());
        System.out.println("Dob " + student.getDob());
    }

    public void receiveNotification(Notification notification, List<Student> students) {
        for (Student student : students) {
            student.addNotification(notification);
        }
    }
    public List<Notification> getNotifications(Student student) {
        return student.getNotifications();
    }


}
