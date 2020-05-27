package com.example.shopnextdoor.Customer;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopnextdoor.Adapters.RecyclerAdapterAddItemsCustomer;
import com.example.shopnextdoor.Data.Data;
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

public class PlaceOrder extends AppCompatActivity{

    RecyclerView recyclerView;
    int INDEX = 1;

    //Types of units for the dropdown
    String[] units = {"kg", "gram", "litre", "millilitre", "count", "bottle", "packet"};
    Spinner unit_spinner;
    EditText item_entry, quantity, item_number;
    LoadingDialog loadingDialog;

    //dataStream holds item entries in the form of string
    String dataStream = "";

    String customer_username, customer_name, shop_username, shop_name, shop_type, unit_string;
    TextView shop_name_text_view, add_entry, remove_msg;

    RecyclerAdapterAddItemsCustomer recyclerAdapterAddItemsCustomer;

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
        setContentView(R.layout.activity_customer_place_order);

        Intent intent = getIntent();
        //Getting data from previous intent
        customer_username = intent.getStringExtra("customer_username");
        customer_name = intent.getStringExtra("customer_name");
        shop_username = intent.getStringExtra("shop_username");
        shop_name = intent.getStringExtra("shop_name");
        shop_type = intent.getStringExtra("shop_type");

        shop_name_text_view = findViewById(R.id.shop_name);
        orderMode = findViewById(R.id.order_mode);
        unit_spinner = findViewById(R.id.unit);
        item_entry = findViewById(R.id.item_entry);
        quantity = findViewById(R.id.quantity);
        add_entry = findViewById(R.id.add_entry);
        loadingDialog = new LoadingDialog(this);
        item_number = findViewById(R.id.item_number);
        remove_msg = findViewById(R.id.remove_msg);

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
        recyclerAdapterAddItemsCustomer = new RecyclerAdapterAddItemsCustomer(inputData);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, units);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        unit_spinner.setAdapter(adapter);

        recyclerView.setAdapter(recyclerAdapterAddItemsCustomer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_customer_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        item_entry.getText().clear();
        quantity.getText().clear();
        orderMode.clearCheck();
        clearAdapter();
    }

    private void clearAdapter() {
        inputData.clear();
        recyclerAdapterAddItemsCustomer.notifyDataSetChanged();
    }

    //Place Order Button
    public void btn_place_order(View view) {
        for (int i = 0; i < inputData.size(); i++) {
            if (inputData.get(i).getItem_name() != "" && inputData.get(i).getQuantity() != "") {
                dataStream += inputData.get(i).getItem_name() + "," + inputData.get(i).getQuantity() + "," + inputData.get(i).getUnit();
                if (i < inputData.size() - 1) dataStream += ";";
            }
        }

        orderModeType = findViewById(orderMode.getCheckedRadioButtonId());

        if (dataStream.equals("")) {
            Toast.makeText(this, "No items added.", Toast.LENGTH_SHORT).show();
        } else if (orderModeType == null) {
            Toast.makeText(this, "Select Order Mode to proceed!", Toast.LENGTH_SHORT).show();
        } else {
            openDialog();
        }
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.customer_place_order_dialog, null);
        Button place_order = view.findViewById(R.id.place_order);
        Button cancel = view.findViewById(R.id.cancel);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startDialog();
                //If dataStream is not null and orderModeType is provided, then make an API call to place order
                Call<Orders> call = shopNextDoorServerAPI.placeOrder(new Orders(dataStream, customer_username, shop_username, shop_type, orderModeType.getText().toString(), customer_name, shop_name));
                call.enqueue(new Callback<Orders>() {
                    @Override
                    public void onResponse(Call<Orders> call, Response<Orders> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Unsuccessful response: ", response.toString());
                            Toast.makeText(PlaceOrder.this, "Order Request can not be processed.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (response.body().getResult().equals("0")) {
                                Log.e("Server ran into error", "");
                                Toast.makeText(PlaceOrder.this, "Unable to process your order currently. Please try after some time.", Toast.LENGTH_SHORT).show();
                            } else {
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
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void click_add_entry(View view) {
        if (INDEX <= 25) {
            if (!(item_entry.getText().toString().equals("") || quantity.getText().toString().equals(""))) {
                inputData.add(new Data(Integer.toString(INDEX++) + ".", item_entry.getText().toString(), quantity.getText().toString(), unit_string));
                recyclerAdapterAddItemsCustomer.notifyDataSetChanged();
                item_entry.getText().clear();
                quantity.getText().clear();
            } else {
                Toast.makeText(this, "Item Name and quantity can not be empty.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Maximum 25 items allowed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void click_remove_entry(View view) {
        int idx = Integer.parseInt(item_number.getText().toString());
        if(item_number.getText().toString().equals("")){
            remove_msg.setVisibility(View.VISIBLE);
            return;
        }
        remove_msg.setVisibility(View.GONE);

        if(idx > inputData.size()){
            Toast.makeText(this, "Item number not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputData.size() > 0) {
            for(int i=idx-1; i<inputData.size()-1; i++){
                inputData.set(i, inputData.get(i+1));
                inputData.get(i).setItem_number(Integer.toString(i+1));
            }
            inputData.remove(inputData.size() - 1);
            INDEX = inputData.size()+1;
            recyclerAdapterAddItemsCustomer.notifyDataSetChanged();
        }
    }
}

