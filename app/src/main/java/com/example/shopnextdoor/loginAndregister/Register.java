package com.example.shopnextdoor.LoginAndRegister;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Data.Customer;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    EditText username, password, confirmPassword, name, mobile, address;
    RadioGroup gender;
    RadioButton genderVal;
    Button loginButton;
    TextView error, genderTitle;

    URL url = new URL();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

    //Main class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        gender = findViewById(R.id.gender);
        genderTitle = findViewById(R.id.genderTitle);
        error = findViewById(R.id.errorDisplay);
        loginButton = findViewById(R.id.loginButton);

        onPause();
    }

    @Override
    public void onPause(){
        super.onPause();
        username.getText().clear();
        password.getText().clear();
        confirmPassword.getText().clear();
        name.getText().clear();
        mobile.getText().clear();
        address.getText().clear();
        error.setText("");
        gender.clearCheck();
    }

    //Register Button
    public void btn_register(View view){

        //Setting field colors to default values
        username.setHighlightColor(Color.parseColor("#2196F3"));
        password.setHighlightColor(Color.parseColor("#2196F3"));
        confirmPassword.setHighlightColor(Color.parseColor("#2196F3"));
        name.setHighlightColor(Color.parseColor("#2196F3"));
        mobile.setHighlightColor(Color.parseColor("#2196F3"));
        address.setHighlightColor(Color.parseColor("#2196F3"));
        genderTitle.setHighlightColor(Color.parseColor("#2196F3"));
        error.setText("");

        String user = username.getText().toString();
        String pass = password.getText().toString();
        String confPass = confirmPassword.getText().toString();
        String nameVal = name.getText().toString();
        String mob = mobile.getText().toString();
        String add = address.getText().toString();
        genderVal = findViewById(gender.getCheckedRadioButtonId());

        if(nameVal.equals("")){
            name.setHintTextColor(Color.RED);
            error.setText("Full Name required.");
        }else if(genderVal==null){
            genderTitle.setHintTextColor(Color.RED);
            error.setText("Select Gender.");
        }else if(mob.equals("")){
            mobile.setHintTextColor(Color.RED);
            error.setText("Mobile Number required.");
        }else if(add.equals("")){
            address.setHintTextColor(Color.RED);
            error.setText("Address required.");
        }else if(user.equals("")){
            username.setHintTextColor(Color.RED);
            error.setText("Username required.");
        }else if(pass.equals("")){
            password.setHintTextColor(Color.RED);
            error.setText("Password required.");
        }else if(!confPass.equals(pass)){
            confirmPassword.setHintTextColor(Color.RED);
            error.setText("Password and Confirm Password do not match.");
        }else{
            if(user.length()<=4){
                error.setText("Username should be more than 4 characters");
            }else if(pass.length()<=6){
                error.setText("Password should be more than 6 characters");
            }else if(mob.length()!=10){
                error.setText("Invalid Mobile Number");
            }else if(pass.length()<=6){
                error.setText("Password should be more than 6 characters");
            }else{
                register();
            }
        }
    }

    //Function to register customer by calling API
    private void register() {
        //Toast.makeText(this, "Entered Register Function!", Toast.LENGTH_SHORT).show();
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String nameVal = name.getText().toString();
        String mob = mobile.getText().toString();
        String add = address.getText().toString();
        String genderT = genderVal.getText().toString();
        String err = error.getText().toString();

        Customer customer = new Customer(user, pass, nameVal, genderT, mob, add);

        //Calling the API
        Call<Customer> call = shopNextDoorServerAPI.registerCustomer(customer);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Register.this, "Registration unsuccessful due to some error. Please try again after some time.", Toast.LENGTH_SHORT).show();
                    Log.e("Unsuccessful response: ", response.toString());
                    return;
                }

                String result = response.body().getResult();
                Log.e("String response: ", result);
                if(result.equals("1")){
                    error.setTextColor(Color.BLUE);
                    error.setText("Customer created successfully! Proceed to login below.");
                    loginButton.setVisibility(View.VISIBLE);
                }else if(result.equals("2")){
                    error.setTextColor(Color.RED);
                    error.setText("Mobile number already registered with a different account!");
                }else if(result.equals("3")){
                    error.setTextColor(Color.RED);
                    error.setText("Username taken! Use a different one.");
                }else if(result.equals("4")){
                    Toast.makeText(Register.this, "Internal Server Error!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(Register.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Login Button
    public void btn_login(View view) {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }
}
