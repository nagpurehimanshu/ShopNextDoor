package com.example.shopnextdoor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLOutput;

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        databaseHelper = new DatabaseHelper(this);
    }

    //Register Button
    public void btn_register(View view){
        startActivity(new Intent(getApplicationContext(),Register.class));
    }

    //Login Button
    public void btn_login(View view){
        String user = username.getText().toString();
        String pass = password.getText().toString();

        if(databaseHelper.isLoginValid(user, pass)==true){
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
        }
    }
}
