package Midterm.View;

import Midterm.database.Database;
import Midterm.entities.*;
import Midterm.services.*;

import java.util.List;
import java.util.Scanner;

public class Menu {
    AccountServices accountServices = AccountServices.getInstance();
    AdminServices adminServices = AdminServices.getInstance();
    TeacherServices teacherServices = TeacherServices.getInstance();
    StudentServices studentServices = StudentServices.getInstance();
    ParentServices parentServices = ParentServices.getInstance();
    CourseServices courseServices = CourseServices.getInstance();

    public void displayMenu(Scanner scanner) {
        while (true) {
            System.out.println("1 - Login");
            System.out.println("2 - Register");
            System.out.println("0 - Exit Program");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    register(scanner);
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void register(Scanner scanner) {
        System.out.println("Welcome to Register:");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.println("Select Role: ");
        System.out.println("2 - Student");
        System.out.println("3 - Parent");
        System.out.print("Enter role number: ");
        boolean selectRole = true;
        while (selectRole){
            int role = Integer.parseInt(scanner.nextLine());
            if (role == 2 || role == 3) {
                accountServices.registerUser(username, password, email, role);
                selectRole = false;
            } else {
                System.out.println("Invalid role selection. Only Student and Parent roles can be registered.");
            }
        }

    }

    public void login(Scanner scanner) {
        System.out.println("Welcome to Login:");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Account loggedInAccount = accountServices.findAccountByUserName(username);

        if (accountServices.loginValid(username, password)) {
            System.out.println("Logged in as " + accountServices.getRoleName(loggedInAccount.getRole()));
            userLoggedInMenu(scanner, loggedInAccount);
        } else {
            System.out.println("Invalid login credentials.");
        }
    }

    private void userLoggedInMenu(Scanner scanner, Account account) {
        boolean loggedIn = true;
        while (loggedIn) {
            switch (account.getRole()) {
                case 0: // Admin menu
                    System.out.println("This is admin menu");
                    System.out.println("1. View Account by Role");
                    System.out.println("2. Create Teacher Account");
                    System.out.println("3. Delete an account with username");
                    System.out.println("0. Log out");

                    int option = Integer.parseInt(scanner.nextLine());
                    switch (option) {
                        case 1:
                            accountServices.viewAccountByRole();
                            break;
                        case 2:
                            adminServices.createTeacherAccount(scanner);
                            break;
                        case 3:
                            System.out.println("Input the username of the account");
                            String username = scanner.nextLine();
                            accountServices.deleteAccount(username);
                            break;
                        case 0:
                            System.out.println("Logged out");
                            loggedIn = false;
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                    break;
                case 1: // Teacher Menu
                    if (account instanceof Teacher) {
                        Teacher teacher = (Teacher) account;
                        boolean teacherLoggedIn = true;

                        while (teacherLoggedIn) {
                            System.out.println("This is the Teacher Menu:");
                            System.out.println("1. Get student list on specialty");
                            System.out.println("2. Create notification");
                            System.out.println("3. Manage courses");
                            System.out.println("4. Show teaching timetable");
                            System.out.println("5. Change password");
                            System.out.println("0. Log out");
                            System.out.print("Choose an option: ");

                            int teacherOption = Integer.parseInt(scanner.nextLine());

                            switch (teacherOption) {
                                case 0: // Log out
                                    System.out.println("Logged out of Teacher Menu.");
                                    teacherLoggedIn = false;
                                    break;

                                case 1: // Get student list by specialty
                                    teacherServices.getListOnSpeciality(scanner);
                                    break;

                                case 2: // Create notification
                                    System.out.println("Create a notification:");
                                    teacherServices.createNotification(scanner);
                                    break;

                                case 3: // Course management
                                    boolean managingCourses = true;
                                    while (managingCourses) {
                                        System.out.println("Courses Management:");
                                        System.out.println("1. Create new course");
                                        System.out.println("2. Manage existing courses");
                                        System.out.println("0. Back to Teacher Menu");
                                        System.out.print("Choose an option: ");

                                        int courseOption = Integer.parseInt(scanner.nextLine());

                                        switch (courseOption) {
                                            case 0: // Return to teacher menu
                                                System.out.println("Returning to Teacher Menu...");
                                                managingCourses = false;
                                                break;

                                            case 1: // Create new course
                                                System.out.println("Creating a new course...");
                                                courseServices.createNewCourse();
                                                break;

                                            case 2: // Manage existing courses
                                                System.out.println("Choose a course to manage:");
                                                courseServices.displayAllCourses();
                                                Course selectedCourse = courseServices.chooseCourse();

                                                if (selectedCourse == null) {
                                                    System.out.println("No course selected. Returning to course management.");
                                                    break;
                                                }

                                                boolean managingSelectedCourse = true;
                                                while (managingSelectedCourse) {
                                                    System.out.println("Managing Course: " + selectedCourse.getCourseName());
                                                    System.out.println("1. Delete Course");
                                                    System.out.println("2. View Student List");
                                                    System.out.println("3. Update Student Scores");
                                                    System.out.println("4. Attendance Check");
                                                    System.out.println("5. Update Student List");
                                                    System.out.println("0. Back to Course Selection");
                                                    System.out.print("Choose an option: ");

                                                    int selectedCourseOption = Integer.parseInt(scanner.nextLine());

                                                    switch (selectedCourseOption) {
                                                        case 0: // Back to course selection
                                                            System.out.println("Returning to course selection...");
                                                            managingSelectedCourse = false;
                                                            break;

                                                        case 1: // Delete course
                                                            courseServices.removeCourse(selectedCourse);
                                                            managingSelectedCourse = false; // Exit after deletion
                                                            break;

                                                        case 2: // View student list
                                                            courseServices.showStudentListEnrolled(selectedCourse);
                                                            break;

                                                        case 3: // Update student scores
                                                            courseServices.updateStudentScore(selectedCourse);
                                                            break;

                                                        case 4: // Attendance check
                                                            courseServices.attendanceCheck(selectedCourse);
                                                            break;

                                                        case 5: // Update student list
                                                            courseServices.updateStudentList(selectedCourse);
                                                            break;

                                                        default:
                                                            System.out.println("Invalid option. Please try again.");
                                                            break;
                                                    }
                                                }
                                                break;

                                            default:
                                                System.out.println("Invalid option. Please try again.");
                                                break;
                                        }
                                    }
                                    break;

                                case 4: // Show teaching timetable
                                    System.out.println("Displaying teaching timetable:");
                                    courseServices.showTimeTableForTeacher(teacher);
                                    break;
                                case 5:
                                    accountServices.changePassword(account);
                                    break;
                                default:
                                    System.out.println("Invalid option. Please try again.");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("You must be logged in as a teacher to access this menu.");
                    }
                    break;

                case 2: // Student menu
                    if (account instanceof Student) { // Type-check to ensure it's a Student
                        Student student = (Student) account;
                        System.out.println("This is student menu");
                        System.out.println("0. Log out");
                        System.out.println("1. View notifications");
                        System.out.println("2. View courses");
                        System.out.println("3. View Timetable");
                        System.out.println("4. Change password");

                        int studentOption = Integer.parseInt(scanner.nextLine());
                        switch (studentOption) {
                            case 0:
                                System.out.println("Logged out");
                                loggedIn = false;
                                break;
                            case 1:
                                List<Notification> notifications = studentServices.getNotifications(student);
                                if (notifications.isEmpty()) {
                                    System.out.println("No notifications.");
                                } else {
                                    System.out.println("Notifications:");
                                    for (Notification notification : notifications) {
                                        System.out.println(notification);
                                    }
                                }
                                break;
                            case 2:
                                courseServices.displayCourseDetailsForStudent(student);
                                break;
                            case 3:
                                courseServices.showTimeTableForStudent(student);
                                break;
                            case 4:
                                accountServices.changePassword(account);
                                break;
                            default:
                                System.out.println("Invalid option");
                                break;
                        }
                    } else {
                        System.out.println("This account is not a student.");
                        loggedIn = false;
                    }
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");

                case 3: // Parent menu
                    System.out.println("This is parent menu");
                    System.out.println("0. Log out");
                    System.out.println("1. View child's score");
                    System.out.println("2. View child's timetable");
                    System.out.println("3. Change password");

                    int parentOption = Integer.parseInt(scanner.nextLine());
                    switch (parentOption) {
                        case 0:
                            System.out.println("Logged out");
                            loggedIn = false;
                            break;
                        case 1:
                            Parent parentAccount = (Parent) account;
                            Student childStudent = parentServices.findStudentByParent(parentAccount);

                            if (childStudent != null) {
                                System.out.println("Child's Name: " + childStudent.getName());
                            } else {
                                System.out.println("Could not find child.");
                            }
                            break;
                        case 2:
                            parentAccount = (Parent) account;
                            childStudent = parentServices.findStudentByParent(parentAccount);

                            if (childStudent != null) {
                                courseServices.showTimeTableForStudent(childStudent);
                            } else {
                                System.out.println("Could not find child.");
                            }
                            break;
                        case 3:
                            accountServices.changePassword(account);
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                    break;

            }

        }
    }

}
