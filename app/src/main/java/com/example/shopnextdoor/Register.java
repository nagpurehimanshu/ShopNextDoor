package com.example.shopnextdoor;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText username, password, name, mobile, address;
    RadioGroup gender;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        mobile = (EditText)findViewById(R.id.mobile);
        address = (EditText)findViewById(R.id.address);
        gender = (RadioGroup)findViewById(R.id.gender);
        databaseHelper = new DatabaseHelper(this);
    }

    public void btn_register(View view){
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String nameVal = name.getText().toString();
        String mob = mobile.getText().toString();
        String add = address.getText().toString();

        RadioButton checkGender = findViewById(gender.getCheckedRadioButtonId());
        String genderVal = checkGender.getText().toString();

        if(user.length()>3 && pass.length()>6){
            ContentValues contentValues = new ContentValues();
            contentValues.put("username",user);
            contentValues.put("password",pass);
            contentValues.put("name",nameVal);
            contentValues.put("mobile",mob);
            contentValues.put("address",add);
            contentValues.put("gender",genderVal);

            databaseHelper.insertUser(contentValues);
            Toast.makeText(this, "User Registered! Now login using same credentials.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Enter the values correctly.", Toast.LENGTH_SHORT).show();
        }
    }
}
