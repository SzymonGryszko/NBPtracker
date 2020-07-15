package comgryszko.szymon.nbptracker.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;


import comgryszko.szymon.nbptracker.api.GoldRate;
import comgryszko.szymon.nbptracker.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<GoldRate> mGoldRates;
    private Context context;

    public RecyclerViewAdapter(List<GoldRate> goldRates) {
        mGoldRates = goldRates;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        GoldRate currentRate = getRateAt(position);
        float difference;
        difference = currentRate.getCena() - getRateAt(position + 1).getCena();
        holder.difference.setText(String.format("%.2f", difference));
        if (difference < 0) {
            holder.difference.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.difference.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        holder.rate.setText(String.valueOf(currentRate.getCena()));
        holder.date.setText(currentRate.getData());
    }

    @Override
    public int getItemCount() {
        return mGoldRates.size() - 1;
    }

    private GoldRate getRateAt(int position) {
        return mGoldRates.get(position);
    }

    public void setGoldRatesList(List<GoldRate> goldRatesList) {
        Collections.reverse(goldRatesList);
        this.mGoldRates = goldRatesList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rate, date, difference;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rate = itemView.findViewById(R.id.text_view_amount);
            date = itemView.findViewById(R.id.text_view_date);
            difference = itemView.findViewById(R.id.difference);
        }
    }
}


