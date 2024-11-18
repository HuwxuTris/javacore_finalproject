package Midterm.entities;

public class Parent extends Account {
    private String name;
    private String childEmail;

    public Parent(String username, String password,String name, String email, String childEmail) {
        super(username, password, email, 3); // role 3 = Parent
        this.name = name;
        this.childEmail = childEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChildEmail() {
        return childEmail;
    }

    public void setChildEmail(String childEmail) {
        this.childEmail = childEmail;
    }
}
