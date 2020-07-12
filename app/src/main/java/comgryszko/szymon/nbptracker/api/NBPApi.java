package comgryszko.szymon.nbptracker.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NBPApi {

    @GET("{currency}/last/30/?format=json")
    Call<CurrencyExchangeRates>getRate(@Path("currency") String currency);

}
