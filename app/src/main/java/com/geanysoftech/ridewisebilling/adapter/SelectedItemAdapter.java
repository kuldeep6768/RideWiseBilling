package com.geanysoftech.ridewisebilling.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geanysoftech.ridewisebilling.R;
import com.geanysoftech.ridewisebilling.model.SelectedItemModel;

import java.util.List;

public class SelectedItemAdapter extends RecyclerView.Adapter<SelectedItemAdapter.MySelectedItem> {

    private Context context;
    private List<SelectedItemModel> selectedItemModelList;
    private OnItemClickListener onItemClickListener;

    public SelectedItemAdapter(Context context, List<SelectedItemModel> selectedItemModelList) {
        this.context = context;
        this.selectedItemModelList = selectedItemModelList;
    }

    public SelectedItemAdapter(Context context, List<SelectedItemModel> selectedItemModelList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.selectedItemModelList = selectedItemModelList;
        this.onItemClickListener = onItemClickListener;
    }

    public class MySelectedItem extends RecyclerView.ViewHolder {

        TextView tvItemName, tvQuantity, tvRate, tvAmount;

        public MySelectedItem(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvAmount = itemView.findViewById(R.id.tvAmount);

        }

        public void bind(final SelectedItemModel selectedItemModel, final OnItemClickListener onItemClickListener){
            tvQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(selectedItemModel);
                    tvQuantity.setTextColor(Color.WHITE);
                    tvQuantity.setBackgroundColor(Color.BLACK);
                }
            });
        }

    }

    @NonNull
    @Override
    public MySelectedItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.selected_item_layout, parent, false);

        return new MySelectedItem(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MySelectedItem holder, int position) {

        SelectedItemModel selectedItemModel = selectedItemModelList.get(position);
        holder.tvItemName.setText(selectedItemModel.getPluName());
        holder.tvQuantity.setText(selectedItemModel.getQuantity());
        holder.tvRate.setText(selectedItemModel.getPrice());
        holder.tvAmount.setText(selectedItemModel.getAmount());

        holder.bind(selectedItemModelList.get(position), onItemClickListener);

        int currentPosition = position;
        //addItem(currentPosition, selectedItemModel);

        //removeItem(selectedItemModel);

    }

    private void removeItem(SelectedItemModel selectedItemModel) {

        int position = selectedItemModelList.indexOf(selectedItemModel);
        selectedItemModelList.remove(position);
        notifyItemRemoved(position);

    }

    private void addItem(int currentPosition, SelectedItemModel selectedItemModel) {

        selectedItemModelList.add(currentPosition, selectedItemModel);
        notifyItemInserted(currentPosition);

    }

    @Override
    public int getItemCount() {
        return selectedItemModelList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(SelectedItemModel selectedItemModel);
    }


}
