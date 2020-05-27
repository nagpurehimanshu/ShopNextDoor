package com.example.shopnextdoor.Customer;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.Data.Customer;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Utility.LoadingDialog;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountDetailsCustomer extends AppCompatActivity {
    String customer_username, customer_name;
    TextView username, gender, name;
    EditText mobile, address, email;
    Boolean update = false;
    LoadingDialog loadingDialog;

    URL url = new URL();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details_customer);

        Intent intent = new Intent();
        intent = getIntent();
        customer_username = intent.getStringExtra("customer_username");
        customer_name = intent.getStringExtra("customer_name");

        username = findViewById(R.id.username);
        gender = findViewById(R.id.gender);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        loadingDialog = new LoadingDialog(this);

        mobile.addTextChangedListener(new TextWatcher() {
            String prev_mobile;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prev_mobile = s.toString();
                Log.e("Before text changed", prev_mobile);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(prev_mobile)){
                    update = true;
                }
                Log.e("After text changed", s.toString());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            String prev_email;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prev_email = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(prev_email)){
                    update = true;
                }
            }
        });

        address.addTextChangedListener(new TextWatcher() {
            String prev_address;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prev_address = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(prev_address)){
                    update = true;
                }
            }
        });

        fetchData();
    }

    private void fetchData() {
        loadingDialog.startDialog();
        Call<Customer> call = shopNextDoorServerAPI.getCustomer(customer_username);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(AccountDetailsCustomer.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    return;
                }

                username.setText(response.body().getUsername());
                gender.setText(response.body().getGender());
                name.setText(response.body().getName());
                mobile.setText(response.body().getMobile());
                email.setText(response.body().getEmail());
                address.setText(response.body().getAddress());
                update = false;
                loadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(AccountDetailsCustomer.this, "Server not reachable. Please try again later.", Toast.LENGTH_SHORT).show();
                Log.e("Failure response: ", t.getMessage());
                loadingDialog.dismissDialog();
            }
        });
    }

    public void update_details_btn(View view){
        loadingDialog.startDialog();
        if(update){
            Call<String> call = shopNextDoorServerAPI.updateCustomer(username.getText().toString(), mobile.getText().toString(), address.getText().toString(), email.getText().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(!response.isSuccessful()){
                        Log.e("Unsuccessful response: ", response.toString());
                        Toast.makeText(AccountDetailsCustomer.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    loadingDialog.dismissDialog();
                    if(response.body().equals("1")){
                        showSuccessDialog("Details updated successfully.");
                        update = false;
                    }else if(response.body().equals("-1")){
                        showErrorDialog("Could not process your request.", 2);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(AccountDetailsCustomer.this, "Server not reachable. Please try again later.", Toast.LENGTH_SHORT).show();
                    Log.e("Failure response: ", t.getMessage());
                    loadingDialog.dismissDialog();
                }
            });
        }else{
            loadingDialog.dismissDialog();
            showErrorDialog("No changes found.", 2);
        }
    }

    //Show registration error dialog (action: 1 for login button, 2 for ok button)
    private void showErrorDialog(String error, int action) {
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

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //Show success dialog
    private void showSuccessDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_success, null);
        TextView msg = view.findViewById(R.id.msg);
        Button ok_btn = view.findViewById(R.id.ok_btn);
        msg.setText(str);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
