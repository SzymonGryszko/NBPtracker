package comgryszko.szymon.nbptracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
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
    private LineChart lineChart;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "ExchangeRateFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exchange_rate, container, false);

        mProgressBar = view.findViewById(R.id.progress_bar_exchange);
        mProgressBar.setVisibility(View.INVISIBLE);
        lineChart = view.findViewById(R.id.lineChart);
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

        swipeRefreshLayout = view.findViewById(R.id.refreshExchange);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRateFromNBPApi(String.valueOf(mForeignCurrency.getText()));
                swipeRefreshLayout.setRefreshing(false);
            }
        });


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
        Call<CurrencyExchangeRates> callExchangeRates = nbpApi.getCurrencyRate(currency.toLowerCase());
        callExchangeRates.enqueue(this);
    }

    @Override
    public void onResponse(Call<CurrencyExchangeRates> call, Response<CurrencyExchangeRates> response) {
        Log.d(TAG, "onResponse: " + response.message() + response.body());
        int ratesListSize = response.body().getRates().size();
        List<CurrencyRate> ratesList = response.body().getRates();
        CurrencyRate currencyRate = response.body().getRates().get(ratesListSize - 1);
        mRate.setText(String.valueOf(currencyRate.getMid()));
        mDate.setText(currencyRate.getEffectiveDate());
        mProgressBar.setVisibility(View.INVISIBLE);
        initGraph(ratesListSize, ratesList);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    private void initGraph(int ratesListSize, List<CurrencyRate> ratesList) {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < ratesListSize; i++) {
            values.add(new Entry((float) i, (float) ratesList.get(i).getMid()
            ));
        }

        LineDataSet set1;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "Rates");
            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 0f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(1f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // text size of values
            set1.setValueTextSize(8f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return lineChart.getAxisLeft().getAxisMinimum();
                }
            });

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            //format X axis to represent date
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    String fullDate = ratesList.get((int) value).getEffectiveDate();
                    return fullDate.substring(5);
                }
            });

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            lineChart.setData(data);
            lineChart.getData().setHighlightEnabled(false);
        }
    }

    @Override
    public void onFailure(Call<CurrencyExchangeRates> call, Throwable t) {
        Log.d(TAG, "onFailure: " + t.getMessage());
        mProgressBar.setVisibility(View.INVISIBLE);
        mRate.setText(null);
        mDate.setText(R.string.unableToDownload);
    }
}
