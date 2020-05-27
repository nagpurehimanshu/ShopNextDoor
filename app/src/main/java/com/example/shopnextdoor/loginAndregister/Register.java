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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Data.Customer;
import com.example.shopnextdoor.Utility.LoadingDialog;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    EditText username, password, confirmPassword, name, mobile, email, address;
    RadioGroup gender;
    RadioButton genderVal;
    Button loginButton;
    TextView error, genderTitle, name_msg, mobile_msg, email_msg, address_msg, username_msg, password_msg;
    LoadingDialog loadingDialog;

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
        setContentView(R.layout.activity_customer_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        gender = findViewById(R.id.gender);
        genderTitle = findViewById(R.id.genderTitle);
        error = findViewById(R.id.errorDisplay);
        loadingDialog = new LoadingDialog(this);
        name_msg = findViewById(R.id.name_msg);
        mobile_msg = findViewById(R.id.mobile_msg);
        email_msg = findViewById(R.id.email_msg);
        address_msg = findViewById(R.id.address_msg);
        username_msg = findViewById(R.id.username_msg);
        password_msg = findViewById(R.id.password_msg);

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

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                name.setText(firstCapitalSentence(name.getText().toString()));
                name_msg.setVisibility(View.GONE);
            }
        });

        mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        mobile.getText().clear();
        address.getText().clear();
        error.setText("");
        gender.clearCheck();
        email.getText().clear();
    }

    //Register Button
    public void btn_register(View view){
        loadingDialog.startDialog();
        //Setting field colors to default values
//        username.setHighlightColor(Color.parseColor("#2196F3"));
//        password.setHighlightColor(Color.parseColor("#2196F3"));
//        confirmPassword.setHighlightColor(Color.parseColor("#2196F3"));
//        name.setHighlightColor(Color.parseColor("#2196F3"));
//        mobile.setHighlightColor(Color.parseColor("#2196F3"));
//        address.setHighlightColor(Color.parseColor("#2196F3"));
//        genderTitle.setHighlightColor(Color.parseColor("#2196F3"));
        error.setText("");

        String user = username.getText().toString();
        String pass = password.getText().toString();
        String confPass = confirmPassword.getText().toString();
        String nameVal = name.getText().toString();
        String mob = mobile.getText().toString();
        String em = email.getText().toString();
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
        }else if(em.equals("")){
            email.setHintTextColor(Color.RED);
            error.setText("Valid email address required.");
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
        loadingDialog.dismissDialog();
    }

    //Function to register customer by calling API
    private void register() {
        loadingDialog.startDialog();
        //Toast.makeText(this, "Entered Register Function!", Toast.LENGTH_SHORT).show();
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String nameVal = name.getText().toString();
        String mob = mobile.getText().toString();
        String em = email.getText().toString();
        String add = address.getText().toString();
        String genderT = genderVal.getText().toString();
        String err = error.getText().toString();

        Customer customer = new Customer(user, pass, nameVal, genderT, mob, em, add);

        //Calling the API
        Call<Customer> call = shopNextDoorServerAPI.registerCustomer(customer);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                loadingDialog.dismissDialog();
                if(!response.isSuccessful()){
                    Toast.makeText(Register.this, "Registration unsuccessful due to some error. Please try again after some time.", Toast.LENGTH_SHORT).show();
                    Log.e("Unsuccessful response: ", response.toString());
                    return;
                }

                String result = response.body().getResult();
                Log.e("String response: ", result);
                if(result.equals("1")){
                    showMyDialog("Customer created successfully!", 1);
                }else if(result.equals("2")){
                    showMyDialog("Mobile number already registered with a different account!", 2);
                }else if(result.equals("3")){
                    showMyDialog("Username taken! Use a different one.", 2);
                }else if(result.equals("4")){
                    showMyDialog("Internal Server Error!", 2);
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                loadingDialog.dismissDialog();
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(Register.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
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
