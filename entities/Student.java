package Midterm.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends Account {
    private static int autoId = 0;
    private int id;
    private String name;
    private String origin;
    private Speciality speciality;
    private LocalDate dob;
    private List<Notification> notifications = new ArrayList<>();

    public Student(String username, String password, String email, String name, String origin, Speciality speciality, LocalDate dob) {
        super(username, password, email, 2); // 2 is the role for Student
        this.id = ++autoId;
        this.name = name;
        this.origin = origin;
        this.speciality = speciality;
        this.dob = dob;
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }
}
