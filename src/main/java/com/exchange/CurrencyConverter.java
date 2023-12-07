package com.exchange;


import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyConverter {

    private final ComissionService comissionService;

    @Autowired
    public CurrencyConverter( ComissionService comissionService) {
        this.comissionService = comissionService;
    }

    @Autowired
    public CurrencyRate currencyRateService;
    
    public BigDecimal convertCurrency(
            String from,
            String to,
            double amount
    ) {
        double fromRate = currencyRateService.getCurrencyRate(from);
        double toRate = currencyRateService.getCurrencyRate(to);
        if ( BigDecimal.valueOf(fromRate).equals(BigDecimal.valueOf(-1)) || BigDecimal.valueOf(toRate).equals(BigDecimal.valueOf(-1))) {
            return BigDecimal.valueOf(-1);
        }

        
        Comission comission = comissionService.getComission();

        BigDecimal comissionFrom = comission.getComissionFrom();
        BigDecimal comissionTo = comission.getComissionTo();

        if (from.equals("UZS")){

            BigDecimal convertedAmount = BigDecimal.valueOf(amount / toRate);

            BigDecimal comissionApplied = comissionFrom.multiply(BigDecimal.valueOf((amount / toRate)/100));

            comissionApplied = convertedAmount.subtract(comissionApplied);

            return comissionApplied.setScale(6, BigDecimal.ROUND_HALF_UP);
        } else if (to.equals("UZS")) {

            BigDecimal convertedAmount = BigDecimal.valueOf(amount * fromRate);

            BigDecimal comissionApplied = comissionTo.multiply(BigDecimal.valueOf((amount * fromRate)/100));

            comissionApplied = convertedAmount.subtract(comissionApplied);

            return comissionApplied.setScale(6, BigDecimal.ROUND_HALF_UP);
        } else{

            if (fromRate != -1 && toRate != -1) {
        
            BigDecimal convertedAmount = BigDecimal.valueOf(amount * fromRate);

            BigDecimal comissionApplied1 = comissionFrom.multiply(BigDecimal.valueOf((amount * fromRate)/100));

            comissionApplied1 = convertedAmount.subtract(comissionApplied1);

            BigDecimal comissionApplied2 = comissionTo.multiply(comissionApplied1.divide(BigDecimal.valueOf(100)));

            comissionApplied2 = comissionApplied1.subtract(comissionApplied2);

            comissionApplied2 = comissionApplied2.divide(BigDecimal.valueOf(toRate), 6, RoundingMode.HALF_UP);

        
            return comissionApplied2.setScale(6, BigDecimal.ROUND_HALF_UP);
            
            } else {
                return BigDecimal.valueOf(-1);
            }
        }
        
    }
}
