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

public class RecyclerAdapterActiveOrdersCustomer extends RecyclerView.Adapter<RecyclerAdapterActiveOrdersCustomer.MyViewHolder> {
    private List<Orders> ordersList;
    Customer_CancelOrder_OnClick customerCancelOrder_onClick;
    public RecyclerAdapterActiveOrdersCustomer(List<Orders> inputData, Customer_CancelOrder_OnClick customerCancelOrder_onClick){
        this.ordersList = inputData;
        this.customerCancelOrder_onClick = customerCancelOrder_onClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.customer_view_active_orders_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterActiveOrdersCustomer.MyViewHolder holder, int position) {
        holder.order_number.setText(ordersList.get(position).getOrder_number());
        holder.shop_name.setText(ordersList.get(position).getShop_name());
        holder.order_type.setText(ordersList.get(position).getOrder_type());
        holder.mode.setText(ordersList.get(position).getOrder_mode());
        holder.placed_on.setText(ordersList.get(position).getOrder_placed_date());
        holder.amount.setText(Integer.toString(ordersList.get(position).getAmount()));
        holder.order_status.setText(ordersList.get(position).getOrder_status());
        holder.order_items_expanded.setText(ordersList.get(position).getOrder_items());
        holder.expandableLayout.setDuration(200);

        if(ordersList.get(position).getOrder_mode().equals("Home Delivery")){
            holder.shop_address_logo.setVisibility(View.GONE);
            holder.shop_address.setVisibility(View.GONE);
        }else{
            holder.shop_address.setText(ordersList.get(position).getResult());
        }

        if(ordersList.get(position).getOrder_status().equals("pending")){
            holder.accepted_on.setText("Not Applicable");
            holder.completed_on.setText("Not Applicable");
            holder.order_status_info.setText("Shop has not accepted your order yet.");
            holder.order_status.setTextColor(Color.parseColor("#FFC107"));
        }else if(ordersList.get(position).getOrder_status().equals("accepted")) {
            holder.accepted_on.setText(ordersList.get(position).getOrder_acceptance_date());
            holder.completed_on.setText("Not Applicable");
            holder.order_status_info.setText("Shop has accepted your order.");
            holder.cancel_order.setVisibility(View.GONE);
            holder.order_status.setTextColor(Color.parseColor("#6200EE"));
        }
        else if(ordersList.get(position).getOrder_status().equals("rejected")){
            holder.accepted_on.setText("Not Applicable");
            holder.completed_on.setText(ordersList.get(position).getOrder_completion_date());
            holder.order_status_info.setText("Sorry to say that shop has rejected your order.");
            holder.cancel_order.setVisibility(View.GONE);
            holder.order_status.setTextColor(Color.parseColor("#F44336"));
        }else if(ordersList.get(position).getOrder_status().equals("cancelled")){
            holder.accepted_on.setText(ordersList.get(position).getOrder_acceptance_date());
            holder.completed_on.setText(ordersList.get(position).getOrder_completion_date());
            holder.order_status_info.setVisibility(View.GONE);
            holder.cancel_order.setVisibility(View.GONE);
            holder.order_status.setTextColor(Color.parseColor("#8F8F8F"));
        }else{
            holder.accepted_on.setText(ordersList.get(position).getOrder_acceptance_date());
            holder.completed_on.setText(ordersList.get(position).getOrder_completion_date());
            holder.order_status_info.setVisibility(View.GONE);
            holder.cancel_order.setVisibility(View.GONE);
            holder.order_status.setTextColor(Color.parseColor("#009688"));
        }
    }

    @Override
    public int getItemCount() {
        if(ordersList==null) return 0;
        return ordersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView order_number, shop_name, order_type, mode, placed_on, accepted_on, completed_on, amount, order_status, order_status_info, order_items_expanded, shop_address, shop_address_logo;
        Button view_order_items, cancel_order;
        ExpandableLayout expandableLayout;
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
            view_order_items = itemView.findViewById(R.id.view_order_items);
            cancel_order = itemView.findViewById(R.id.cancel_order);
            shop_address = itemView.findViewById(R.id.shop_address);
            shop_address_logo = itemView.findViewById(R.id.shop_address_logo);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            order_items_expanded = itemView.findViewById(R.id.order_items_expanded);

            view_order_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandableLayout.toggle();
                }
            });

            cancel_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerCancelOrder_onClick.cancel_order_onClick(getAdapterPosition());
                }
            });
        }
    }
}
