package comgryszko.szymon.nbptracker.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CurrencyExchangeRates {
    private String table;
    private String currency;
    private String code;
    private List<CurrencyRate> rates;
}


