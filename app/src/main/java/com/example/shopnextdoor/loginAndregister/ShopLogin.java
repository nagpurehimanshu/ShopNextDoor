package com.example.shopnextdoor.loginAndregister;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.HomeCustomer;
import com.example.shopnextdoor.HomeShop;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.network.Shop;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopLogin extends AppCompatActivity {

    EditText username, password;
    TextView errorDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        errorDisplay = findViewById(R.id.errorDisplay);
    }

    public void btn_register(View view) {
        startActivity(new Intent(getApplicationContext(), ShopRegister.class));
    }

    public void btn_login(View view) {
        final String user = username.getText().toString();
        String pass = password.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.13:3030")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

        Call<Shop> call = shopNextDoorServerAPI.getShop(user, pass);
        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    return;
                }
                String result = response.body().getResult();
                Log.e("String response: ", result);

                if(result.equals("1")){
                    Intent intent = new Intent(ShopLogin.this, VerificationPending.class);
                    startActivity(intent);
                }else if(result.equals("2")){
                    Intent intent = new Intent(ShopLogin.this, HomeShop.class);
                    intent.putExtra("username", user);
                    startActivity(intent);
                    Toast.makeText(ShopLogin.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                }else if(result.equals("3")){
                    errorDisplay.setTextColor(Color.RED);
                    errorDisplay.setText("Your verification has been rejected by our team. \n Redirecting you to Customer Login Page.");
                }else if(result.equals("4")){
                    Toast.makeText(ShopLogin.this, "Username or Password incorrect!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ShopLogin.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
            }
        });
    }
}
