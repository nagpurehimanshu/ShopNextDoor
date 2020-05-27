package com.example.shopnextdoor.LoginAndRegister;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shopnextdoor.Customer.HomeCustomer;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Shop.HomeShop;
import com.example.shopnextdoor.Utility.ManageSharedPreferences;

public class WelcomePage extends AppCompatActivity {
    Button login, register, shop_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        checkLoginStatus();

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        shop_login = findViewById(R.id.shop_login);
    }

    private void checkLoginStatus() {
        if(ManageSharedPreferences.getUsername(getApplicationContext()).equals("")) return;

        if(ManageSharedPreferences.getType(getApplicationContext())){
            Toast.makeText(this, "Welcome Back Shopkeeper!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeShop.class);
            intent.putExtra("shop_username", ManageSharedPreferences.getUsername(getApplicationContext()));
            intent.putExtra("shop_name", ManageSharedPreferences.getName(getApplicationContext()));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Welcome Back Customer!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeCustomer.class);
            intent.putExtra("customer_username", ManageSharedPreferences.getUsername(getApplicationContext()));
            intent.putExtra("customer_name", ManageSharedPreferences.getName(getApplicationContext()));
            startActivity(intent);
        }

    }

    public void shop_login_btn(View view) {
        startActivity(new Intent(getApplicationContext(), ShopLogin.class));
    }

    public void login_btn(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
    }

    public void register_btn(View view) {
        startActivity(new Intent(getApplicationContext(), Register.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
