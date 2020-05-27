package com.example.shopnextdoor.Customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.Data.Shop;
import com.example.shopnextdoor.LoginAndRegister.WelcomePage;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Utility.LoadingDialog;
import com.example.shopnextdoor.Utility.ManageSharedPreferences;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeCustomer extends AppCompatActivity{
    TextView welcome, order_count, errorDisplay, nav_customer_name, nav_customer_username;
    Spinner shop_list_dropdown;
    String customer_username, customer_name, selected_shop_name, selected_shop_username, selected_shop_type;
    Boolean shop_selected = false;

    LoadingDialog loadingDialog;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    List<String> shopNameList = new ArrayList<String>();
    List<String> shopUsernameList = new ArrayList<String>();
    List<String> shopTypeList = new ArrayList<String>();
    int proceedWithOrder = 0;
    URL url = new URL();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        //Get Intent Data
        Intent intent = getIntent();
        if(intent.hasExtra("customer_username")){
            customer_username = intent.getStringExtra("customer_username");
            customer_name = intent.getStringExtra("customer_name");
        }else{
            customer_username = ManageSharedPreferences.getUsername(getApplicationContext());
            customer_name = ManageSharedPreferences.getName(getApplicationContext());
        }

        welcome = findViewById(R.id.welcome);
        welcome.setText("Welcome, " + customer_name);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialog();
        //Setting Navigation View variables
        setNavData();

        order_count = findViewById(R.id.order_status);
        errorDisplay = findViewById(R.id.errorDisplay);

        configureToolbar();
        configureNavigationDrawer();
        setOrderCount();
        extract_shop_list();
        shopNameList.add("No shops selected");
        shopUsernameList.add("null");
        shopTypeList.add("null");

        shop_list_dropdown = findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, shopNameList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        shop_list_dropdown.setAdapter(adapter);

        shop_list_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                errorDisplay.setText("");
                if(position==0) shop_selected = false;
                else{
                    loadingDialog.startDialog();
                    Log.e("Starting dialog", "");
                    shop_selected = true;
                    selected_shop_name = parent.getItemAtPosition(position).toString();
                    selected_shop_username = shopUsernameList.get(shopNameList.indexOf(selected_shop_name));
                    selected_shop_type = shopTypeList.get(shopNameList.indexOf(selected_shop_name));

                    Call<String> call = shopNextDoorServerAPI.getActiveOrderWithShop(customer_username, selected_shop_username);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(!response.isSuccessful()){
                                Log.e("Unsuccessful response: ", response.toString());
                                return;
                            }
                            Log.e("Successful response: ", response.toString());
                            if(response.body().toString().equals("0")){
                                proceedWithOrder = 1;
                            }else if(response.body().toString().equals("1")){
                                proceedWithOrder = 2;
                            }
                            loadingDialog.dismissDialog();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("Failure response: " , t.getMessage());
                            loadingDialog.dismissDialog();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_shop_name = parent.getItemAtPosition(0).toString();
                selected_shop_username = shopUsernameList.get(shopNameList.indexOf(selected_shop_name));
                selected_shop_type = shopTypeList.get(shopNameList.indexOf(selected_shop_name));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_customer_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setNavData() {
        NavigationView nv = findViewById(R.id.nav_view);
        View headerView = nv.getHeaderView(0);
        TextView nav_customer_name, nav_customer_username;
        nav_customer_name = headerView.findViewById(R.id.nav_customer_name);
        nav_customer_username = headerView.findViewById(R.id.nav_username);
        nav_customer_name.setText(customer_name);
        nav_customer_username.setText(customer_username);
    }

    //Toolbar configuration
    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Navigation Drawer configuration
    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    //When View Orders is pressed
                    case R.id.view_past_orders:
                        Intent intent = new Intent(getApplicationContext(), PastOrdersCustomer.class);
                        intent.putExtra("customer_username", customer_username);
                        intent.putExtra("customer_name", customer_name);
                        startActivity(intent);
                        break;
                    //Show Account Details
                    case R.id.account_details:
                        Intent intent2 = new Intent(getApplicationContext(), AccountDetailsCustomer.class);
                        intent2.putExtra("customer_username", customer_username);
                        intent2.putExtra("customer_name", customer_name);
                        startActivity(intent2);
                        break;

                    //When Logout is pressed
                    case R.id.logout:
                        ManageSharedPreferences.saveUsername(getApplicationContext(), "");
                        ManageSharedPreferences.saveName(getApplicationContext(), "");
                        ManageSharedPreferences.saveType(getApplicationContext(), false);
                        startActivity(new Intent(getApplicationContext(), WelcomePage.class));
                        break;

                    //When Exit is pressed
                    case R.id.exit:
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        if(item.getItemId()==R.id.refresh){
            SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(this);
            swipeRefreshLayout.setRefreshing(true);
            myRefreshOperation();
        }
        return super.onOptionsItemSelected(item);
    }

    private void myRefreshOperation() {
        loadingDialog.startDialog();
        setOrderCount();
        extract_shop_list();
    }

    //Pressing back button options
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            showMyDialog();
        }
    }

    //Getting back to the same activity
    @Override
    protected void onResume() {
        loadingDialog.startDialog();
        super.onResume();
        setOrderCount();
        extract_shop_list();
    }

    //Extracting shop list information from the database using API call
    private void extract_shop_list() {
        Call<List<Shop>> call = shopNextDoorServerAPI.getShopList();
        call.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                shopNameList.subList(1, shopNameList.size()).clear();
                shopUsernameList.subList(1, shopUsernameList.size()).clear();
                shopTypeList.subList(1, shopTypeList.size()).clear();

                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(HomeCustomer.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(response.body().get(0).getResult().equals("0")){
                       Log.e("Internal Server error", "");
                    return;
                }

                for(int i=0; i<response.body().size(); i++){
                    shopNameList.add(response.body().get(i).getName());
                    shopUsernameList.add(response.body().get(i).getUsername());
                    shopTypeList.add(response.body().get(i).getShop_type());
                }

                loadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                Toast.makeText(HomeCustomer.this, "Server not reachable. Please try again later.", Toast.LENGTH_SHORT).show();
                Log.e("Failure response: ", t.getMessage());
                loadingDialog.dismissDialog();
            }
        });
    }

    //Set the order count for the customer
    private void setOrderCount() {
        Call<Orders> call = shopNextDoorServerAPI.getActiveOrderCount(customer_username);
        call.enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    return;
                }else{
                    //if(response.body().getResult()==null) Log.e("Null response: ", response.body().toString());
                    if(response.body().getResult().equals("-1")){
                        //Toast.makeText(HomeCustomer.this, "Internal Server Error!", Toast.LENGTH_SHORT).show();
                    }else{
                        order_count.setText(response.body().getResult());
                    }
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
                //Toast.makeText(HomeCustomer.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Place Order Button
    public void btn_place_order(View view) {
        errorDisplay.setText("");

        if(shop_selected==false) {
            Toast.makeText(this, "No shops selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(proceedWithOrder==0){
            errorDisplay.setText("Internal Server Error. Could not proceed.");
        }else if(proceedWithOrder==1){
            Intent intent = new Intent(this, PlaceOrder.class);
            intent.putExtra("customer_username", customer_username);
            intent.putExtra("customer_name", customer_name);
            intent.putExtra("shop_name", selected_shop_name);
            intent.putExtra("shop_username", selected_shop_username);
            intent.putExtra("shop_type", selected_shop_type);
            startActivity(intent);
        }else{
            errorDisplay.setText("You already have an active order with this shop. Select a different shop to proceed.");
        }

    }

    public void active_orders(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewActiveOrders.class);
        intent.putExtra("customer_name", customer_name);
        intent.putExtra("customer_username", customer_username);
        startActivity(intent);
    }

    //Show registration error dialog (action: 1 for login button, 2 for ok button)
    private void showMyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_exit, null);
        Button exit_btn = view.findViewById(R.id.exit_btn);
        Button cancel_btn = view.findViewById(R.id.cancel_btn);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
