package Midterm.entities;

import java.time.LocalDate;

public class Teacher extends Account {
    private String name;
    private String origin;
    private Speciality speciality;
    private LocalDate dob;

    public Teacher(String username,String password ,String name, String origin, String email, Speciality speciality, LocalDate dob) {
        super(username, password, email, 1); // role 1 = Teacher
        this.name = name;
        this.origin = origin;
        this.speciality = speciality;
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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
