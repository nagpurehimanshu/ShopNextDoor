package com.example.shopnextdoor.LoginAndRegister;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Data.Shop;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopRegister extends AppCompatActivity {

    EditText name, owner_name, owner_mobile, address, username, password, confirmPassword;
    Button loginButton;
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
        setContentView(R.layout.activity_shop_register);
        name = findViewById(R.id.name);
        owner_name = findViewById(R.id.owner_name);
        owner_mobile = findViewById(R.id.owner_mobile);
        address = findViewById(R.id.address);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        loginButton = findViewById(R.id.loginButton);
        errorDisplay = findViewById(R.id.errorDisplay);

        onPause();
    }

    @Override
    public void onPause(){
        super.onPause();
        username.getText().clear();
        password.getText().clear();
        confirmPassword.getText().clear();
        name.getText().clear();
        owner_mobile.getText().clear();
        address.getText().clear();
        errorDisplay.setText("");
        owner_name.getText().clear();
    }

    //Register Button
    public void btn_register(View view) {
        username.setHighlightColor(Color.parseColor("#2196F3"));
        password.setHighlightColor(Color.parseColor("#2196F3"));
        confirmPassword.setHighlightColor(Color.parseColor("#2196F3"));
        name.setHighlightColor(Color.parseColor("#2196F3"));
        owner_mobile.setHighlightColor(Color.parseColor("#2196F3"));
        address.setHighlightColor(Color.parseColor("#2196F3"));
        owner_name.setHighlightColor(Color.parseColor("#2196F3"));
        errorDisplay.setText("");

        String user = username.getText().toString();
        String pass = password.getText().toString();
        String shopName = name.getText().toString();
        String ownerName = owner_name.getText().toString();
        String ownerMobile = owner_mobile.getText().toString();
        String add = address.getText().toString();
        String confPass = confirmPassword.getText().toString();

        if(shopName.equals("")){
            name.setHintTextColor(Color.RED);
            errorDisplay.setText("Shop Name required.");
        }else if(ownerName==null){
            owner_name.setHintTextColor(Color.RED);
            errorDisplay.setText("Owner Name required.");
        }else if(ownerMobile.equals("")){
            owner_mobile.setHintTextColor(Color.RED);
            errorDisplay.setText("Owner Mobile Number required.");
        }else if(add.equals("")){
            address.setHintTextColor(Color.RED);
            errorDisplay.setText("Shop Address required.");
        }else if(user.equals("")){
            username.setHintTextColor(Color.RED);
            errorDisplay.setText("Username required.");
        }else if(pass.equals("")){
            password.setHintTextColor(Color.RED);
            errorDisplay.setText("Password required.");
        }else if(!confPass.equals(pass)){
            confirmPassword.setHintTextColor(Color.RED);
            errorDisplay.setText("Password and Confirm Password do not match.");
        }else{
            if(user.length()<=4){
                errorDisplay.setText("Username should be more than 4 characters");
            }else if(pass.length()<=6){
                errorDisplay.setText("Password should be more than 6 characters");
            }else if(ownerMobile.length()!=10){
                errorDisplay.setText("Invalid Mobile Number");
            }else if(pass.length()<=6){
                errorDisplay.setText("Password should be more than 6 characters");
            }else{
                register();
            }
        }
    }

    //Function to register shopkeepers
    private void register() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String shopName = name.getText().toString();
        String ownerName = owner_name.getText().toString();
        String ownerMobile = owner_mobile.getText().toString();
        String add = address.getText().toString();
        String confPass = confirmPassword.getText().toString();

        Shop shop = new Shop(user, pass, shopName, ownerName, ownerMobile, add);
        Call<Shop> call = shopNextDoorServerAPI.registerShop(shop);
        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(ShopRegister.this, "Registration unsuccessful due to some error. Please try again after some time.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String result = response.body().getResult();
                Log.e("String response: ", result);
                if(result.equals("1")){
                    errorDisplay.setTextColor(Color.BLUE);
                    errorDisplay.setText("Account creation request sent successfully! Wait until we verify and approve your account.");
                    loginButton.setVisibility(View.VISIBLE);
                }else if(result.equals("2")){
                    errorDisplay.setTextColor(Color.RED);
                    errorDisplay.setText("Username taken! Use a different one.");
                }else if(result.equals("3")){
                    Toast.makeText(ShopRegister.this, "Internal Server Error!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(ShopRegister.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Login Button
    public void btn_login(View view) {
        startActivity(new Intent(getApplicationContext(), ShopLogin.class));
    }
}
