package comgryszko.szymon.nbptracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import comgryszko.szymon.nbptracker.utils.RecyclerViewAdapter;

public class GoldRateFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold_rate, container, false);

        ArrayList<GoldRate> goldRates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            goldRates.add(new GoldRate("1", 1));
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(goldRates);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
