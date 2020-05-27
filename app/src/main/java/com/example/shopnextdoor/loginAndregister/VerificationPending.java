package com.example.shopnextdoor.LoginAndRegister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.shopnextdoor.R;

public class VerificationPending extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_verification_pending);
    }

    public void btn_login(View view){
        startActivity(new Intent(getApplicationContext(), ShopLogin.class));
    }
}
