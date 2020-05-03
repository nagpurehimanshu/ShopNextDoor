package com.example.shopnextdoor.loginAndregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopnextdoor.HomeCustomer;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.network.Customer;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
    }

    //Register Button
    public void btn_register(View view){
        startActivity(new Intent(getApplicationContext(), Register.class));
    }

    //Login Button
    public void btn_login(View view){
        final String user = username.getText().toString();
        String pass = password.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.13:3030")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

        Call<Customer> call = shopNextDoorServerAPI.getCustomer(user, pass);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {

                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    return;
                }
                String result = response.body().getResult();
                Log.e("String response: ", result);
                if(result.equals("1")){
                    Intent intent = new Intent(Login.this, HomeCustomer.class);
                    intent.putExtra("username", user);
                    startActivity(intent);
                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                }else if(result.equals("2")){
                    Toast.makeText(Login.this, "Username or Password incorrect!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Login.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
            }
        });

    }

    public void btn_shop_login(View view) {
        startActivity(new Intent(getApplicationContext(), ShopLogin.class));
    }
}
