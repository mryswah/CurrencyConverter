package main.java.com.currencyconverter.vo;

import java.util.Date;
import java.util.Map;

public class CurrencyResponse {

    String base;
    Map<String, Double> rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
