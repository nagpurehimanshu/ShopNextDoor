package com.example.shopnextdoor.Shop;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shopnextdoor.Adapters.Shop_OrderComplete_OnClick;
import com.example.shopnextdoor.Adapters.RecyclerAdapterPendingOrdersShop;
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

public class ShopPendingOrders extends AppCompatActivity implements Shop_OrderComplete_OnClick {

    String shop_name, shop_username;
    List<Orders> inputData = new ArrayList<Orders>();
    RecyclerView recyclerView;
    RecyclerAdapterPendingOrdersShop recyclerAdapterPendingOrdersShop;

    URL url = new URL();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_pending_orders);

        Intent intent = getIntent();
        shop_name = intent.getStringExtra("shop_name");
        shop_username = intent.getStringExtra("shop_username");

        //Recycler View
        recyclerView = findViewById(R.id.recycler_view_pending_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapterPendingOrdersShop = new RecyclerAdapterPendingOrdersShop(inputData, this);
        recyclerView.setAdapter(recyclerAdapterPendingOrdersShop);

        fetchData();
    }

    private void fetchData() {
        Call<List<Orders>> call = shopNextDoorServerAPI.getOrdersByShop(shop_username);
        call.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(ShopPendingOrders.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body().size() > 0) {
                    if(response.body().get(0).getOrder_mode().equals("0")){
                        Toast.makeText(ShopPendingOrders.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.e("Successful Response: ", response.toString());
                        for (int i = 0; i < response.body().size(); i++) {
                            if (response.body().get(i).getOrder_status().equals("accepted")) {
                                Orders orders = new Orders();
                                orders.setCustomer_name(response.body().get(i).getCustomer_name());
                                orders.setOrder_mode(response.body().get(i).getOrder_mode());
                                orders.setOrder_number(response.body().get(i).getOrder_number());
                                orders.setOrder_placed_date(response.body().get(i).getOrder_placed_date());
                                orders.setOrder_status(response.body().get(i).getOrder_status());
                                orders.setShop_name(response.body().get(i).getShop_name());
                                orders.setAmount(response.body().get(i).getAmount());
                                orders.setOrder_acceptance_date(response.body().get(i).getOrder_acceptance_date());
                                orders.setOrder_completion_date(response.body().get(i).getOrder_completion_date());
                                orders.setOrder_type(response.body().get(i).getOrder_type());
                                orders.setResult(response.body().get(i).getResult());
                                orders.setCustomer_username(response.body().get(i).getCustomer_username());
                                orders.setShop_username(response.body().get(i).getShop_username());
                                orders.setResult(response.body().get(i).getResult());

                                String responseDetails = response.body().get(i).getOrder_items();
                                String orderItemsOutput = "1. ";
                                int count = 2;
                                for (int j = 0; j < responseDetails.length(); j++) {
                                    if (responseDetails.charAt(j) == ',') {
                                        orderItemsOutput += " ";
                                    } else if (responseDetails.charAt(j) == ';') {
                                        orderItemsOutput += "\n" + Integer.toString(count++) + ". ";
                                    } else {
                                        orderItemsOutput += responseDetails.charAt(j);
                                    }
                                }
                                orders.setOrder_items(orderItemsOutput);
                                inputData.add(orders);
                            }
                        }
                    }
                }else if(response==null) {
                    Log.e("Null Response: ", response.toString());
                }
                if(inputData.size()==0) showMyDialog();
                recyclerAdapterPendingOrdersShop.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(ShopPendingOrders.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void order_complete_btn_onClick(final int position) {
        if(inputData.get(position).getAmount()==0) {
            Toast.makeText(this, "Please add amount to mark it completed.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<String> call = shopNextDoorServerAPI.updateOrderStatus(inputData.get(position).getOrder_number(), "completed", inputData.get(position).getAmount());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(ShopPendingOrders.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    return;
                }

                inputData.remove(position);
                if(inputData.size()==0) showMyDialog();
                recyclerAdapterPendingOrdersShop.notifyDataSetChanged();
                Toast.makeText(ShopPendingOrders.this, "Order completed successfully.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Failure Response: ", t.getMessage());
            }
        });
    }

    private void showMyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_no_orders, null);
        Button return_btn = view.findViewById(R.id.return_btn);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeShop.class);
                intent.putExtra("shop_name", shop_name);
                intent.putExtra("shop_username", shop_username);
                startActivity(intent);
            }
        });
    }
}
