package Midterm.services;

import Midterm.database.Database;
import Midterm.entities.Course;
import Midterm.entities.Speciality;
import Midterm.entities.Student;
import Midterm.entities.Teacher;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.InputMismatchException;
import java.util.*;

public class CourseServices {

    private static CourseServices instance;

    private CourseServices() {
    }

    public static synchronized CourseServices getInstance() {
        if (instance == null) {
            instance = new CourseServices();
        }
        return instance;
    }

    TeacherServices teacherServices = TeacherServices.getInstance();

    public void addCourse(Course course) {
        Database.getCourses().put(course.getCourseId(), course);
    }

    public Course getCourse(String courseId) {
        return Database.getCourses().get(courseId);
    }

    public void createNewCourse() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Course Id:");
        String courseId = scanner.nextLine();

        System.out.println("Course Name:");
        String name = scanner.nextLine();


        System.out.println("This course is for students of the following specialities:");
        Set<Speciality> selectedSpecialities = teacherServices.selectSpecialities(scanner);


        System.out.println("Enter Teacher's Name for the course:");
        String teacherName = scanner.nextLine();


        System.out.println("Enter the study schedule for the course (you can add multiple time slots):");
        Map<DayOfWeek, List<Course.TimeSlot>> studySchedule = new HashMap<>();

        boolean addMoreTimeslots = true;
        while (addMoreTimeslots) {

            Course.TimeSlot timeSlot = createTimeSlot();
            if (timeSlot != null) {

                studySchedule.computeIfAbsent(timeSlot.getDay(), k -> new ArrayList<>()).add(timeSlot);
                System.out.println("Time slot added: " + timeSlot);
            }

            System.out.print("Add another timeslot for this course? (yes/no): ");
            String response = scanner.nextLine().toLowerCase();
            if (!response.equals("yes")) {
                addMoreTimeslots = false;
            }
        }


        Course course = new Course(courseId, name, studySchedule, teacherName);

        addCourse(course);

        addStudentOnSpeciality(course, selectedSpecialities);

        System.out.println("Course Created: " + courseId + " - " + name);
    }


    public void addStudentOnSpeciality(Course course, Set<Speciality> selectedSpecialities) {
        for (Speciality speciality : selectedSpecialities) {
            List<Student> studentsInSpeciality = teacherServices.getStudentsBySpeciality(speciality);
            for (Student student : studentsInSpeciality) {
                course.addStudent(student);
            }
        }
    }

    public List<Course> getCoursesForStudent(Student student) {
        List<Course> enrolledCourses = new ArrayList<>();

        for (Course course : Database.courses.values()) {
            if (course.getStudentRecords().containsKey(student)) {
                enrolledCourses.add(course);
            }
        }

        return enrolledCourses;
    }

    public void displayCoursesForStudent(Student student) {
        List<Course> enrolledCourses = getCoursesForStudent(student);

        if (enrolledCourses.isEmpty()) {
            System.out.println("Student " + student.getName() + " is not enrolled in any courses.");
            return;
        }

        System.out.println("Courses for Student " + student.getName() + ":");
        for (Course course : enrolledCourses) {
            System.out.println("Course ID: " + course.getCourseId() + ", Name: " + course.getCourseName());
        }
    }

    public void displayCourseDetailsForStudent(Student student) {
        Scanner scanner = new Scanner(System.in);

        displayCoursesForStudent(student);

        System.out.println("\nEnter the Course ID to view full information (or 'exit' to quit):");
        String selectedCourseId = scanner.nextLine();

        if ("exit".equalsIgnoreCase(selectedCourseId)) {
            System.out.println("Exiting course details view.");
            return;
        }

        Course selectedCourse = getCourse(selectedCourseId);

        if (selectedCourse == null || !selectedCourse.getStudentRecords().containsKey(student)) {
            System.out.println("Invalid Course ID or you are not enrolled in this course.");
            return;
        }

        System.out.println("\nFull Details for Course:");
        System.out.println("Course ID: " + selectedCourse.getCourseId());
        System.out.println("Name: " + selectedCourse.getCourseName());
        System.out.println("Teacher: " + selectedCourse.getTeacherName());
        System.out.println("Study Date: " + selectedCourse.getStudySchedule());
        System.out.println("Midterm Score: " + selectedCourse.getStudentRecords().get(student).getMidtermScore());
        System.out.println("Final Score: " + selectedCourse.getStudentRecords().get(student).getFinalScore());
        System.out.println("Attendance: " + selectedCourse.getStudentRecords().get(student).getAttendance());
        System.out.println("Overall Score " + selectedCourse.getStudentRecords().get(student).getOverallScore());
    }

    public void displayAllCourses() {
        for (Course course : Database.courses.values()) {
            System.out.println("Course ID: " + course.getCourseId() + ", Name: " + course.getCourseName());
        }
    }

    public Course chooseCourse() {
        System.out.println("\nEnter the Course ID to view full information (or 'exit' to quit):");
        Scanner scanner = new Scanner(System.in);
        String selectedCourseId = scanner.nextLine();

        if ("exit".equalsIgnoreCase(selectedCourseId)) {
            System.out.println("Exiting course details view.");
            return null;
        }

        Course selectedCourse = getCourse(selectedCourseId);
        if (selectedCourse == null) {
            System.out.println("Course not found. Please try again.");
            return null;
        }

        System.out.println("\nFull Details for Course:");
        System.out.println("Course ID: " + selectedCourse.getCourseId());
        System.out.println("Name: " + selectedCourse.getCourseName());
        System.out.println("Teacher: " + selectedCourse.getTeacherName());
        System.out.println("Study Date: " + selectedCourse.getStudySchedule());

        return selectedCourse;
    }

    public void showStudentListEnrolled(Course course) {
        System.out.println("\nEnrolled Students:");
        Map<Student, ?> studentRecords = course.getStudentRecords();
        if (studentRecords.isEmpty()) {
            System.out.println("No students enrolled in this course.");
        } else {
            for (Student student : studentRecords.keySet()) {
                System.out.println("- Student ID: " + student.getId() + ", Name: " + student.getName());
            }
        }
    }

    public void removeCourse(Course course) {
        System.out.println("Would you like to remove this course forever");
        System.out.println("Remember all the details about this course will be deleted.");
        System.out.println("Do you wish to continue?");
        System.out.println("Yes or no (Y / N)");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        choice = choice.toUpperCase();
        if (choice.equals("Y")) {
            Database.courses.remove(course.getCourseId());
        }
        if (choice.equals("N")) {
            System.out.println("Ok");
        }
    }

    public void updateStudentList(Course course) {
        System.out.println("This course is for student of :");
        Scanner scanner = new Scanner(System.in);
        Set<Speciality> selectedSpecialities = teacherServices.selectSpecialities(scanner);
        addStudentOnSpeciality(course, selectedSpecialities);
    }

    public void attendanceCheck(Course course) {
        for (Student student : course.getStudentRecords().keySet()) {
            System.out.println("Student ID: " + student.getId() + ", Name: " + student.getName());
            System.out.println(" YES or NO (Y / N)");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            choice = choice.toUpperCase();
            if (choice.equals("Y")) {
                course.updateAttendanceByOne(student);
            }
        }
        System.out.println("Finished!");
    }

    public void updateMidtermScore(Course course) {
        Scanner scanner = new Scanner(System.in);
        Map<Student, Course.StudentRecord> studentRecords = course.getStudentRecords();
        Set<Student> processedStudents = new HashSet<>();

        int totalStudents = studentRecords.size();
        System.out.println("Total students to process: " + totalStudents);

        while (processedStudents.size() < totalStudents) {
            for (Student student : studentRecords.keySet()) {

                if (processedStudents.contains(student)) {
                    continue;
                }

                System.out.println("Student ID: " + student.getId() + ", Name: " + student.getName());
                System.out.println("Current Midterm Score: " + studentRecords.get(student).getMidtermScore());
                System.out.println("Input new Score (or type -1 to exit, -2 to skip this student):");

                try {
                    float newScore = scanner.nextFloat();

                    if (newScore == -1) {
                        System.out.println("Exiting score update.");
                        return;
                    }

                    if (newScore == -2) {
                        System.out.println("Skipping student " + student.getName());
                        processedStudents.add(student);
                        break;
                    }

                    if (newScore < 0 || newScore > 100) {
                        System.out.println("Invalid score! Please enter a value between 0 and 100.");
                    } else {
                        studentRecords.get(student).setMidtermScore(newScore);
                        System.out.println("New midterm score for " + student.getName() + ": " + newScore);
                        processedStudents.add(student);
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a numeric value.");
                    scanner.next();
                }
            }
            System.out.println("Processed students: " + processedStudents.size() + "/" + totalStudents);
        }
        System.out.println("All students have been processed.");
    }


    public void updateFinalScore(Course course) {
        Scanner scanner = new Scanner(System.in);
        Map<Student, Course.StudentRecord> studentRecords = course.getStudentRecords();
        Set<Student> processedStudents = new HashSet<>();

        int totalStudents = studentRecords.size();
        System.out.println("Total students to process: " + totalStudents);

        while (processedStudents.size() < totalStudents) {
            for (Student student : studentRecords.keySet()) {

                if (processedStudents.contains(student)) {
                    continue;
                }

                System.out.println("Student ID: " + student.getId() + ", Name: " + student.getName());
                System.out.println("Current Final Score: " + studentRecords.get(student).getFinalScore());
                System.out.println("Input new Score (or type -1 to exit, -2 to skip this student):");

                try {
                    float newScore = scanner.nextFloat();

                    if (newScore == -1) {
                        System.out.println("Exiting score update.");
                        return;
                    }

                    if (newScore == -2) {
                        System.out.println("Skipping student " + student.getName());
                        processedStudents.add(student);
                        break;
                    }

                    if (newScore < 0 || newScore > 100) {
                        System.out.println("Invalid score! Please enter a value between 0 and 100.");
                    } else {
                        studentRecords.get(student).setFinalScore(newScore);
                        System.out.println("New midterm score for " + student.getName() + ": " + newScore);
                        processedStudents.add(student);
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a numeric value.");
                    scanner.next();
                }
            }
            System.out.println("Processed students: " + processedStudents.size() + "/" + totalStudents);
        }
        System.out.println("All students have been processed.");
    }

    public void updateOverallScore(Course course) {
        for (Student student : course.getStudentRecords().keySet()) {
            course.updateOverallScore(student);
        }
    }

    public static Course.TimeSlot createTimeSlot() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            System.out.print("Enter day of the week (e.g., MONDAY): ");
            Scanner scanner = new Scanner(System.in);
            String dayInput = scanner.nextLine().toUpperCase();
            DayOfWeek day = DayOfWeek.valueOf(dayInput);

            System.out.print("Enter start time (HH:mm): ");
            LocalTime startTime = LocalTime.parse(scanner.nextLine(), timeFormatter);

            System.out.print("Enter end time (HH:mm): ");
            LocalTime endTime = LocalTime.parse(scanner.nextLine(), timeFormatter);

            if (endTime.isBefore(startTime)) {
                System.out.println("Error: End time must be after start time.");
                return null;
            }

            return new Course.TimeSlot(day, startTime, endTime);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid day of the week. Please try again.");
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid time format. Please use HH:mm format.");
        }
        return null;
    }

    public void showTimeTableForStudent(Student student) {
        System.out.println("Displaying timetable for: " + student.getName());

        List<Course> enrolledCourses = getCoursesForStudent(student);
        if (enrolledCourses.isEmpty()) {
            System.out.println("Student " + student.getName() + " is not enrolled in any courses.");
            return;
        }

        // Create a timetable map for all days of the week
        Map<DayOfWeek, List<String>> timetable = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new ArrayList<>()); // Initialize empty lists for all days
        }

        // Populate the timetable with courses
        for (Course course : enrolledCourses) {
            Map<DayOfWeek, List<Course.TimeSlot>> studySchedule = course.getStudySchedule();

            for (Map.Entry<DayOfWeek, List<Course.TimeSlot>> entry : studySchedule.entrySet()) {
                DayOfWeek day = entry.getKey();
                List<Course.TimeSlot> timeSlots = entry.getValue();

                for (Course.TimeSlot timeSlot : timeSlots) {
                    String timeSlotInfo = String.format("%s: %s to %s", course.getCourseName(),
                            timeSlot.getStartTime(), timeSlot.getEndTime());
                    timetable.get(day).add(timeSlotInfo);
                }
            }
        }

        // Print the timetable
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println(day + ":");
            List<String> coursesForDay = timetable.get(day);
            if (coursesForDay.isEmpty()) {
                System.out.println("  No course on this day.");
            } else {
                for (String slot : coursesForDay) {
                    System.out.println("  - " + slot);
                }
            }
        }
    }


    public void showTimeTableForTeacher(Teacher teacher) {
        System.out.println("Displaying teaching timetable for: " + teacher.getName());

        List<Course> enrolledCourses = getCoursesForTeacher(teacher);
        if (enrolledCourses.isEmpty()) {
            System.out.println("Teacher " + teacher.getName() + " is not enrolled in any courses.");
            return;
        }


        Map<DayOfWeek, List<String>> timetable = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new ArrayList<>());
        }


        for (Course course : enrolledCourses) {
            Map<DayOfWeek, List<Course.TimeSlot>> studySchedule = course.getStudySchedule();

            for (Map.Entry<DayOfWeek, List<Course.TimeSlot>> entry : studySchedule.entrySet()) {
                DayOfWeek day = entry.getKey();
                List<Course.TimeSlot> timeSlots = entry.getValue();

                for (Course.TimeSlot timeSlot : timeSlots) {
                    String timeSlotInfo = String.format("%s: %s to %s", course.getCourseName(),
                            timeSlot.getStartTime(), timeSlot.getEndTime());
                    timetable.get(day).add(timeSlotInfo);
                }
            }
        }


        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println(day + ":");
            List<String> coursesForDay = timetable.get(day);
            if (coursesForDay.isEmpty()) {
                System.out.println("  No course on this day.");
            } else {
                for (String slot : coursesForDay) {
                    System.out.println("  - " + slot);
                }
            }
        }
    }


    public List<Course> getCoursesForTeacher(Teacher teacher) {
        List<Course> enrolledCourses = new ArrayList<>();
        for (Course course : Database.courses.values()) {
            System.out.println("Checking course: " + course.getCourseName() + " taught by: " + course.getTeacherName());
            if (course.getTeacherName().equals(teacher.getUsername())) {
                System.out.println("Matched course: " + course.getCourseName());
                enrolledCourses.add(course);
            }
        }
        return enrolledCourses;
    }


    public void updateStudentScore(Course chooseCourse) {
        boolean updateScore = true;

        while (updateScore) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("0. Go back");
            System.out.println("1. Midterm Score Update:");
            System.out.println("2. Final Score Update:");
            System.out.println("3. Overall Score Update:");
            int option3 = Integer.parseInt(scanner.nextLine());
            switch (option3) {
                case 0:
                    updateScore = false;
                    break;
                case 1:
                    updateMidtermScore(chooseCourse);
                    break;
                case 2:
                    updateFinalScore(chooseCourse);
                    break;
                case 3:
                    updateOverallScore(chooseCourse);
                    break;
                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }
}
