package com.example.shopnextdoor.LoginAndRegister;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Data.Shop;
import com.example.shopnextdoor.Utility.LoadingDialog;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopRegister extends AppCompatActivity {

    EditText name, owner_name, owner_mobile, email, address, username, password, confirmPassword;
    Spinner shop_type;
    Button loginButton;
    TextView errorDisplay, name_msg, owner_name_msg, shop_type_msg, mobile_msg, email_msg, address_msg, username_msg, password_msg;
    LoadingDialog loadingDialog;
    String shop_type_value;
    Boolean shop_type_selected = false;
    String[] order_type_list = {"Shop Type:", "Kirana", "Medical", "Bar"};

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
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        errorDisplay = findViewById(R.id.errorDisplay);
        loadingDialog = new LoadingDialog(this);
        shop_type = findViewById(R.id.shop_type);
        name_msg = findViewById(R.id.name_msg);
        owner_name_msg = findViewById(R.id.owner_name_msg);
        shop_type_msg = findViewById(R.id.shop_type_msg);
        mobile_msg = findViewById(R.id.mobile_msg);
        email_msg = findViewById(R.id.email_msg);
        address_msg = findViewById(R.id.address_msg);
        username_msg = findViewById(R.id.username_msg);
        password_msg = findViewById(R.id.password_msg);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, order_type_list);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        shop_type.setAdapter(arrayAdapter);

        shop_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    shop_type_selected = false;
                }else{
                    shop_type_value = parent.getItemAtPosition(position).toString();
                    shop_type_selected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                shop_type_selected = false;
            }
        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    name.setText(firstCapitalSentence(name.getText().toString()));
                    name_msg.setVisibility(View.GONE);
                }else{
                    name_msg.setVisibility(View.VISIBLE);
                }
            }
        });

        shop_type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    shop_type_msg.setVisibility(View.GONE);
                }else{
                    shop_type_msg.setVisibility(View.VISIBLE);
                }
            }
        });

        owner_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    owner_name.setText(firstCapitalSentence(owner_name.getText().toString()));
                    owner_name_msg.setVisibility(View.GONE);
                }else{
                    owner_name_msg.setVisibility(View.VISIBLE);
                }
            }
        });

        owner_mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    mobile_msg.setVisibility(View.GONE);
                }else{
                    mobile_msg.setVisibility(View.VISIBLE);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    email_msg.setVisibility(View.GONE);
                    email.setText(allLowerCase(email.getText().toString()));
                }else{
                    email_msg.setVisibility(View.VISIBLE);
                }
            }
        });

        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    address_msg.setVisibility(View.GONE);
                }else{
                    address_msg.setVisibility(View.VISIBLE);
                }
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    username.setText(allLowerCase(username.getText().toString()));
                    username_msg.setVisibility(View.GONE);
                }else{
                    username_msg.setVisibility(View.VISIBLE);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    password_msg.setVisibility(View.GONE);
                }else{
                    password_msg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
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
//        username.setHighlightColor(Color.parseColor("#2196F3"));
//        password.setHighlightColor(Color.parseColor("#2196F3"));
//        confirmPassword.setHighlightColor(Color.parseColor("#2196F3"));
//        name.setHighlightColor(Color.parseColor("#2196F3"));
//        owner_mobile.setHighlightColor(Color.parseColor("#2196F3"));
//        address.setHighlightColor(Color.parseColor("#2196F3"));
//        owner_name.setHighlightColor(Color.parseColor("#2196F3"));
        errorDisplay.setText("");

        String user = username.getText().toString();
        String pass = password.getText().toString();
        String shopName = name.getText().toString();
        String ownerName = owner_name.getText().toString();
        String ownerMobile = owner_mobile.getText().toString();
        String em = email.getText().toString();
        String add = address.getText().toString();
        String confPass = confirmPassword.getText().toString();

        if(shopName.equals("")){
            name.setHintTextColor(Color.RED);
            errorDisplay.setText("Shop Name required.");
        }else if(!shop_type_selected){
            errorDisplay.setText("Select a shop type.");
        }else if(ownerName==null){
            owner_name.setHintTextColor(Color.RED);
            errorDisplay.setText("Owner Name required.");
        }else if(ownerMobile.equals("")){
            owner_mobile.setHintTextColor(Color.RED);
            errorDisplay.setText("Owner Mobile Number required.");
        }else if(em.equals("")){
            email.setHintTextColor(Color.RED);
            errorDisplay.setText("Valid email address required.");
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
        loadingDialog.startDialog();
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String shopName = name.getText().toString();
        String ownerName = owner_name.getText().toString();
        String ownerMobile = owner_mobile.getText().toString();
        String em = email.getText().toString();
        String add = address.getText().toString();
        String confPass = confirmPassword.getText().toString();

        Shop shop = new Shop(user, pass, shopName, ownerName, ownerMobile, add, em, shop_type_value);
        Call<Shop> call = shopNextDoorServerAPI.registerShop(shop);
        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(ShopRegister.this, "Registration unsuccessful due to some error. Please try again after some time.", Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingDialog.dismissDialog();
                String result = response.body().getResult();
                Log.e("String response: ", result);
                if(result.equals("1")){
                    showMyDialog("Account creation request sent successfully! Wait until we verify and approve your account.", 1);
                }else if(result.equals("2")){
                    showMyDialog("Username taken! Use a different one.", 2);
                }else if(result.equals("3")){
                    showMyDialog("Internal Server Error!", 2);
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                loadingDialog.dismissDialog();
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(ShopRegister.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(getApplicationContext(), ShopLogin.class);
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

    //Function to capitalise first letter of word
    private String firstCapitalSentence(String str) {
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        String st = new String(ch);
        return st;
    }

    //Function to lowercase all letters
    private String allLowerCase(String str){
        return str.toLowerCase();
    }
}
