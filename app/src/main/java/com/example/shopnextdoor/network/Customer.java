package com.example.shopnextdoor.network;

public class Customer {
    private String username;
    private String password;
    private String name;
    private String gender;
    private String mobile;
    private String address;
    private String result;

    public Customer(String username, String password, String name, String gender, String mobile, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.mobile = mobile;
        this.address = address;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getGender() {
        return this.gender;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getAddress() {
        return this.address;
    }

    public String getResult() {
        return this.result;
    }
}
