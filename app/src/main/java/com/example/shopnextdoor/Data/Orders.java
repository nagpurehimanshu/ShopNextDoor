package com.example.shopnextdoor.Data;

public class Orders {
    String order_number;
    String order_items;
    String customer_username;
    String shop_username;
    String order_placed_date;
    String order_acceptance_date;
    String order_completion_date;
    String order_type;
    int amount;
    String order_status;
    String result;
    String order_mode;
    String customer_name;
    String shop_name;
    String rejection_msg;

    public Orders(String order_items, String customer_username, String shop_username, String order_type, String order_mode, String customer_name, String shop_name) {
        this.order_items = order_items;
        this.customer_username = customer_username;
        this.shop_username = shop_username;
        this.order_type = order_type;
        this.order_mode = order_mode;
        this.customer_name = customer_name;
        this.shop_name = shop_name;
    }

    public Orders(){}

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_items() {
        return order_items;
    }

    public void setOrder_items(String order_items) {
        this.order_items = order_items;
    }

    public String getCustomer_username() {
        return customer_username;
    }

    public void setCustomer_username(String customer_username) {
        this.customer_username = customer_username;
    }

    public String getShop_username() {
        return shop_username;
    }

    public void setShop_username(String shop_username) {
        this.shop_username = shop_username;
    }

    public String getOrder_placed_date() {
        return order_placed_date;
    }

    public void setOrder_placed_date(String order_placed_date) {
        this.order_placed_date = order_placed_date;
    }

    public String getOrder_acceptance_date() {
        return order_acceptance_date;
    }

    public void setOrder_acceptance_date(String order_acceptance_date) {
        this.order_acceptance_date = order_acceptance_date;
    }

    public String getOrder_completion_date() {
        return order_completion_date;
    }

    public void setOrder_completion_date(String order_completion_date) {
        this.order_completion_date = order_completion_date;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOrder_mode() {
        return order_mode;
    }

    public void setOrder_mode(String order_mode) {
        this.order_mode = order_mode;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getRejection_msg() {
        return rejection_msg;
    }

    public void setRejection_msg(String rejection_msg) {
        this.rejection_msg = rejection_msg;
    }
}
