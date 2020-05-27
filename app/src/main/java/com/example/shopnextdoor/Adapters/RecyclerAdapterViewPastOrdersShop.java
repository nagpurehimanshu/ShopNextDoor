package com.example.shopnextdoor.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import static android.view.View.GONE;

public class RecyclerAdapterViewPastOrdersShop extends RecyclerView.Adapter<RecyclerAdapterViewPastOrdersShop.MyViewHolder>{

    private List<Orders> ordersList;
    public RecyclerAdapterViewPastOrdersShop(List<Orders> inputData){
        this.ordersList = inputData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shop_past_order_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.order_number.setText(ordersList.get(position).getOrder_number());
        holder.customer_name.setText(ordersList.get(position).getCustomer_name());
        holder.mode.setText(ordersList.get(position).getOrder_mode());
        holder.placed_on.setText(ordersList.get(position).getOrder_placed_date());
        holder.amount.setText(Integer.toString(ordersList.get(position).getAmount()));
        holder.order_status.setText(ordersList.get(position).getOrder_status());
        holder.order_items_expanded.setText(ordersList.get(position).getOrder_items());
        holder.expandableLayout.setDuration(200);

        if(ordersList.get(position).getOrder_mode().equals("Shop Pickup")){
            holder.address_logo.setVisibility(View.GONE);
            holder.address.setVisibility(View.GONE);
        }else{
            holder.address.setText(ordersList.get(position).getResult());
        }

        if(ordersList.get(position).getOrder_status().equals("rejected")){
            holder.accepted_on.setText("Not Applicable");
            holder.completed_on.setText(ordersList.get(position).getOrder_completion_date());
            holder.order_status.setTextColor(Color.parseColor("#F44336"));
        }else{
            holder.accepted_on.setText(ordersList.get(position).getOrder_acceptance_date());
            holder.completed_on.setText(ordersList.get(position).getOrder_completion_date());
            holder.order_status.setTextColor(Color.parseColor("#009688"));
        }
    }

    @Override
    public int getItemCount() {
        if(ordersList==null) return 0;
        return ordersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView order_number, customer_name, mode, placed_on, accepted_on, completed_on, amount, order_status, order_items_expanded, address_logo, address;
        Button view_order_items;
        ExpandableLayout expandableLayout;
        public MyViewHolder(View itemView) {
            super(itemView);

            order_number = itemView.findViewById(R.id.order_number);
            customer_name = itemView.findViewById(R.id.customer_name);
            mode = itemView.findViewById(R.id.order_mode);
            placed_on = itemView.findViewById(R.id.placed_date);
            accepted_on = itemView.findViewById(R.id.accepted_date);
            completed_on = itemView.findViewById(R.id.completion_date);
            amount = itemView.findViewById(R.id.amount);
            order_status = itemView.findViewById(R.id.order_status);
            view_order_items = itemView.findViewById(R.id.view_order_items);
            address = itemView.findViewById(R.id.address);
            address_logo = itemView.findViewById(R.id.address_logo);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            order_items_expanded = itemView.findViewById(R.id.order_items_expanded);

            view_order_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandableLayout.toggle();
                }
            });
        }
    }
}
