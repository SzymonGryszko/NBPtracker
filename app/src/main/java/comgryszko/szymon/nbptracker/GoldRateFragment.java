package comgryszko.szymon.nbptracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comgryszko.szymon.nbptracker.api.GoldRate;
import comgryszko.szymon.nbptracker.api.NBPApi;
import comgryszko.szymon.nbptracker.utils.RecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GoldRateFragment extends Fragment implements Callback<List<GoldRate>> {

    private static final String BASE_URL_EXCHANGERATE = "https://api.nbp.pl/api/cenyzlota/";
    private static final String TAG = "GoldRateFragment";
    private RecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold_rate, container, false);

        List<GoldRate> goldRates = new ArrayList<>();
        adapter = new RecyclerViewAdapter(goldRates);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        getGoldFromNBPApi();

        swipeRefreshLayout = view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGoldFromNBPApi();
                swipeRefreshLayout.setRefreshing(false);
            }
        });




        return view;
    }

    private void getGoldFromNBPApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofitGoldRate = new Retrofit.Builder()
                .baseUrl(BASE_URL_EXCHANGERATE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NBPApi nbpApi = retrofitGoldRate.create(NBPApi.class);

        Call<List<GoldRate>> callGoldRates = nbpApi.getGoldRate(30);
        callGoldRates.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<GoldRate>> call, Response<List<GoldRate>> response) {
        adapter.setGoldRatesList(response.body());
    }

    @Override
    public void onFailure(Call<List<GoldRate>> call, Throwable t) {
        Toast.makeText(getContext(), "Unable to download data", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onFailure: " + t.getMessage());
    }
}
