package Midterm.services;

import Midterm.database.Database;
import Midterm.entities.*;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountServices {
    ParentServices parentServices = ParentServices.getInstance();
    TeacherServices teacherServices = TeacherServices.getInstance();
    StudentServices studentServices = StudentServices.getInstance();
    private static AccountServices instance;
    private AccountServices() {
    }

    public static synchronized AccountServices getInstance() {
        if (instance == null) {
            instance = new AccountServices();
        }
        return instance;
    }
    public boolean isUsernameExists(String username) {
        for (Account account : Database.accounts.values()) {
            if (account.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUsernameValid(String username) {
        if (username.contains(" ")) {
            return false;
        }
        return true;
    }

    public boolean isEmailExists(String email) {
        for (Account account : Database.accounts.values()) {
            if (account.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[.,-_])[A-Za-z0-9.,-_]{7,15}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    public void registerUser(String username, String password, String email, int role) {
        boolean isValid = true;

        if (isUsernameExists(username)) {
            System.out.println("Username already exists.");
            isValid = false;
        }
        if (!isUsernameValid(username)) {
            System.out.println("Username cannot contain spaces.");
            isValid = false;
        }
        if (isValidPassword(password)) {
            System.out.println("Invalid password format. Must have 1 uppercase letter, 7-15 characters, and 1 symbol (-, _, .).");
            isValid = false;
        }
        if (isEmailExists(email)) {
            System.out.println("Email already exists.");
            isValid = false;
        }
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            isValid = false;
        }
        if (isValid) {
            Account newAccount = new Account(username, password, email, role);
            Database.accounts.put(username, newAccount);

            System.out.println("Registered Successfully as " + getRoleName(role));

            if (role == 2) { // Student role
                StudentServices studentServices = StudentServices.getInstance();
                studentServices.addStudentDetails(username, email,password);
                Student student = (Student) Database.getStudents().get(username);
                Database.getAccounts().put(username, student);
            } else if (role == 3) {
                ParentServices parentServices = ParentServices.getInstance();
                parentServices.addParentDetails(username, email,password);
                Parent parent = (Parent) Database.getParents().get(username);
                Database.getAccounts().put(username, parent);
            }
            else if(role == 1){
                TeacherServices teacherServices = TeacherServices.getInstance();
                teacherServices.addTeacherDetails(username, email,password);
                Teacher teacher = (Teacher) Database.getTeachers().get(username);
                Database.getAccounts().put(username, teacher);
            }
        }
    }

    public String getRoleName(int role) {
        switch (role) {
            case 0: return "Admin";
            case 1: return "Teacher";
            case 2: return "Student";
            case 3: return "Parent";
            default: return "Unknown";
        }
    }




    // Login Part
    public boolean loginValid(String userName, String password) {
        Account foundUserAccount = findAccountByUserName(userName);
        if (!isUsernameValid(userName)) {
            System.out.println("Username cannot contain spaces.");
            return false;
        }
        if (foundUserAccount == null) {
            System.out.println("Username not found. Please try again.");
            return false;
        }
        if (!foundUserAccount.getPassword().equals(password)) {
            System.out.println("Password is incorrect.");
            handlePasswordOptions(foundUserAccount);
            return false;
        }
        System.out.println("Login Successful.");
        return true;
    }

    public void handlePasswordOptions(Account account) {
        Scanner scanner = new Scanner(System.in);
        boolean validOption = false;

        while (!validOption) {
            System.out.println("1. Try logging in again");
            System.out.println("2. Forgot password");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Please enter your username and password again.");
                    validOption = true;
                    break;
                case 2:
                    passwordRecoverWithEmail(account);
                    validOption = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Password recovery method
    public void passwordRecoverWithEmail(Account account) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the email used for registration: ");
        String email = scanner.nextLine();
        if (account.getEmail().equals(email)) {
            System.out.print("Email verified. Please enter a new password: ");
            String newPassword = scanner.nextLine();
            while (isValidPassword(newPassword)) {
                System.out.println("Invalid password format. Password must have one uppercase letter, 7-15 characters, and one symbol (-, _, .).");
                newPassword = scanner.nextLine();
            }
            account.setPassword(newPassword);
            System.out.println("Password has been reset successfully!");
        } else {
            System.out.println("Email does not match. Password reset failed.");
        }
    }

    public Account findAccountByUserName(String userName) {
        for (Account account : Database.accounts.values()) {
            if (account.getUsername().equals(userName)) {
                return account;
            }
        }
        return null;
    }

    //Logged in user logic
    public void changeUsername(Account account, String newUsername) {
        if (!isUsernameExists(newUsername) && isUsernameValid(newUsername)) {
            account.setUsername(newUsername);
            System.out.println("Username updated successfully.");
        } else {
            System.out.println("Invalid or already existing username.");
        }
    }

    public void changeEmail(Account account, String newEmail) {
        if (!isEmailExists(newEmail) && isValidEmail(newEmail)) {
            account.setEmail(newEmail);
            System.out.println("Email updated successfully.");
        } else {
            System.out.println("Invalid or already existing email.");
        }
    }

    public void changePassword(Account account) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        while (isValidPassword(newPassword)) {
            System.out.println("Invalid password format. Must have 1 uppercase letter, 7-15 characters, and 1 symbol (-, _, .).");
            newPassword = scanner.nextLine();
        }
        account.setPassword(newPassword);
        System.out.println("Password updated successfully.");
    }

    /*admin features*/
    public void viewAccountByRole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Show list of (1) Teacher || (2) Student || (3) Parent");
        int option = Integer.parseInt(scanner.nextLine());

        switch (option) {
            case 1:
                System.out.println("List of teachers: ");
                for (Account account : Database.getAccounts().values()) {
                    if (account.getRole() == 1) {
                        String username = account.getUsername();
                        Teacher teacher = Database.getTeachers().get(username);
                        if (teacher != null) {
                            System.out.println("Username: " + teacher.getUsername());
                            System.out.println("Name : " + teacher.getName());
                            System.out.println("Email: " + teacher.getEmail());
                            System.out.println("Origin: " + teacher.getOrigin());
                            System.out.println("Speciality: " + teacher.getSpeciality());
                            System.out.println("-------------------------------");
                        }
                    }
                }
                break;
            case 2:
                System.out.println("List of students: ");
                for (Account account : Database.getAccounts().values()) {
                    if (account.getRole() == 2) {
                        String username = account.getUsername();
                        Student student = Database.getStudents().get(username);
                        if (student != null) {
                            System.out.println("Id " + student.getId());
                            System.out.println("Username: " + student.getUsername());
                            System.out.println("Email: " + student.getEmail());
                            System.out.println("Origin: " + student.getOrigin());
                            System.out.println("Class: " + student.getSpeciality());
                            System.out.println("Dob: " + student.getDob());
                            System.out.println("-------------------------------");
                        }
                    }
                }
                break;
            case 3:
                System.out.println("List of parents: ");
                for (Account account : Database.getAccounts().values()) {
                    if (account.getRole() == 3) {
                        String username = account.getUsername();
                        Parent parent = Database.getParents().get(username);
                        if (parent != null) {
                            System.out.println("Username: " + parent.getUsername());
                            System.out.println("Name " + parent.getName());
                            System.out.println("Email: " + parent.getEmail());
                            System.out.println("Child's Email: " + parent.getChildEmail());
                            System.out.println("-------------------------------");
                        }
                    }
                }
                break;
            default:
                System.out.println("Invalid option. Please choose 1, 2, or 3.");
        }
    }



    public void deleteAccount(String username) {
        if (username.equals("adminUser") ){
            System.out.println("Can not delete this account");
            return;
        }
        Account account = Database.accounts.get(username);

        if (account == null) {
            System.out.println("Account not found!");
            return;
        }

        Database.accounts.remove(username);
        System.out.println("Account " + username + " deleted successfully.");


        switch (account.getRole()) {
            case 1:
                Database.teachers.remove(username);
                System.out.println("Teacher details for " + username + " deleted.");
                break;
            case 2:
                Database.students.remove(username);
                System.out.println("Student details for " + username + " deleted.");
                break;
            case 3:
                Database.parents.remove(username);
                System.out.println("Parent details for " + username + " deleted.");
                break;
            default:
                System.out.println("No additional data to delete for this role.");
                break;
        }
    }


}