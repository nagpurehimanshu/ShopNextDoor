package com.example.shopnextdoor.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.shopnextdoor.Data.Data;
import com.example.shopnextdoor.R;

import java.util.List;

public class RecyclerAdapterAddItems extends RecyclerView.Adapter<RecyclerAdapterAddItems.MyViewHolder> {

    //inputData stores the all the entries in the form of lists of Data
    private List<Data> inputData;

    public RecyclerAdapterAddItems(List<Data> inputData){
        this.inputData = inputData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.shopping_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.index_result.setText(inputData.get(position).getItem_number());
        holder.item_name_result.setText(inputData.get(position).getItem_name());
        holder.quantity_result.setText(inputData.get(position).getQuantity());
        holder.unit_result.setText(inputData.get(position).getUnit());
    }

    @Override
    public int getItemCount() {
        return inputData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView index_result, item_name_result, quantity_result, unit_result;

        public MyViewHolder(final View itemView) {
            super(itemView);
            index_result = itemView.findViewById(R.id.index_result);
            item_name_result = itemView.findViewById(R.id.item_name_result);
            quantity_result = itemView.findViewById(R.id.quantity_result);
            unit_result = itemView.findViewById(R.id.unit_result);
        }
    }
}
