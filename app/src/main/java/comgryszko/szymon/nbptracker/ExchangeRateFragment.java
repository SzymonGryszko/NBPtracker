package comgryszko.szymon.nbptracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import comgryszko.szymon.nbptracker.api.CurrencyExchangeRates;
import comgryszko.szymon.nbptracker.api.CurrencyRate;
import comgryszko.szymon.nbptracker.api.NBPApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExchangeRateFragment extends Fragment implements Callback<CurrencyExchangeRates> {

    public static final String BASE_URL_EXCHANGERATE = "https://api.nbp.pl/";
    private TextView mRate, mDate;
    private ProgressBar mProgressBar;
    private static final String TAG = "ExchangeRateFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_rate, container, false);

        mRate = view.findViewById(R.id.rate);
        mDate = view.findViewById(R.id.date);
        mProgressBar = view.findViewById(R.id.progress_bar_exchange);
        mProgressBar.setVisibility(View.INVISIBLE);


        getRateFromNBPApi();


        return view;
    }

    private void getRateFromNBPApi() {
        mProgressBar.setVisibility(View.VISIBLE);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofitExchangerate = new Retrofit.Builder()
                .baseUrl(BASE_URL_EXCHANGERATE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NBPApi nbpApi = retrofitExchangerate.create(NBPApi.class);

        Call<CurrencyExchangeRates> callRates = nbpApi.getRate("usd");
        callRates.enqueue(this);
    }

    @Override
    public void onResponse(Call<CurrencyExchangeRates> call, Response<CurrencyExchangeRates> response) {
        Log.d(TAG, "onResponse: " + response.message() + response.body());
        CurrencyRate currencyRate = response.body().getRates().get(0);
        mRate.setText(String.valueOf(currencyRate.getMid()));
        mDate.setText(currencyRate.getEffectiveDate());
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Call<CurrencyExchangeRates> call, Throwable t) {
        Log.d(TAG, "onFailure: " + t.getMessage());
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
