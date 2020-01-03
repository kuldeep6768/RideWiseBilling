package com.geanysoftech.ridewisebilling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.geanysoftech.ridewisebilling.R;
import com.geanysoftech.ridewisebilling.model.PluModel;

import java.util.List;

public class PluListAdapter extends RecyclerView.Adapter<PluListAdapter.MyViewHolder> {

    private Context context;
    List<PluModel> pluModelList;
    private OnItemClickListener itemClickListener;

    public PluListAdapter(Context context, List<PluModel> pluModelList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.pluModelList = pluModelList;
        this.itemClickListener = itemClickListener;
    }

    public PluListAdapter(Context context, List<PluModel> pluModelList) {
        this.context = context;
        this.pluModelList = pluModelList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvPluName, tvPluPrice;
        CardView cvPluDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPluName = itemView.findViewById(R.id.tvPluName);
            tvPluPrice = itemView.findViewById(R.id.tvPluPrice);
            cvPluDetails = itemView.findViewById(R.id.cvPluDetails);

        }

        public void bind(final PluModel pluModel, final OnItemClickListener clickListener){

            tvPluName.setText(pluModel.getPluName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(pluModel);
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.plu_show_layout, parent, false);

        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final PluModel pluModel = pluModelList.get(position);
        holder.tvPluName.setText(pluModel.getPluName());
        holder.tvPluPrice.setText(pluModel.getPrice());

        holder.bind(pluModelList.get(position), itemClickListener);

    }

    @Override
    public int getItemCount() {
        return pluModelList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(PluModel pluModel);

    }




}
