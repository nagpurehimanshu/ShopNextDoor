package com.example.shopnextdoor.LoginAndRegister;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.Shop.HomeShop;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Data.Shop;
import com.example.shopnextdoor.Utility.LoadingDialog;
import com.example.shopnextdoor.Utility.ManageSharedPreferences;
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
    LoadingDialog loadingDialog;

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
        loadingDialog = new LoadingDialog(this);
    }

    //Getting back to the same activity
    @Override
    protected void onResume() {
        super.onResume();
        username.getText().clear();
        password.getText().clear();
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(this, WelcomePage.class));
    }

    public void btn_register(View view) {
        startActivity(new Intent(getApplicationContext(), ShopRegister.class));
    }

    public void btn_login(View view) {
        final String user = username.getText().toString();
        final String pass = password.getText().toString();

        if(user.length()<=4){
            showMyDialog("Username can not be less than 4 letters.", 2);
            return;
        }

        if(pass.length()<=6){
            showMyDialog("Password can not be less than 6 letters.", 2);
            return;
        }

        loadingDialog.startDialog();
        //Calling the API
        Call<Shop> call = shopNextDoorServerAPI.getShop(user);
        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(ShopLogin.this, "Unsuccessful Login Attempt. Please try again.", Toast.LENGTH_SHORT).show();
                    Log.e("Unsuccessful response: ", response.toString());
                    return;
                }
                loadingDialog.dismissDialog();
                String result = response.body().getResult();
                Log.e("String response: ", result);

                if(result.equals("1")){
                    Intent intent = new Intent(ShopLogin.this, VerificationPending.class);
                    startActivity(intent);
                }else if(result.equals("2")){
                    if(response.body().getPassword().equals(pass)){
                        ManageSharedPreferences.saveUsername(getApplicationContext(), user);
                        ManageSharedPreferences.saveName(getApplicationContext(), response.body().getName());
                        ManageSharedPreferences.saveType(getApplicationContext(), true);
                        Intent intent = new Intent(ShopLogin.this, HomeShop.class);
                        intent.putExtra("shop_username", user);
                        intent.putExtra("shop_name", response.body().getName());
                        startActivity(intent);
                        Toast.makeText(ShopLogin.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    }else{
                        showMyDialog("Username or Password incorrect!", 2);
                    }
                }else if(result.equals("3")){
                    showMyDialog("Your verification has been rejected by our team.", 2);
                }else if(result.equals("4")){
                    showMyDialog("Username or Password incorrect!", 2);
                }else{
                    showMyDialog("Server Error", 2);
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                loadingDialog.dismissDialog();
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(ShopLogin.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
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
