package com.exchange;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyAmountChanger {

    private final CurrencyRepository currencyRepository;
    private final CurrencyConverter currencyConverter;
    private final CurrencyRate currencyRate;

    @Autowired
    public CurrencyAmountChanger(
        CurrencyRepository currencyRepository, 
        CurrencyConverter currencyConverter, 
        CurrencyRate currencyRate, 
        CurrencyService currencyService
        ) {
        this.currencyRepository = currencyRepository;
        this.currencyConverter = currencyConverter;
        this.currencyRate = currencyRate;
    }

    public String ConvertAmount(String from, String to, double amount) {
        try {

            CurrencyCheck existingCurrency1 = currencyRepository.findByName(from);
            CurrencyCheck existingCurrency2 = currencyRepository.findByName(to);
            if (existingCurrency1 == null){
                if(BigDecimal.valueOf(currencyRate.getCurrencyRate(from)).equals(BigDecimal.valueOf(-1))){
                    return "Currency not found";
                } else{
                    existingCurrency1 = new CurrencyCheck(to,BigDecimal.valueOf(0));
                    currencyRepository.save(existingCurrency1);
                }
            } 
            if (existingCurrency2 == null){
                if(BigDecimal.valueOf(currencyRate.getCurrencyRate(to)).equals(BigDecimal.valueOf(-1))){
                    return "Currency not found";
                } else{
                    existingCurrency2 = new CurrencyCheck(to,BigDecimal.valueOf(0));
                    currencyRepository.save(existingCurrency2);

                }
            } 
            

            if (existingCurrency1.getAmount().compareTo(BigDecimal.valueOf(amount)) < 0){
                return "insufficient funds";
            }
            existingCurrency1.setAmount(existingCurrency1.getAmount().subtract(BigDecimal.valueOf(amount)));
            existingCurrency2.setAmount(existingCurrency2.getAmount().add(currencyConverter.convertCurrency(from, to, amount)));

            currencyRepository.save(existingCurrency1);
            currencyRepository.save(existingCurrency2);

            return "Converted successfully";
        } catch (Exception e) {
            return "Failed to convert: " + e.getMessage();
        }
    }

    
}