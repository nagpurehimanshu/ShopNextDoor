package com.example.shopnextdoor.LoginAndRegister;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.Shop.HomeShop;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Data.Shop;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopLogin extends AppCompatActivity {

    EditText username, password;
    TextView errorDisplay;
    URL url = new URL();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

    //Main Class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        errorDisplay = findViewById(R.id.errorDisplay);

        onPause();
    }

    @Override
    public void onPause(){
        super.onPause();
        username.getText().clear();
        password.getText().clear();
    }

    public void btn_register(View view) {
        startActivity(new Intent(getApplicationContext(), ShopRegister.class));
    }

    public void btn_login(View view) {
        final String user = username.getText().toString();
        String pass = password.getText().toString();

        //Calling the API
        Call<Shop> call = shopNextDoorServerAPI.getShop(user, pass);
        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(ShopLogin.this, "Unsuccessful Login Attempt. Please try again.", Toast.LENGTH_SHORT).show();
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
                    intent.putExtra("shop_username", user);
                    intent.putExtra("shop_name", response.body().getName());
                    startActivity(intent);
                    Toast.makeText(ShopLogin.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                }else if(result.equals("3")){
                    errorDisplay.setTextColor(Color.RED);
                    errorDisplay.setText("Your verification has been rejected by our team.");
                }else if(result.equals("4")){
                    Toast.makeText(ShopLogin.this, "Username or Password incorrect!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ShopLogin.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(ShopLogin.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
