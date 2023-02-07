package com.example.bartaapp.model;

public class User {
    String user_id;
    String user_name;
    String user_email;
    String user_phone;
    String user_password;
    String user_rePassword;
    String user_profile;
    String user_cover;
    String user_bio;

    public User() {
    }

    public User(String user_id, String user_name, String user_email, String user_phone, String user_password, String user_rePassword, String user_profile, String user_cover, String user_bio) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.user_password = user_password;
        this.user_rePassword = user_rePassword;
        this.user_profile = user_profile;
        this.user_cover = user_cover;
        this.user_bio = user_bio;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_rePassword() {
        return user_rePassword;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public String getUser_cover() {
        return user_cover;
    }

    public String getUser_bio() {
        return user_bio;
    }
}
