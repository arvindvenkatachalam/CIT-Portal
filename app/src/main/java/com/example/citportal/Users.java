package com.example.citportal;

public class Users {

    String username, password;

    public Users() {
    }

    public Users(String firstName, String password) {
        this.username = firstName;
        this.password = password;
    }

    public String getFirstName() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.username = username;
    }




    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}