package Midterm.entities;

import java.time.LocalDate;

public class Notification {
    private String title;
    private String message;
    private LocalDate date;
    private String createdBy;


    public Notification(String title, String message, LocalDate date, String createdBy) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public String toString() {
        return "=============\n" +
                title + "\n"+
                message +"\n" +
                date + "\n" +
               createdBy ;
    }
}

