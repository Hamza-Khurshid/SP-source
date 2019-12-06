package tayyab.khan.fyp.smartparking.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String id, name, phone, password;
    private String currentBookings;

    public User() {
    }

    public User(String id, String name, String phone, String password, String currentBookings) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.currentBookings = currentBookings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrentBookings() {
        return currentBookings;
    }

    public void setCurrentBookings(String currentBookings) {
        this.currentBookings = currentBookings;
    }
}
