package comgryszko.szymon.nbptracker.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import comgryszko.szymon.nbptracker.GoldRate;
import comgryszko.szymon.nbptracker.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<GoldRate> mGoldRates;

    public RecyclerViewAdapter(ArrayList<GoldRate> goldRates) {
        mGoldRates = goldRates;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        GoldRate currentRate = getRateAt(position);
        holder.rate.setText(String.valueOf(currentRate.getCena()));
        holder.date.setText(currentRate.getData());
    }

    @Override
    public int getItemCount() {
        return mGoldRates.size();
    }

    public GoldRate getRateAt(int position) {
        return mGoldRates.get(position);
    }

    static class  ViewHolder extends RecyclerView.ViewHolder{
        private TextView rate, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rate = itemView.findViewById(R.id.text_view_amount);
            date = itemView.findViewById(R.id.text_view_date);
        }
    }
}


