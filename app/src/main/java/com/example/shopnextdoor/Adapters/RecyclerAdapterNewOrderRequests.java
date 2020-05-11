package com.example.shopnextdoor.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.R;
import com.github.aakira.expandablelayout.ExpandableLayout;

import java.util.List;

public class RecyclerAdapterNewOrderRequests extends RecyclerView.Adapter<RecyclerAdapterNewOrderRequests.MyViewHolder> {
    private List<Orders> ordersList;
    public RecyclerAdapterNewOrderRequests (List<Orders> inputData){
        this.ordersList = inputData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_request_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.order_number.setText(ordersList.get(position).getOrder_number());
        holder.customer_name.setText(ordersList.get(position).getShop_name());
        holder.mode.setText(ordersList.get(position).getOrder_mode());
        holder.placed_on.setText(ordersList.get(position).getOrder_placed_date());
        holder.amount.setText(Integer.toString(ordersList.get(position).getAmount()));
        holder.order_items.setText(ordersList.get(position).getOrder_items());
        holder.expandableLayout.setExpanded(false);
        holder.expandableLayout.setDuration(200);
    }

    @Override
    public int getItemCount() {
        if(ordersList==null) return 0;
        return ordersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView order_number, customer_name, mode, placed_on, amount, order_items;
        Button show_items_btn, approve_btn, reject_btn;
        ExpandableLayout expandableLayout;

        public MyViewHolder(final View itemView) {
            super(itemView);

            order_number = itemView.findViewById(R.id.order_number);
            customer_name = itemView.findViewById(R.id.customer_name);
            mode = itemView.findViewById(R.id.order_mode);
            placed_on = itemView.findViewById(R.id.placed_date);
            amount = itemView.findViewById(R.id.amount);
            show_items_btn = itemView.findViewById(R.id.show_items_btn);
            approve_btn = itemView.findViewById(R.id.approve_btn);
            reject_btn = itemView.findViewById(R.id.reject_btn);
            order_items = itemView.findViewById(R.id.order_items_expanded);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            show_items_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandableLayout.toggle();
                }
            });
        }
    }
}
