package com.example.shopnextdoor;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.shopnextdoor.network.Shop;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeCustomer extends AppCompatActivity {
    TextView welcome, track_order, order_status, errorDisplay;
    String shopText;
    String username;
    String[] shoplist;
    Boolean shop_selected = false;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.43.13:3030")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_customer);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        welcome = findViewById(R.id.welcome);
        welcome.setText("Welcome " + username);

        order_status = findViewById(R.id.order_status);
        track_order = findViewById(R.id.track_order);
        errorDisplay = findViewById(R.id.errorDisplay);

        setOrderstatus();
        ShopList();
    }

    private void ShopList() {
        Call<Shop[]> call = shopNextDoorServerAPI.getShopList();
        call.enqueue(new Callback<Shop[]>() {
            @Override
            public void onResponse(Call<Shop[]> call, Response<Shop[]> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    return;
                }
                shoplist = new String[response.body().length];
                for(int i=0; i<response.body().length; i++){
                    shoplist[i] = response.body()[i].getName();
                }
            }

            @Override
            public void onFailure(Call<Shop[]> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
            }
        });
    }

    //Set Status of current order
    private void setOrderstatus() {
        //Order Status = 1: Idle, 2: Pending, 3: Accepted, 4: Delivered
        if(checkOrderStatus(username).equals("1")){
            order_status.setTextColor(Color.BLUE);
            order_status.setText("No Pending Orders");
        }else if(checkOrderStatus(username).equals("2")){
            order_status.setTextColor(Color.RED);
            order_status.setText("Pending");
            track_order.setVisibility(View.VISIBLE);
        }else if(checkOrderStatus(username).equals("3")){
            order_status.setTextColor(Color.YELLOW);
            order_status.setText("Accepted");
            track_order.setVisibility(View.VISIBLE);
        }else if(checkOrderStatus(username).equals("4")){
            order_status.setTextColor(Color.GREEN);
            order_status.setText("Delivered");
            track_order.setVisibility(View.VISIBLE);
        }
    }

    private String checkOrderStatus(String username) {
        return "";
    }

    //Link to show order details
    public void link_track_order(View view) {

    }

    //Place Order Button
    public void btn_place_order(View view) {
        if(shop_selected==false){
            errorDisplay.setText("Select a shop to proceed.");
        }else{
            Intent intent = new Intent(this, PlaceOrder.class);
            intent.putExtra(username, shopText);
            startActivity(intent);
        }
    }
}
