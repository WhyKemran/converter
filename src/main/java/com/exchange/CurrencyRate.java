package com.exchange;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class CurrencyRate {
    
    private final RateBaseRepository rateBaseRepository;

    @Autowired
    public CurrencyRate(
        RateBaseRepository rateBaseRepository
        ) {
        this.rateBaseRepository = rateBaseRepository;
    }
    

    public void todayRates(){
        try {
            URL url = new URL("https://cbu.uz/ru/arkhiv-kursov-valyut/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String json = response.toString();

            Gson gson = new Gson();
            CurrencyRateFinder[] currencyRates = gson.fromJson(json, CurrencyRateFinder[].class);

            for (CurrencyRateFinder rate : currencyRates) {
                RateBase rates = new RateBase(rate.getCcy(), BigDecimal.valueOf(rate.getRate()));
                rateBaseRepository.save(rates);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getCurrencyRate(String codeToFind){
        try {
            return rateBaseRepository.findByCcy(codeToFind).getRate().doubleValue();
        } catch(Exception e){
            return -1;
        }
        
    }

    // код ниже просто каждый раз берет новые данные с банка
    
    // public double getCurrencyRate(String codeToFind) {
    //     try {
    //         URL url = new URL("https://cbu.uz/ru/arkhiv-kursov-valyut/json/");
    //         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //         conn.setRequestMethod("GET");

    //         BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    //         StringBuilder response = new StringBuilder();
    //         String line;

    //         while ((line = reader.readLine()) != null) {
    //             response.append(line);
    //         }
    //         reader.close();

    //         String json = response.toString();

    //         Gson gson = new Gson();
    //         CurrencyRateFinder[] currencyRates = gson.fromJson(json, CurrencyRateFinder[].class);

    //         for (CurrencyRateFinder rate : currencyRates) {
    //             if (rate.getCcy().equals(codeToFind)) {
    //                 conn.disconnect();
    //                 return rate.getRate();
    //             }
    //         }

    //         conn.disconnect();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return -1;
    // }


    public String getOfficialRates(String date, String pair) {
        try {
            URL url = new URL("https://cbu.uz/ru/arkhiv-kursov-valyut/json/all/" + date + "/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String json = response.toString();

            Gson gson = new Gson();
            CurrencyRateFinder[] currencyRates = gson.fromJson(json, CurrencyRateFinder[].class);

            String[] currencies = pair.split("/");
            if (currencies[0].equals("UZS")){
                for (CurrencyRateFinder rate : currencyRates) {
                    if (rate.getCcy().equals(currencies[1])) {
                    conn.disconnect();
                    return "The official Central Bank rate for the " + pair + " pair on " + date + ": " + rate.getRate();
                    }
                }
            } else if (currencies[1].equals("UZS")){
                for (CurrencyRateFinder rate : currencyRates) {
                    if (rate.getCcy().equals(currencies[0])) {
                    conn.disconnect();
                    return "The official Central Bank rate for the " + pair + " pair on " + date + ": " + rate.getRate();
                    }
                }
            } else{
                double pair1 = 0;
                double pair2 = 0;
                for (CurrencyRateFinder rate : currencyRates) {
                    if (rate.getCcy().equals(currencies[0])) {
                    pair1 = rate.getRate();
                    }
                    if (rate.getCcy().equals(currencies[1])) {
                    pair2 = rate.getRate();
                    }
                }
                if (pair1!=0 && pair2!=0){
                    return "The official Central Bank rate for the " + pair + " pair on " + date + ": " + "1 " + currencies[0] + ": " + BigDecimal.valueOf(pair1/pair2).setScale(6, RoundingMode.HALF_UP ) + " " + currencies[1];
                }  else {
                    return "something went wrong, try again";
                }
                
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error when receiving official rate for " + pair + " date: " + date;
    }

}

class CurrencyRateFinder {
    private String Ccy;
    private double Rate;

    public double getRate() {
        return Rate;
    }

    public String getCcy() {
        return Ccy;
    }

}