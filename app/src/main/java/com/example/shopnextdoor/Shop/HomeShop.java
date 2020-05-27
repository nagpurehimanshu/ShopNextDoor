package com.example.shopnextdoor.Shop;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.LoginAndRegister.ShopLogin;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Utility.LoadingDialog;
import com.example.shopnextdoor.Utility.ManageSharedPreferences;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeShop extends AppCompatActivity {

    private String shop_username, shop_name;
    private TextView pending_orders_number, order_requests_number;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
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
        setContentView(R.layout.activity_shop_home);
        Intent intent = getIntent();

        if(intent.hasExtra("shop_username")){
            shop_username = intent.getStringExtra("shop_username");
            shop_name = intent.getStringExtra("shop_name");
        }else{
            shop_username = ManageSharedPreferences.getUsername(getApplicationContext());
            shop_name = ManageSharedPreferences.getName(getApplicationContext());
        }

        pending_orders_number = findViewById(R.id.pending_orders_number);
        order_requests_number = findViewById(R.id.order_requests_number);
        loadingDialog = new LoadingDialog(this);

        loadingDialog.startDialog();
        //Setting Navigation View variables
        setNavData();

        configureToolbar();
        configureNavigationDrawer();
        fetchData();
    }

    private void setNavData() {
        NavigationView nv = findViewById(R.id.nav_view);
        View headerView = nv.getHeaderView(0);
        TextView nav_shop_name, nav_shop_username;
        nav_shop_name = headerView.findViewById(R.id.nav_shop_name);
        nav_shop_username = headerView.findViewById(R.id.nav_username);
        nav_shop_name.setText(shop_name);
        nav_shop_username.setText(shop_username);
    }

    private void fetchData() {
        Call<List<Orders>> call = shopNextDoorServerAPI.getOrdersByShop(shop_username);
        call.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(HomeShop.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int pending = 0;
                int requests = 0;
                for(int i=0; i<response.body().size(); i++){
                    if(response.body().get(i).getOrder_status().equals("accepted")) ++pending;
                    if(response.body().get(i).getOrder_status().equals("pending")) ++requests;
                }

                pending_orders_number.setText(Integer.toString(pending));
                order_requests_number.setText(Integer.toString(requests));
                loadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                Log.e("Failure Response: ", t.getMessage());
                loadingDialog.dismissDialog();
            }
        });
    }

    //Toolbar configuration
    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                        loadingDialog.startDialog();
                        Intent intent = new Intent(getApplicationContext(), ViewPastOrdersShop.class);
                        intent.putExtra("shop_username", shop_username);
                        intent.putExtra("shop_name", shop_name);
                        startActivity(intent);
                        break;

                    //Show Account Details
                    case R.id.account_details:
                        loadingDialog.startDialog();
                        Intent intent2 = new Intent(getApplicationContext(), AccountDetailsShop.class);
                        intent2.putExtra("shop_username", shop_username);
                        intent2.putExtra("shop_name", shop_name);
                        startActivity(intent2);
                        break;

                    //When Logout is pressed
                    case R.id.logout:
                        loadingDialog.startDialog();
                        ManageSharedPreferences.saveUsername(getApplicationContext(), "");
                        ManageSharedPreferences.saveName(getApplicationContext(), "");
                        ManageSharedPreferences.saveType(getApplicationContext(), false);
                        startActivity(new Intent(getApplicationContext(), ShopLogin.class));
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
        fetchData();
    }

    //Pressing back button options
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            showMyDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_shop_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void new_order_requests(View view) {
        loadingDialog.startDialog();
        Intent intent = new Intent(getApplicationContext(), NewOrderRequests.class);
        intent.putExtra("shop_username", shop_username);
        intent.putExtra("shop_name", shop_name);
        startActivity(intent);
    }

    public void pending_orders(View view) {
        loadingDialog.startDialog();
        Intent intent = new Intent(getApplicationContext(), ShopPendingOrders.class);
        intent.putExtra("shop_username", shop_username);
        intent.putExtra("shop_name", shop_name);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myRefreshOperation();
    }

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
