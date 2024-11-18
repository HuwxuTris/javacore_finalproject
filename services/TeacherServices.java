package Midterm.services;

import Midterm.database.Database;
import Midterm.entities.Notification;
import Midterm.entities.Speciality;
import Midterm.entities.Student;
import Midterm.entities.Teacher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class TeacherServices {
    private static TeacherServices instance;
    private TeacherServices() {
    }

    public static synchronized TeacherServices getInstance() {
        if (instance == null) {
            instance = new TeacherServices();
        }
        return instance;
    }

    StudentServices studentServices = StudentServices.getInstance();
    public void addTeacherDetails(String username, String email,String password) {
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

        Teacher teacher = new Teacher(username, password, name, origin, email, speciality, dob);
        Database.getTeachers().put(username, teacher);
        Database.getAccounts().put(username, teacher);
        System.out.println("Teacher details saved.");
    }
    public void getListOnSpeciality(Scanner scanner){
        System.out.println("List student of :");
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
        System.out.println("List student of " + speciality);
        for (Student student : Database.students.values()){
            if (student.getSpeciality().equals(speciality)){
                studentServices.printInfo(student);
            }
        }
    }
    public void createNotification(Scanner scanner) {
        System.out.println("Title of the notification:");
        String title = scanner.nextLine();

        System.out.println("Enter notification note:");
        String note = scanner.nextLine();
        Set<Speciality> selectedSpecialities = selectSpecialities(scanner);
        System.out.println("Notification by :");
        String name = scanner.nextLine();
        Notification notification = new Notification(title, note, LocalDate.now(),name);

        sendNotificationToClasses(notification, selectedSpecialities);
    }

    public Set<Speciality> selectSpecialities(Scanner scanner) {
        Set<Speciality> selectedSpecialities = new HashSet<>();
        String input;

        System.out.println("Enter speciality (ML, WEB, ANDROID, IOS, GAME). Enter 'done' when finished:");
        while (true) {
            input = scanner.nextLine().toUpperCase();

            if ("DONE".equals(input)) break;

            try {
                Speciality speciality = Speciality.valueOf(input);
                selectedSpecialities.add(speciality);
                System.out.println("Added " + speciality + ". Enter another or 'done' to finish.");
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid speciality. Please enter a valid option.");
            }
        }

        return selectedSpecialities;
    }
    public void sendNotificationToClasses(Notification notification, Set<Speciality> selectedSpecialities) {
        for (Speciality speciality : selectedSpecialities) {
            List<Student> studentsInSpeciality = getStudentsBySpeciality(speciality);
            studentServices.receiveNotification(notification, studentsInSpeciality);
        }
    }
    public List<Student> getStudentsBySpeciality(Speciality speciality) {
        return Database.getStudents().values().stream()
                .filter(student -> student.getSpeciality() == speciality)
                .collect(Collectors.toList());
    }
}
