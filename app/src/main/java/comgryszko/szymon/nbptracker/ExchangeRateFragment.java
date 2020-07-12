package comgryszko.szymon.nbptracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private static final String BASE_URL_EXCHANGERATE = "https://api.nbp.pl/api/exchangerates/rates/a/";
    private TextView mRate, mDate, mForeignCurrency;
    private ProgressBar mProgressBar;
    private static final String TAG = "ExchangeRateFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_rate, container, false);

        mProgressBar = view.findViewById(R.id.progress_bar_exchange);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRate = view.findViewById(R.id.rate);
        mDate = view.findViewById(R.id.date);
        mForeignCurrency = view.findViewById(R.id.foreignCurrency);
        mForeignCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCurrencyDialog();
            }
        });
        getRateFromNBPApi(String.valueOf(mForeignCurrency.getText()));


        return view;
    }

    private void initCurrencyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select currency");
        String[] currency = {"USD", "EUR", "GBP", "CHF", "RUB"};
        builder.setItems(currency, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mForeignCurrency.setText(currency[0]);
                        getRateFromNBPApi(currency[0]);
                        break;
                    case 1:
                        mForeignCurrency.setText(currency[1]);
                        getRateFromNBPApi(currency[1]);
                        break;
                    case 2:
                        mForeignCurrency.setText(currency[2]);
                        getRateFromNBPApi(currency[2]);
                        break;
                    case 3:
                        mForeignCurrency.setText(currency[3]);
                        getRateFromNBPApi(currency[3]);
                        break;
                    case 4:
                        mForeignCurrency.setText(currency[4]);
                        getRateFromNBPApi(currency[4]);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getRateFromNBPApi(String currency) {
        mProgressBar.setVisibility(View.VISIBLE);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofitExchangerate = new Retrofit.Builder()
                .baseUrl(BASE_URL_EXCHANGERATE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NBPApi nbpApi = retrofitExchangerate.create(NBPApi.class);

        enqueueCall(nbpApi, currency.toLowerCase());
    }

    private void enqueueCall(NBPApi nbpApi, String currency) {
        Call<CurrencyExchangeRates> callRates = nbpApi.getRate(currency.toLowerCase());
        callRates.enqueue(this);
    }

    @Override
    public void onResponse(Call<CurrencyExchangeRates> call, Response<CurrencyExchangeRates> response) {
        Log.d(TAG, "onResponse: " + response.message() + response.body());
        int ratesListSize = response.body().getRates().size();
        CurrencyRate currencyRate = response.body().getRates().get(ratesListSize - 1);
        mRate.setText(String.valueOf(currencyRate.getMid()));
        mDate.setText(currencyRate.getEffectiveDate());
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Call<CurrencyExchangeRates> call, Throwable t) {
        Log.d(TAG, "onFailure: " + t.getMessage());
        mProgressBar.setVisibility(View.INVISIBLE);
        mRate.setTextSize(12);
        mRate.setText(R.string.unableToDownload);
        mDate.setText(R.string.unableToDownload);
    }
}
