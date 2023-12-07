package com.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class Controller {

    private final CurrencyService currencyService;
    private final CurrencyConverter currencyConverter;
    private final CurrencyAmountChanger currencyAmountChanger;
    private final SecretKeyService secretKeyService;
    private final ComissionService comissionService;
    private final CurrencyRate currencyRate;

    @Autowired
    public Controller(
        CurrencyService currencyService, 
        CurrencyConverter currencyConverter, 
        CurrencyAmountChanger currencyAmountChanger, 
        SecretKeyService secretKeyService,
        ComissionService comissionService,
        CurrencyRate currencyRate
    ) {
        this.currencyService = currencyService;
        this.currencyConverter = currencyConverter;
        this.currencyAmountChanger = currencyAmountChanger;
        this.secretKeyService = secretKeyService;
        this.comissionService = comissionService;
        this.currencyRate = currencyRate;
    }

   

    @GetMapping("/currency")
    public List<CurrencyCheck> getAllCurrencyChecks() {
        List<CurrencyCheck> checks = currencyService.findAllAmount();
        return checks;
    }

    @PostMapping("/currency")
    public String createCurrencyCheck(@RequestBody CurrencyCheck check) {
        String output = currencyService.setAmount(check);
        return output;
    }


    @GetMapping("/officialrates")
    public String getOfficial(@RequestParam String date, @RequestParam String pair) {
        return currencyRate.getOfficialRates(date, pair);
    }

    @GetMapping("/convert")
    public ResponseEntity<String> convertCurrencyGet(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount
    ) {
        if (currencyConverter.convertCurrency(from, to, amount).equals(BigDecimal.valueOf(-1))){
            return new ResponseEntity<>("Currency conversion failed. Please check the provided currency codes.", HttpStatus.NOT_FOUND);
        } else if (from.equals("UZS")){
            return new ResponseEntity<>(amount + " " + "UZS" + " equals " + currencyConverter.convertCurrency(from, to, amount) + " " + to, HttpStatus.OK);
        } else if (to.equals("UZS")) {
            return new ResponseEntity<>(amount + " " + from + " equals " + currencyConverter.convertCurrency(from, to, amount) + " " + "UZS", HttpStatus.OK);
        } else{
            return new ResponseEntity<>(amount + " " + from + " equals " + currencyConverter.convertCurrency(from, to, amount) + " " + to, HttpStatus.OK);
        }
            
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convertCurrencyPost(
        @RequestBody ConvertorRequest conversionRequest
    ) {
        String from = conversionRequest.getFrom();
        String to = conversionRequest.getTo();
        double amount = conversionRequest.getAmount();

        if (currencyConverter.convertCurrency(from, to, amount).equals(BigDecimal.valueOf(-1))){
            return new ResponseEntity<>("Currency conversion failed. Please check the provided currency codes.", HttpStatus.NOT_FOUND);
        } else if (from.equals("UZS")) {            
            return new ResponseEntity<>(currencyAmountChanger.ConvertAmount(from, to, amount), HttpStatus.OK);
        } else if (to.equals("UZS")) {
            return new ResponseEntity<>(currencyAmountChanger.ConvertAmount(from, to, amount), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(currencyAmountChanger.ConvertAmount(from, to, amount), HttpStatus.OK);
        }
    }

    @PostMapping("/setcomission")
    public ResponseEntity<String> createOrUpdateComission(@RequestBody Comission request) {
        BigDecimal from = request.getComissionFrom();
        BigDecimal to = request.getComissionTo();
        String secretKey = request.getKey();

        String key = secretKeyService.getKey();

        if (!key.equals(secretKey)) {
            return new ResponseEntity<>("Wrong secret key! ", HttpStatus.FORBIDDEN);
        }

        Comission comission = comissionService.createOrUpdateComission(from, to);
        return new ResponseEntity<>("Comission created/updated successfully with value: from: " + comission.getComissionFrom() + " | to: " + comission.getComissionTo(), HttpStatus.OK);
    }

    @GetMapping("/getcomission")
    public ResponseEntity<String> getComission() {
        Comission comission = comissionService.getComission();
        return new ResponseEntity<>("Comission From: " + comission.getComissionFrom() + "% | Comission To: " + comission.getComissionTo() + "%", HttpStatus.OK);
    }

    @PostMapping("/key")
    public ResponseEntity<String> updateKey(@RequestBody String keyValue) {
        SecretKey key = secretKeyService.updateKey(keyValue);
        return new ResponseEntity<>("Key updated successfully with value: " + key.getKey(), HttpStatus.OK);
    }




}