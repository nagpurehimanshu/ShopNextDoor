package com.example.shopnextdoor.Customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderPlaced extends AppCompatActivity {

    private TextView orderNumber, orderShopName, orderDate, orderType, orderMode, orderItems;
    String order_number, customer_username, customer_name, shop_name;

    URL url = new URL();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        Intent intent = getIntent();
        order_number = intent.getStringExtra("order_number");
        customer_username = intent.getStringExtra("customer_username");
        shop_name = intent.getStringExtra("shop_name");
        customer_name = intent.getStringExtra("customer_name");

        orderNumber = findViewById(R.id.order_number);
        orderShopName = findViewById(R.id.shop_name);
        orderDate = findViewById(R.id.date_of_order);
        orderType = findViewById(R.id.order_type);
        orderMode = findViewById(R.id.mode_of_delivery);
        orderItems = findViewById(R.id.order_items);

        Call<Orders> call = shopNextDoorServerAPI.getOrderDetails(order_number);
        call.enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(OrderPlaced.this, "We are unable to load data from the database.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(response.body().getResult().equals("0")){
                    Log.e("Internal Server Error!", "");
                    Toast.makeText(OrderPlaced.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                }
                else{
                    orderNumber.setText(response.body().getOrder_number());
                    orderShopName.setText(shop_name);
                    orderDate.setText(response.body().getOrder_placed_date());
                    orderType.setText(response.body().getOrder_type());
                    orderMode.setText(response.body().getOrder_mode());
                    String orderItemsOutput = "1. ";
                    int count = 2;
                    String responseDetails = response.body().getOrder_items();
                    for(int i=0; i<response.body().getOrder_items().length(); i++){
                        if(responseDetails.charAt(i)==','){
                            orderItemsOutput += " ";
                        }else if(responseDetails.charAt(i) == ';'){
                            orderItemsOutput += "\n" + Integer.toString(count++) + ". ";
                        }else{
                            orderItemsOutput += responseDetails.charAt(i);
                        }
                    }
                    orderItems.setText(orderItemsOutput);
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(OrderPlaced.this, "Unable to load data. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void btn_home(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeCustomer.class);
        intent.putExtra("customer_username", customer_username);
        intent.putExtra("customer_name", customer_name);
        startActivity(intent);
    }
}
