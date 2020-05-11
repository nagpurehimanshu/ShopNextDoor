package com.example.shopnextdoor.LoginAndRegister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shopnextdoor.LoginAndRegister.ShopLogin;
import com.example.shopnextdoor.Customer.HomeCustomer;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Data.Customer;
import com.example.shopnextdoor.Utility.ManageSharedPreferences;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    EditText username;
    EditText password;

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
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        if(ManageSharedPreferences.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), HomeCustomer.class);
            startActivity(intent);
        }

        onPause();
    }

    @Override
    public void onPause(){
        super.onPause();
        username.getText().clear();
        password.getText().clear();
    }

    //Register Button
    public void btn_register(View view){
        startActivity(new Intent(getApplicationContext(), Register.class));
    }

    //Login Button
    public void btn_login(View view){
        final String user = username.getText().toString();
        String pass = password.getText().toString();

        //API call to check user details
        Call<Customer> call = shopNextDoorServerAPI.getCustomer(user, pass);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(Login.this, "Login Attempt Unsuccessful", Toast.LENGTH_SHORT).show();
                    return;
                }

                String result = response.body().getResult();
                Log.e("String response: ", result);

                if(result.equals("1")){
                    Intent intent = new Intent(Login.this, HomeCustomer.class);
                    intent.putExtra("customer_username", user);
                    intent.putExtra("customer_name", response.body().getName());

                    startActivity(intent);
                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                }else if(result.equals("2")){
                    Toast.makeText(Login.this, "Username or Password incorrect!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Login.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(Login.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Shop Login Button
    public void btn_shop_login(View view) {
        startActivity(new Intent(getApplicationContext(), ShopLogin.class));
    }
}
