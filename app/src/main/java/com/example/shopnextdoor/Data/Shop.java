package com.example.shopnextdoor.Data;

public class Shop {
    private String username;
    private String password;
    private String name;
    private String owner_name;
    private String owner_mobile;
    private String address;
    private String email;
    private String shop_type;
    private String result;

    public Shop(String username, String password, String name, String owner_name, String owner_mobile, String address, String email, String shop_type) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.owner_name = owner_name;
        this.owner_mobile = owner_mobile;
        this.email = email;
        this.shop_type = shop_type;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getOwner_mobile() {
        return owner_mobile;
    }

    public String getEmail() { return email; }

    public String getShop_type() {
        return shop_type;
    }

    public String getAddress() {
        return address;
    }

    public String getResult() {
        return result;
    }
}
