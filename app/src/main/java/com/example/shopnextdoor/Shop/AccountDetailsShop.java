package com.example.shopnextdoor.Shop;

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

import com.example.shopnextdoor.Customer.AccountDetailsCustomer;
import com.example.shopnextdoor.Data.Shop;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Utility.LoadingDialog;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountDetailsShop extends AppCompatActivity {
    String shop_username, shop_name;
    TextView username, name, owner_name, shop_type;
    EditText owner_mobile, email, address;
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
        setContentView(R.layout.activity_account_details_shop);

        Intent intent = new Intent();
        intent = getIntent();
        shop_username = intent.getStringExtra("shop_username");
        shop_name = intent.getStringExtra("shop_name");

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        owner_name = findViewById(R.id.owner_name);
        owner_mobile = findViewById(R.id.owner_mobile);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        shop_type = findViewById(R.id.type);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialog();

        fetchData();

        owner_mobile.addTextChangedListener(new TextWatcher() {
            String prev_mobile;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prev_mobile = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(prev_mobile)){
                    update = true;
                }
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
    }

    private void fetchData() {
        Call<Shop> call = shopNextDoorServerAPI.getShop(shop_username);
        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(AccountDetailsShop.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    return;
                }

                username.setText(response.body().getUsername());
                name.setText(response.body().getName());
                shop_type.setText(response.body().getShop_type());
                owner_name.setText(response.body().getOwner_name());
                owner_mobile.setText(response.body().getOwner_mobile());
                email.setText(response.body().getEmail());
                address.setText(response.body().getAddress());
                update = false;
                loadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                Toast.makeText(AccountDetailsShop.this, "Server not reachable. Please try again later.", Toast.LENGTH_SHORT).show();
                Log.e("Failure response: ", t.getMessage());
                loadingDialog.dismissDialog();
            }
        });
    }

    public void update_details_btn(View view){
        loadingDialog.startDialog();
        if(update){
            Call<String> call = shopNextDoorServerAPI.updateShop(username.getText().toString(), owner_mobile.getText().toString(), address.getText().toString(), email.getText().toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(!response.isSuccessful()){
                        Log.e("Unsuccessful response: ", response.toString());
                        Toast.makeText(AccountDetailsShop.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AccountDetailsShop.this, "Server not reachable. Please try again later.", Toast.LENGTH_SHORT).show();
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
