package main.java.com.currencyconverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import main.java.com.currencyconverter.enums.Currency;
import main.java.com.currencyconverter.exceptions.CurrencyConverterException;
import main.java.com.currencyconverter.util.PropertiesLoader;
import main.java.com.currencyconverter.vo.CurrencyRequest;
import main.java.com.currencyconverter.vo.CurrencyResponse;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HttpsURLConnection;

public class CurrencyConverter {
    private static final String API_KEY = "API_KEY";
    private static final String URL_ENDPOINT = "URL_ENDPOINT";

    public static void main(String[] args) throws IOException {
        CurrencyRequest currencyRequest = new CurrencyRequest();
        userInputMethod(currencyRequest);

        String apiKey = PropertiesLoader.getFromProperties(API_KEY);
        String urlEndpoint = PropertiesLoader.getFromProperties(URL_ENDPOINT);

        // Call api.currencyfreaks.com
        String currecyReponseJsonString =  getBufferReaderByUrl(stringBuildUrlEndpoint(apiKey,urlEndpoint,currencyRequest.getToCurrencyCode(),currencyRequest.getFromCurrencyCode()));
        System.out.println("\nJsonResponse : " + currecyReponseJsonString);

        Gson gson = new Gson();
        CurrencyResponse obj = gson.fromJson(currecyReponseJsonString, CurrencyResponse.class);
        Double rate = obj.getRates().get(currencyRequest.getToCurrencyCode());

        System.out.printf("\nExchange rate from %s to %s is %.2f", currencyRequest.getFromCurrencyCode(), currencyRequest.getToCurrencyCode(), rate);
        System.out.printf("\nYou will receive %.2f %s", Double.parseDouble(currencyRequest.getAmount()) * rate, currencyRequest.getToCurrencyCode());

    }

    private static void userInputMethod(CurrencyRequest currencyRequest){
        Scanner scanner = new Scanner(System.in);
        String confirmation = "N";
        while(!confirmation.equalsIgnoreCase("Y")) {
            // From/Base currency is only working for Paid Subscription, thus it's set to "USD" by default in CurrencyRequest.java
            while(StringUtils.isEmpty(currencyRequest.getFromCurrencyCode())) {
                System.out.print("\nCurrency you would like to Exchange From : ");
                currencyRequest.setFromCurrencyCode(scanner.nextLine().toUpperCase());
            }

            while(StringUtils.isEmpty(currencyRequest.getToCurrencyCode())) {
                System.out.print("Currency you would like to Exchange To : ");
                currencyRequest.setToCurrencyCode(scanner.nextLine().toUpperCase());
            }

            while(StringUtils.isEmpty(currencyRequest.getAmount())) {
                System.out.print("Please enter amount : ");
                while (!scanner.hasNextInt()) {
                    System.out.print("Error, please enter a valid amount : ");
                    scanner.nextLine();
                }
                currencyRequest.setAmount(scanner.nextLine());
            }

            System.out.printf("\nCurrency From: %s \nCurrent To : %s \nAmount : %s ", currencyRequest.getFromCurrencyCode(), currencyRequest.getToCurrencyCode(), currencyRequest.getAmount());

            System.out.print("\nAre the above details correct? (Y/N) : ");
            confirmation = scanner.nextLine();
            if(confirmation.equalsIgnoreCase("N") || !currencyCodeChecker(currencyRequest.getFromCurrencyCode(), currencyRequest.getToCurrencyCode())){
                currencyRequest.setFromCurrencyCode("USD");// From/Base will always be "USD" for Free subscription
                currencyRequest.setToCurrencyCode("");
                currencyRequest.setAmount("");
                confirmation="N";
            }
        }
    }

    private static Boolean currencyCodeChecker(String fromCurrencyCode, String toCurrencyCode){
        if(!Currency.findByValue(fromCurrencyCode)){
            System.out.printf("Currency Code %s does not exists \n", fromCurrencyCode);
            return false;
        }

        if(!Currency.findByValue(toCurrencyCode)){
            System.out.printf("Currency Code %s does not exists \n", toCurrencyCode);
            return false;
        }
        return true;
    }


    private static String stringBuildUrlEndpoint(String apiKey, String urlEndpoint, String toCurrencyCode, String fromCurrencyCode){
        StringBuilder str = new StringBuilder();
        str.append(urlEndpoint);
        str.append(apiKey);
        str.append("&symbols=");
        str.append(toCurrencyCode);
        if(!fromCurrencyCode.equalsIgnoreCase("USD")) {
            str.append("&base=");
            str.append(fromCurrencyCode);
        }
        System.out.printf("\nURL : %s",str);
        return str.toString();
    }

    private static String getBufferReaderByUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Cache-Control", "no-cache");

        if (conn.getResponseCode() != 200) {
            Scanner s = new Scanner(conn.getErrorStream()).useDelimiter("\\A");
            String errorResponse = s.hasNext() ? s.next() : "";
            throw new CurrencyConverterException("Service Unavailable : " + errorResponse);
        }

        String responseString = new BufferedReader(
                new InputStreamReader(conn.getInputStream())).lines().collect(Collectors.joining());
        conn.disconnect();
        return responseString;
    }
}
