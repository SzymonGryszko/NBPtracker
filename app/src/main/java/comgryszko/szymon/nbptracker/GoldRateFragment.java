package comgryszko.szymon.nbptracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private TextView unableToDownload;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gold_rate, container, false);

        unableToDownload = view.findViewById(R.id.unable_to_download);
        unableToDownload.setVisibility(View.INVISIBLE);

        List<GoldRate> goldRates = new ArrayList<>();
        adapter = new RecyclerViewAdapter(goldRates);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

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

        return view;
    }

    @Override
    public void onResponse(Call<List<GoldRate>> call, Response<List<GoldRate>> response) {
        adapter.setGoldRatesList(response.body());
    }

    @Override
    public void onFailure(Call<List<GoldRate>> call, Throwable t) {
        unableToDownload.setVisibility(View.VISIBLE);
        Log.d(TAG, "onFailure: " + t.getMessage());
    }
}
