package com.example.quickcode.loginRegister;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String uid;
    private String firstName;
    private String email;
    private String phoneNumber;

    public User() {
    }

    public User(String firstName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("firstName", firstName);
        result.put("email", email);
        result.put("phoneNumber", phoneNumber);
        return result;
    }
}