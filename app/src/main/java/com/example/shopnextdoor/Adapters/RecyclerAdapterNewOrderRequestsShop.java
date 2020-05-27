package com.example.shopnextdoor.Adapters;

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

public class RecyclerAdapterNewOrderRequestsShop extends RecyclerView.Adapter<RecyclerAdapterNewOrderRequestsShop.MyViewHolder> {
    private List<Orders> ordersList;
    Shop_Approve_Reject_OnClick shopApprove_reject_onClick;
    public RecyclerAdapterNewOrderRequestsShop(List<Orders> inputData, Shop_Approve_Reject_OnClick shopApprove_reject_onClick){
        this.ordersList = inputData;
        this.shopApprove_reject_onClick = shopApprove_reject_onClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shop_order_request_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.order_number.setText(ordersList.get(position).getOrder_number());
        holder.customer_name.setText(ordersList.get(position).getCustomer_name());
        holder.mode.setText(ordersList.get(position).getOrder_mode());
        holder.placed_on.setText(ordersList.get(position).getOrder_placed_date());
        holder.order_items.setText(ordersList.get(position).getOrder_items());
        holder.expandableLayout.setDuration(200);

        if(ordersList.get(position).getOrder_mode().equals("Shop Pickup")){
            holder.address_logo.setVisibility(View.GONE);
            holder.address.setVisibility(View.GONE);
        }else{
            holder.address.setText(ordersList.get(position).getResult());
        }
    }

    @Override
    public int getItemCount() {
        if(ordersList==null) return 0;
        return ordersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView order_number, customer_name, mode, placed_on, order_items, address_logo, address;
        Button show_items_btn, approve_btn, reject_btn;
        ExpandableLayout expandableLayout;

        public MyViewHolder(final View itemView) {
            super(itemView);

            order_number = itemView.findViewById(R.id.order_number);
            customer_name = itemView.findViewById(R.id.customer_name);
            mode = itemView.findViewById(R.id.order_mode);
            placed_on = itemView.findViewById(R.id.placed_date);
            show_items_btn = itemView.findViewById(R.id.show_items_btn);
            approve_btn = itemView.findViewById(R.id.approve_btn);
            reject_btn = itemView.findViewById(R.id.reject_btn);
            address = itemView.findViewById(R.id.address);
            address_logo = itemView.findViewById(R.id.address_logo);
            order_items = itemView.findViewById(R.id.order_items_expanded);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            show_items_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandableLayout.toggle();
                }
            });

            approve_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopApprove_reject_onClick.app_rej_onClick(getAdapterPosition(), false);
                }
            });

            reject_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopApprove_reject_onClick.app_rej_onClick(getAdapterPosition(), true);
                }
            });
        }
    }
}
