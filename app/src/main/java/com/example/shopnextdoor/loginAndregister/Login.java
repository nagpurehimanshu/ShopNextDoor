package com.example.shopnextdoor.LoginAndRegister;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.Customer.HomeCustomer;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Data.Customer;
import com.example.shopnextdoor.Utility.LoadingDialog;
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
    LoadingDialog loadingDialog;

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
        setContentView(R.layout.activity_customer_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loadingDialog = new LoadingDialog(this);

        onPause();
    }

    @Override
    public void onPause(){
        super.onPause();
        username.getText().clear();
        password.getText().clear();
    }

    //Login Button
    public void btn_login(View view){
        final String user = username.getText().toString();
        final String pass = password.getText().toString();

//        username.setHintTextColor(Color.parseColor("#2196F3"));
//        password.setHintTextColor(Color.parseColor("#2196F3"));

        if(user.length()<=4){
            showMyDialog("Username can not be less than 4 letters.", 2);
            return;
        }

        if(pass.length()<=6){
            showMyDialog("Password can not be less than 6 letters.", 2);
            return;
        }

        //API call to check user details
        Call<Customer> call = shopNextDoorServerAPI.getCustomer(user);
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
                    if(response.body().getPassword().equals(pass)){
                        loadingDialog.startDialog();
                        ManageSharedPreferences.saveUsername(getApplicationContext(), user);
                        ManageSharedPreferences.saveName(getApplicationContext(), response.body().getName());
                        ManageSharedPreferences.saveType(getApplicationContext(), false);
                        Intent intent = new Intent(Login.this, HomeCustomer.class);
                        intent.putExtra("customer_username", user);
                        intent.putExtra("customer_name", response.body().getName());
                        startActivity(intent);
                        Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    }else{
                        showMyDialog("Username or Password incorrect!", 2);
                    }
                }else if(result.equals("2")){
                    showMyDialog("Username or Password incorrect!", 2);
                }else{
                    showMyDialog("Internal Server Error", 2);
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(Login.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
        //loadingDialog.dismissDialog();
    }

    //Show registration error dialog (action: 1 for login button, 2 for ok button)
    private void showMyDialog(String error, int action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_registration, null);
        TextView error_msg = view.findViewById(R.id.error_msg);
        Button login_btn = view.findViewById(R.id.login_btn);
        Button ok_btn = view.findViewById(R.id.ok_btn);

        error_msg.setText(error);
        if(action==1) ok_btn.setVisibility(View.GONE);
        else login_btn.setVisibility(View.GONE);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
