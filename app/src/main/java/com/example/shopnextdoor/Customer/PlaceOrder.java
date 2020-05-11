package com.example.shopnextdoor.Customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.Adapters.RecyclerAdapterAddItems;
import com.example.shopnextdoor.Data.Data;
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

public class PlaceOrder extends AppCompatActivity {

    RecyclerView recyclerView;
    int INDEX = 1;

    //Types of units for the dropdown
    String[] units = {"kg", "grams", "litre", "millilitre", "count", "bottles", "packet"};
    Spinner unit_spinner;
    EditText item_entry, quantity;

    String customer_username, customer_name, shop_username, shop_name, unit_string;
    TextView shop_name_text_view, add_entry;

    RecyclerAdapterAddItems recyclerAdapterAddItems;

    List<Data> inputData = new ArrayList<Data>();
    RadioGroup orderMode;
    RadioButton orderModeType;

    URL url = new URL();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ShopNextDoorServerAPI shopNextDoorServerAPI = retrofit.create(ShopNextDoorServerAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        Intent intent = getIntent();

        //Getting data from previous intent
        customer_username = intent.getStringExtra("customer_username");
        customer_name = intent.getStringExtra("customer_name");
        shop_username = intent.getStringExtra("shop_username");
        shop_name = intent.getStringExtra("shop_name");

        shop_name_text_view = findViewById(R.id.shop_name);
        orderMode = findViewById(R.id.order_mode);
        unit_spinner = findViewById(R.id.unit);
        item_entry = findViewById(R.id.item_entry);
        quantity = findViewById(R.id.quantity);
        add_entry = findViewById(R.id.add_entry);

        //Setting Shop Name at the top
        shop_name_text_view.setText(shop_name);

        //Putting values to spinner
        unit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit_string = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                unit_string = "kg";
            }
        });

        //Recycler View
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapterAddItems = new RecyclerAdapterAddItems(inputData);

        //Clear old data on pause
        onPause();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit_spinner.setAdapter(adapter);

        recyclerView.setAdapter(recyclerAdapterAddItems);
    }

    @Override
    public void onPause(){
        super.onPause();
        item_entry.getText().clear();
        quantity.getText().clear();
        orderMode.clearCheck();
        clearAdapter();
    }

    private void clearAdapter() {
        int n = inputData.size();
        for(int i=0; i<n; i++){
            inputData.clear();
        }
        recyclerAdapterAddItems.notifyItemRangeRemoved(0,n);
    }

    //Place Order Button
    public void btn_place_order(View view) {
        //dataStream holds item entries in the form of string
        String dataStream = "";

        for(int i=0; i<inputData.size(); i++){
            if(inputData.get(i).getItem_name()!="" && inputData.get(i).getQuantity()!=""){
                dataStream += inputData.get(i).getItem_name()+","+inputData.get(i).getQuantity()+","+inputData.get(i).getUnit();
                if(i<inputData.size()-1) dataStream += ";";
            }
        }

        orderModeType = findViewById(orderMode.getCheckedRadioButtonId());

        if(dataStream==""){
            Toast.makeText(this, "Add items properly to place an order!", Toast.LENGTH_SHORT).show();
        }else if(orderModeType==null){
            Toast.makeText(this, "Select Order Mode to proceed!", Toast.LENGTH_SHORT).show();
        }else{
            //If dataStream is not null and orderModeType is provided, then make an API call to place order
            Call<Orders> call = shopNextDoorServerAPI.placeOrder(new Orders(dataStream, customer_username, shop_username, "groceries", orderModeType.getText().toString(), customer_name, shop_name));
            call.enqueue(new Callback<Orders>() {
                @Override
                public void onResponse(Call<Orders> call, Response<Orders> response) {
                    if(!response.isSuccessful()){
                        Log.e("Unsuccessful response: ", response.toString());
                        Toast.makeText(PlaceOrder.this, "Order Request can not be processed.", Toast.LENGTH_SHORT).show();
                    }else{
                        if(response.body().getResult().equals("0")){
                            Log.e("Server ran into error", "");
                            Toast.makeText(PlaceOrder.this, "Unable to process your order currently. Please try after some time.", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(getApplicationContext(), OrderPlaced.class);
                            intent.putExtra("customer_username", customer_username);
                            intent.putExtra("customer_name", customer_name);
                            intent.putExtra("shop_name", shop_name);
                            intent.putExtra("order_number", response.body().getOrder_number());
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Orders> call, Throwable t) {
                    Log.e("Failure response: ", t.getMessage());
                    Toast.makeText(PlaceOrder.this, "Server not reachable. Please check your connection.", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    public void click_add_entry(View view) {
        if(INDEX<=25){
            inputData.add(new Data(Integer.toString(INDEX++) + ".", item_entry.getText().toString(), quantity.getText().toString(), unit_string));
            recyclerAdapterAddItems.notifyDataSetChanged();
            item_entry.getText().clear();
            quantity.getText().clear();
        }else {
            Toast.makeText(this, "Maximum 25 items allowed.", Toast.LENGTH_SHORT).show();
        }
    }
}
