package main.java.com.currencyconverter.exceptions;

public class CurrencyConverterException extends RuntimeException {

    public CurrencyConverterException(String message){
        super(message);
    }

    public CurrencyConverterException(){
        super();
    }
}
