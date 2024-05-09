package main.java.com.currencyconverter.vo;


public class CurrencyRequest {
    // From/Base currency is only working for Paid Subscription, thus setting as "USD", else remove Initialization
    private String fromCurrencyCode = "USD";
    private String toCurrencyCode;
    private String amount;

    public CurrencyRequest(){

    }

    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }

    public String getToCurrencyCode() {
        return toCurrencyCode;
    }

    public void setToCurrencyCode(String toCurrencyCode) {
        this.toCurrencyCode = toCurrencyCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
