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

import com.example.shopnextdoor.Adapters.Shop_Approve_Reject_OnClick;
import com.example.shopnextdoor.Adapters.RecyclerAdapterNewOrderRequestsShop;
import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Utility.LoadingDialog;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewOrderRequests extends AppCompatActivity implements Shop_Approve_Reject_OnClick {

    String shop_name, shop_username;
    List<Orders> inputData = new ArrayList<Orders>();
    RecyclerView recyclerView;
    RecyclerAdapterNewOrderRequestsShop recyclerAdapterNewOrderRequestsShop;
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
        setContentView(R.layout.activity_shop_new_order_requests);

        Intent intent = getIntent();
        shop_name = intent.getStringExtra("shop_name");
        shop_username = intent.getStringExtra("shop_username");

        loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialog();

        //Recycler View
        recyclerView = findViewById(R.id.recycler_view_order_requests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapterNewOrderRequestsShop = new RecyclerAdapterNewOrderRequestsShop(inputData, this);
        recyclerView.setAdapter(recyclerAdapterNewOrderRequestsShop);

        fetchData();
    }

    private void fetchData() {
        Call<List<Orders>> call = shopNextDoorServerAPI.getOrdersByShop(shop_username);
        call.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                if (!response.isSuccessful()) {
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(NewOrderRequests.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body().size() > 0) {
                    if(response.body().get(0).getResult().equals("0")){
                        Toast.makeText(NewOrderRequests.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.e("Successful Response: ", response.toString());
                        for (int i = 0; i < response.body().size(); i++) {
                            if(response.body().get(i).getOrder_status().equals("pending")){
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
                                for(int j=0; j<responseDetails.length(); j++){
                                    if(responseDetails.charAt(j)==','){
                                        orderItemsOutput += " ";
                                    }else if(responseDetails.charAt(j) == ';'){
                                        orderItemsOutput += "\n" + Integer.toString(count++) + ". ";
                                    }else{
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
                loadingDialog.dismissDialog();
                if(inputData.size()==0) showMyDialog();
                recyclerAdapterNewOrderRequestsShop.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                loadingDialog.dismissDialog();
                Log.e("Failure response: ", t.getMessage());
                Toast.makeText(NewOrderRequests.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Approve and Reject button on click methods
    @Override
    public void app_rej_onClick(final int position, final boolean action) {
        loadingDialog.startDialog();
        Call<String> call;
        //Approve button clicked
        if(action==false) {
            call = shopNextDoorServerAPI.updateOrderStatus(inputData.get(position).getOrder_number(), "accepted", 0);
        }

        //Reject button clicked
        else {
            call = shopNextDoorServerAPI.updateOrderStatus(inputData.get(position).getOrder_number(), "rejected", 0);
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    Toast.makeText(NewOrderRequests.this, "Server Unresponsive at the moment.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(action==false) {
                    Toast.makeText(NewOrderRequests.this, "Order approved successfully.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(NewOrderRequests.this, "Order rejected successfully.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismissDialog();
                inputData.remove(position);
                recyclerAdapterNewOrderRequestsShop.notifyDataSetChanged();
                if(inputData.size()==0){
                    showMyDialog();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Failure Response: ", t.getMessage());
                loadingDialog.dismissDialog();
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
