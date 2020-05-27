package com.example.shopnextdoor.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shopnextdoor.Data.Orders;
import com.example.shopnextdoor.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class RecyclerAdapterPendingOrdersShop extends RecyclerView.Adapter<RecyclerAdapterPendingOrdersShop.MyViewHolder> {

    private List<Orders> ordersList;
    Shop_OrderComplete_OnClick shopOrderComplete_onClick;
    public RecyclerAdapterPendingOrdersShop(List<Orders> inputData, Shop_OrderComplete_OnClick shopOrderComplete_onClick){
        this.ordersList = inputData;
        this.shopOrderComplete_onClick = shopOrderComplete_onClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shop_pending_orders_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.order_number.setText(ordersList.get(position).getOrder_number());
        holder.customer_name.setText(ordersList.get(position).getCustomer_name());
        holder.mode.setText(ordersList.get(position).getOrder_mode());
        holder.placed_on.setText(ordersList.get(position).getOrder_placed_date());
        holder.accepted_on.setText(ordersList.get(position).getOrder_acceptance_date());
        holder.order_items.setText(ordersList.get(position).getOrder_items());
        holder.expandableLayout.setDuration(200);
        holder.amount.setText("");

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
        TextView order_number, customer_name, mode, placed_on, accepted_on, order_items, address, address_logo;
        EditText amount;
        Button show_items_btn, completed_btn;
        ExpandableLayout expandableLayout;

        public MyViewHolder(final View itemView) {
            super(itemView);

            order_number = itemView.findViewById(R.id.order_number);
            customer_name = itemView.findViewById(R.id.customer_name);
            mode = itemView.findViewById(R.id.order_mode);
            placed_on = itemView.findViewById(R.id.order_placed_date);
            accepted_on = itemView.findViewById(R.id.order_accepted_date);
            amount = itemView.findViewById(R.id.amount);
            show_items_btn = itemView.findViewById(R.id.view_order_items);
            completed_btn = itemView.findViewById(R.id.order_complete_btn);
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

            completed_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopOrderComplete_onClick.order_complete_btn_onClick(getAdapterPosition());
                }
            });

            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {  }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.toString().equals("")) ordersList.get(getAdapterPosition()).setAmount(0);
                    else ordersList.get(getAdapterPosition()).setAmount(Integer.parseInt(s.toString()));
                }
            });
        }
    }
}
