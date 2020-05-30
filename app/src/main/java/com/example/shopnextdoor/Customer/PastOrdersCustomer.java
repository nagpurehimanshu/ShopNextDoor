package com.example.shopnextdoor.Customer;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.Adapters.RecyclerAdapterViewPastOrdersCustomer;
import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.R;
import com.example.shopnextdoor.Utility.LoadingDialog;
import com.example.shopnextdoor.network.ShopNextDoorServerAPI;
import com.example.shopnextdoor.network.URL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PastOrdersCustomer extends AppCompatActivity {

    String customer_username, customer_name;
    RecyclerView recyclerView;
    RecyclerAdapterViewPastOrdersCustomer recyclerAdapterViewPastOrdersCustomer;
    List<Orders> inputData = new ArrayList<Orders>();
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
        setContentView(R.layout.activity_customer_past_orders);
        Intent intent = getIntent();
        customer_username = intent.getStringExtra("customer_username");
        customer_name = intent.getStringExtra("customer_name");

        recyclerView = findViewById(R.id.recycler_view_past_orders);
        loadingDialog = new LoadingDialog(this);

        loadingDialog.startDialog();
        //get Orders Data
        getOrderData();

        //Recycler View
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapterViewPastOrdersCustomer = new RecyclerAdapterViewPastOrdersCustomer(inputData);
        recyclerView.setAdapter(recyclerAdapterViewPastOrdersCustomer);
    }

    private void getOrderData() {
        Call<List<Orders>> call = shopNextDoorServerAPI.getCustomerOrders(customer_username);
        call.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                if(!response.isSuccessful()){
                    Log.e("Unsuccessful response: ", response.toString());
                    showErrorDialog("Server Unresponsive", 2);
                }else if(response.body().size()>0){
                    if(response.body().get(0).getResult().equals("0")) {
                        showErrorDialog("Server Unresponsive", 2);
                    }
                    else{
                        Log.e("Successful Response: ", response.toString());
                        for(int i=0; i<response.body().size(); i++){
                            if(response.body().get(i).getOrder_status().equals("cancelled") || response.body().get(i).getOrder_status().equals("completed") || response.body().get(i).getOrder_status().equals("rejected")) {
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
                                orders.setRejection_msg(response.body().get(i).getRejection_msg());
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
                        sortData(inputData);
                    }
                }else if(response==null) {
                    Log.e("Null Response: ", response.toString());
                }
                loadingDialog.dismissDialog();
                if(inputData.size()==0) showMyDialog();
                recyclerAdapterViewPastOrdersCustomer.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                Log.e("Failure response: ", t.getMessage());
                showErrorDialog("Server not reachable. Please check your connection.", 2);
                loadingDialog.dismissDialog();
            }
        });
    }

    private void sortData(List<Orders> inputData) {
        Collections.sort(inputData, new Comparator<Orders>() {
            @Override
            public int compare(Orders o1, Orders o2) {
                Date d1 = null, d2 = null;
                if(o1.getOrder_completion_date()!=null) {
                    try {
                        d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o1.getOrder_completion_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else if(o1.getOrder_acceptance_date()!=null) {
                    try {
                        d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o1.getOrder_acceptance_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o1.getOrder_placed_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if(o2.getOrder_completion_date()!=null) {
                    try {
                        d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o2.getOrder_completion_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else if(o2.getOrder_acceptance_date()!=null) {
                    try {
                        d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o2.getOrder_acceptance_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        d2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o2.getOrder_placed_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return d2.compareTo(d1);
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
                Intent intent = new Intent(getApplicationContext(), HomeCustomer.class);
                intent.putExtra("customer_name", customer_name);
                intent.putExtra("customer_username", customer_username);
                startActivity(intent);
            }
        });
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
