package com.example.test6;

public class HelperClass {

    String username;
    String email;
    String password;
    String imgUrl;

    public HelperClass(String username, String email, String password, String imgUrl) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.imgUrl = imgUrl;
    }

    public HelperClass(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public HelperClass() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
