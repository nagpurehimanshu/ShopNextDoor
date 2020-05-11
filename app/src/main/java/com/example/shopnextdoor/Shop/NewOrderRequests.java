package com.example.shopnextdoor.Shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.shopnextdoor.Adapters.RecyclerAdapterNewOrderRequests;
import com.example.shopnextdoor.Customer.ViewOrders;
import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewOrderRequests extends AppCompatActivity {

    String shop_name, shop_username;
    List<Orders> inputData = new ArrayList<Orders>();
    RecyclerView recyclerView;
    RecyclerAdapterNewOrderRequests recyclerAdapterNewOrderRequests;

    URL url = new URL();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_requests);

        Intent intent = getIntent();
        shop_name = intent.getStringExtra("shop_name");
        shop_username = intent.getStringExtra("shop_username");

        //Recycler View
        recyclerView = findViewById(R.id.recycler_view_order_requests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapterNewOrderRequests = new RecyclerAdapterNewOrderRequests(inputData);
        recyclerView.setAdapter(recyclerAdapterNewOrderRequests);

        fetchData();
    }

    private void fetchData() {
        Call<List<Orders>> call = shopNextDoorServerAPI.getActiveOrdersList(shop_username);
        call.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(NewOrderRequests.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body().size() > 0) {
                    Log.e("Successful Response: ", response.toString());
                    for (int i = 0; i < response.body().size(); i++) {
                        if(response.body().get(i).getOrder_status().equals("pending")){
                            inputData.add(new Orders());
                            inputData.get(i).setCustomer_name(response.body().get(i).getCustomer_name());
                            inputData.get(i).setOrder_items(response.body().get(i).getOrder_items());
                            inputData.get(i).setOrder_mode(response.body().get(i).getOrder_mode());
                            inputData.get(i).setOrder_number(response.body().get(i).getOrder_number());
                            inputData.get(i).setOrder_placed_date(response.body().get(i).getOrder_placed_date());
                            inputData.get(i).setOrder_status(response.body().get(i).getOrder_status());
                            inputData.get(i).setShop_name(response.body().get(i).getShop_name());
                            inputData.get(i).setAmount(response.body().get(i).getAmount());
                            inputData.get(i).setOrder_acceptance_date(response.body().get(i).getOrder_acceptance_date());
                            inputData.get(i).setOrder_completion_date(response.body().get(i).getOrder_completion_date());
                            inputData.get(i).setOrder_type(response.body().get(i).getOrder_type());
                            inputData.get(i).setResult(response.body().get(i).getResult());
                            inputData.get(i).setCustomer_username(response.body().get(i).getCustomer_username());
                            inputData.get(i).setShop_username(response.body().get(i).getShop_username());
                        }
                    }

                    recyclerAdapterNewOrderRequests.notifyDataSetChanged();
                }else if(response==null) {
                    Log.e("Null Response: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                    Log.e("Failure response: ", t.getMessage());
                    Toast.makeText(NewOrderRequests.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
