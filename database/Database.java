package Midterm.database;

import Midterm.entities.*;
import Midterm.services.CourseServices;
import java.time.*;
import java.util.*;

public class Database {
    public static Map<String, Account> accounts = new HashMap<>();
    public static Map<String, Student> students = new HashMap<>();
    public static Map<String, Parent> parents = new HashMap<>();
    public static Map<String, Teacher> teachers = new HashMap<>();
    public static Map<String, Course> courses = new HashMap<>(); // Centralized courses storage

    CourseServices courseServices = CourseServices.getInstance();

    static {
        // Default admin and teacher accounts
        accounts.put("adminUser", new Account("adminUser", "Admin123!", "admin@example.com", 0)); // Admin
        // Adding teachers to both accounts and teachers maps
        Teacher teacher1 = new Teacher("teacherUser1", "Teach456!", "John Doe", "USA", "teacher1@example.com", Speciality.WEB, LocalDate.of(1980, 3, 15));
        Teacher teacher2 = new Teacher("teacherUser2", "Teach789!", "Jane Smith", "UK", "teacher2@example.com", Speciality.ML, LocalDate.of(1985, 8, 22));

        // Adding teachers to maps
        teachers.put(teacher1.getUsername(), teacher1);
        teachers.put(teacher2.getUsername(), teacher2);

        accounts.put(teacher1.getUsername(), teacher1);
        accounts.put(teacher2.getUsername(), teacher2);

        // Add details for students (5 students, 2 with Speciality.WEB)
        Student student1 = new Student("studentUser1", "Stud101!", "student1@example.com", "Alice Smith", "USA", Speciality.WEB, LocalDate.of(2006, 12, 1));
        Student student2 = new Student("studentUser2", "Stud101!", "student2@example.com", "Bob Smith", "Phap", Speciality.WEB, LocalDate.of(2008, 12, 1));
        Student student3 = new Student("studentUser3", "Stud101!", "student3@example.com", "Alice Chieng", "USA", Speciality.ML, LocalDate.of(2006, 12, 1));
        Student student4 = new Student("studentUser4", "Stud101!", "student4@example.com", "Bob Chieng", "Phap", Speciality.ML, LocalDate.of(2008, 12, 1));
        Student student5 = new Student("studentUser5", "Stud101!", "student5@example.com", "Eve Black", "USA", Speciality.WEB, LocalDate.of(2007, 5, 15)); // New student for Speciality.WEB

        students.put(student1.getUsername(), student1);
        students.put(student2.getUsername(), student2);
        students.put(student3.getUsername(), student3);
        students.put(student4.getUsername(), student4);
        students.put(student5.getUsername(), student5);

        accounts.put(student1.getUsername(), student1);
        accounts.put(student2.getUsername(), student2);
        accounts.put(student3.getUsername(), student3);
        accounts.put(student4.getUsername(), student4);
        accounts.put(student5.getUsername(), student5);

        // Parent accounts
        Parent parent1 = new Parent("parentUser1", "ParentPass1", "Carol Smith", "parent1@example.com", "student1@example.com");
        Parent parent2 = new Parent("parentUser2", "ParentPass2", "David Johnson", "parent2@example.com", "student2@example.com");
        Parent parent3 = new Parent("parentUser3", "ParentPass3", "John Doe", "parent3@example.com", "student3@example.com");
        Parent parent4 = new Parent("parentUser4", "ParentPass4", "Jane Doe", "parent4@example.com", "student4@example.com");
        Parent parent5 = new Parent("parentUser5", "ParentPass5", "Anna Green", "parent5@example.com", "student5@example.com");

        parents.put(parent1.getUsername(), parent1);
        parents.put(parent2.getUsername(), parent2);
        parents.put(parent3.getUsername(), parent3);
        parents.put(parent4.getUsername(), parent4);
        parents.put(parent5.getUsername(), parent5);

        accounts.put(parent1.getUsername(), parent1);
        accounts.put(parent2.getUsername(), parent2);
        accounts.put(parent3.getUsername(), parent3);
        accounts.put(parent4.getUsername(), parent4);
        accounts.put(parent5.getUsername(), parent5);


        Map<DayOfWeek, List<Course.TimeSlot>> webCourse1Schedule = new HashMap<>();
        webCourse1Schedule.put(DayOfWeek.MONDAY, List.of(new Course.TimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0))));
        Course webCourse1 = new Course("CSE101", "Introduction to Programming", webCourse1Schedule, "teacherUser1");

        Map<DayOfWeek, List<Course.TimeSlot>> webCourse2Schedule = new HashMap<>();
        webCourse2Schedule.put(DayOfWeek.WEDNESDAY, List.of(new Course.TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0))));
        Course webCourse2 = new Course("CSE102", "Data Structures", webCourse2Schedule, "teacherUser1");

        Map<DayOfWeek, List<Course.TimeSlot>> webCourse3Schedule = new HashMap<>();
        webCourse3Schedule.put(DayOfWeek.FRIDAY, List.of(new Course.TimeSlot(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(11, 0))));
        Course webCourse3 = new Course("CSE103", "Web Development Basics", webCourse3Schedule, "teacherUser1");

        Map<DayOfWeek, List<Course.TimeSlot>> webCourse4Schedule = new HashMap<>();
        webCourse4Schedule.put(DayOfWeek.TUESDAY, List.of(new Course.TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(12, 0))));
        Course webCourse4 = new Course("CSE104", "Advanced Web Design", webCourse4Schedule, "teacherUser1");

        Map<DayOfWeek, List<Course.TimeSlot>> mlCourse1Schedule = new HashMap<>();
        mlCourse1Schedule.put(DayOfWeek.THURSDAY, List.of(new Course.TimeSlot(DayOfWeek.THURSDAY, LocalTime.of(13, 0), LocalTime.of(15, 0))));
        Course mlCourse1 = new Course("CSE201", "Machine Learning Fundamentals", mlCourse1Schedule, "teacherUser2");

        Map<DayOfWeek, List<Course.TimeSlot>> mlCourse2Schedule = new HashMap<>();
        mlCourse2Schedule.put(DayOfWeek.MONDAY, List.of(new Course.TimeSlot(DayOfWeek.MONDAY, LocalTime.of(15, 0), LocalTime.of(17, 0))));
        Course mlCourse2 = new Course("CSE202", "Deep Learning Concepts", mlCourse2Schedule, "teacherUser2");

        Map<DayOfWeek, List<Course.TimeSlot>> mlCourse3Schedule = new HashMap<>();
        mlCourse3Schedule.put(DayOfWeek.WEDNESDAY, List.of(new Course.TimeSlot(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0), LocalTime.of(13, 0))));
        Course mlCourse3 = new Course("CSE203", "AI and Robotics", mlCourse3Schedule, "teacherUser2");

        courses.put(webCourse1.getCourseId(), webCourse1);
        courses.put(webCourse2.getCourseId(), webCourse2);
        courses.put(webCourse3.getCourseId(), webCourse3);
        courses.put(webCourse4.getCourseId(), webCourse4);
        courses.put(mlCourse1.getCourseId(), mlCourse1);
        courses.put(mlCourse2.getCourseId(), mlCourse2);
        courses.put(mlCourse3.getCourseId(), mlCourse3);


        for (Student student : students.values()) {
            if (student.getSpeciality() == Speciality.ML) {
                mlCourse1.addStudent(student);
                mlCourse2.addStudent(student);
                mlCourse3.addStudent(student);
            }
            if (student.getSpeciality() == Speciality.WEB) {
                webCourse1.addStudent(student);
                webCourse2.addStudent(student);
                webCourse3.addStudent(student);
                webCourse4.addStudent(student);
            }
        }

    }

    // Getters for the maps
    public static Map<String, Account> getAccounts() {
        return accounts;
    }

    public static Map<String, Student> getStudents() {
        return students;
    }

    public static Map<String, Parent> getParents() {
        return parents;
    }

    public static Map<String, Teacher> getTeachers() {
        return teachers;
    }

    public static Map<String, Course> getCourses() {
        return courses;
    }
}
