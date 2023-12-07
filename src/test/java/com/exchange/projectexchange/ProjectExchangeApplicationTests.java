package com.exchange.projectexchange;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.SpringBootTest;

import com.exchange.*;



@SpringBootTest
class ProjectExchangeApplicationTests {


    private CurrencyConverter currencyConverter;

    @Mock
    private CurrencyRate currencyRateService;

    @Mock
    private ComissionService commissionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyConverter = new CurrencyConverter(commissionService);
        currencyConverter.currencyRateService = currencyRateService;
    }


    // Курс валют на момент 11.11.2023
    // USD = 12288.04
    // RUB = 133.67

    @Test
    public void testConvertCurrencyFromUZS() {
        String fromCurrency = "UZS";
        String toCurrency = "USD";
        double amount = 100.0;
        double toRate = 12288.04; // курс доллара
        Comission commission = new Comission(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0));

        when(currencyRateService.getCurrencyRate(anyString())).thenReturn(toRate);
        when(commissionService.getComission()).thenReturn(commission);

        BigDecimal convertedAmount = currencyConverter.convertCurrency(fromCurrency, toCurrency, amount);

        assertEquals(BigDecimal.valueOf(0.008056638).setScale(6, BigDecimal.ROUND_HALF_UP), convertedAmount);
    }

    @Test
    public void testConvertCurrencyToUZS() {
        String fromCurrency = "USD";
        String toCurrency = "UZS";
        double amount = 55.0;
        double fromRate = 12288.04; // курс доллара
        Comission commission = new Comission(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0));

        when(currencyRateService.getCurrencyRate(anyString())).thenReturn(fromRate);
        when(commissionService.getComission()).thenReturn(commission);

        BigDecimal convertedAmount = currencyConverter.convertCurrency(fromCurrency, toCurrency, amount);

        assertEquals(BigDecimal.valueOf(662325.356).setScale(6, BigDecimal.ROUND_HALF_UP), convertedAmount);
    }

    @Test
    public void testConvertCurrencyBetweenNonUZS() {
        String fromCurrency = "USD";
        String toCurrency = "RUB";
        double amount = 30.0;
        double fromRate = 12288.04; // Курс доллара | значение после перевода с учетом комиссии в 1% = 364954.788
        double toRate = 133.67; // Курс рубля 
        Comission commission = new Comission(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0));

        when(currencyRateService.getCurrencyRate("USD")).thenReturn(fromRate);
        when(currencyRateService.getCurrencyRate("RUB")).thenReturn(toRate);
        when(commissionService.getComission()).thenReturn(commission);

        BigDecimal convertedAmount = currencyConverter.convertCurrency(fromCurrency, toCurrency, amount);

        assertEquals(BigDecimal.valueOf(2675.661646143).setScale(6, BigDecimal.ROUND_HALF_UP), convertedAmount);
    }

    @Test
    public void testInvalidCurrencyConversion() {
        String fromCurrency = "XYZ";
        String toCurrency = "ABC";
        double amount = 4586.0;
        Comission commission = new Comission(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0));

        when(currencyRateService.getCurrencyRate(anyString())).thenReturn(-1.0);
        when(commissionService.getComission()).thenReturn(commission);
        

        BigDecimal convertedAmount = currencyConverter.convertCurrency(fromCurrency, toCurrency, amount);

        assertEquals(BigDecimal.valueOf(-1), convertedAmount);
    }
}

