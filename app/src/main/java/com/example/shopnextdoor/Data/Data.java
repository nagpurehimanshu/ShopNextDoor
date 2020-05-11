package com.example.shopnextdoor.Data;

import android.widget.EditText;

public class Data {
    String item_number;
    String item_name;
    String quantity;
    String unit;

    public Data(String item_number, String item_name, String quantity, String unit) {
        this.item_number = item_number;
        this.item_name = item_name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getItem_number() {
        return item_number;
    }

    public void setItem_number(String item_number) {
        this.item_number = item_number;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
