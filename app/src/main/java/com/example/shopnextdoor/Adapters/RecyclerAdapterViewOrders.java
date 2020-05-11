package com.example.shopnextdoor.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.R;

import java.util.List;

public class RecyclerAdapterViewOrders extends RecyclerView.Adapter<RecyclerAdapterViewOrders.MyViewHolder>{

    private List<Orders> ordersList;
    public RecyclerAdapterViewOrders (List<Orders> inputData){
        this.ordersList = inputData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_orders_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.order_number.setText(ordersList.get(position).getOrder_number());
        holder.shop_name.setText("Shop Name: " + ordersList.get(position).getShop_name());
        holder.order_type.setText("Order Type: " + ordersList.get(position).getOrder_type());
        holder.mode.setText("Mode: " + ordersList.get(position).getOrder_mode());
        holder.placed_on.setText("Placed on: " + ordersList.get(position).getOrder_placed_date());
        holder.amount.setText("Amount: " + Integer.toString(ordersList.get(position).getAmount()));
        holder.order_status.setText(ordersList.get(position).getOrder_status());

        if(ordersList.get(position).getOrder_status().equals("pending")){
            holder.accepted_on.setText("Accepted on: Not Applicable");
            holder.completed_on.setText("Completed on: Not Applicable");
            holder.order_status_info.setText("Shop has not accepted your order yet.");
            holder.order_status.setTextColor(Color.parseColor("#FFC107"));
        }else if(ordersList.get(position).getOrder_status().equals("accepted")) {
            holder.accepted_on.setText(ordersList.get(position).getOrder_acceptance_date());
            holder.completed_on.setText("Completed on: Not Applicable");
            holder.order_status_info.setText("Shop has accepted your order.");
            holder.order_status.setTextColor(Color.parseColor("#6200EE"));
        }
        else if(ordersList.get(position).getOrder_status().equals("rejected")){
            holder.accepted_on.setText("Accepted on: Not Applicable");
            holder.completed_on.setText(ordersList.get(position).getOrder_completion_date());
            holder.order_status_info.setText("Sorry to say that shop has rejected your order.");
            holder.order_status.setTextColor(Color.parseColor("#F44336"));
        }
        else{
            holder.accepted_on.setText("Accepted on: " + ordersList.get(position).getOrder_acceptance_date());
            holder.completed_on.setText("Completed on: " + ordersList.get(position).getOrder_completion_date());
            holder.order_status_info.setVisibility(View.GONE);
            holder.order_status.setTextColor(Color.parseColor("#009688"));
        }
    }

    @Override
    public int getItemCount() {
        if(ordersList==null) return 0;
        return ordersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView order_number, shop_name, order_type, mode, placed_on, accepted_on, completed_on, amount, order_status, order_status_info;
        public MyViewHolder(View itemView) {
            super(itemView);

            order_number = itemView.findViewById(R.id.order_number);
            shop_name = itemView.findViewById(R.id.shop_name);
            order_type = itemView.findViewById(R.id.order_type);
            mode = itemView.findViewById(R.id.order_mode);
            placed_on = itemView.findViewById(R.id.placed_date);
            accepted_on = itemView.findViewById(R.id.accepted_date);
            completed_on = itemView.findViewById(R.id.completion_date);
            amount = itemView.findViewById(R.id.amount);
            order_status = itemView.findViewById(R.id.order_status);
            order_status_info = itemView.findViewById(R.id.order_status_info);
        }
    }
}
