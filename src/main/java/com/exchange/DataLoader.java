package com.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final ComissionRepository comissionRepository;
    private final SecretKeyRepository secretKeyRepository;
    private final CurrencyRepository currencyRepository;
    private final CurrencyRate currencyRate;

    @Autowired
    public DataLoader(
        ComissionRepository comissionRepository, 
        SecretKeyRepository secretKeyRepository, 
        CurrencyRepository currencyRepository,
        CurrencyRate currencyRate
        ) {
        this.comissionRepository = comissionRepository;
        this.secretKeyRepository = secretKeyRepository;
        this.currencyRepository = currencyRepository;
        this.currencyRate = currencyRate;
    }

    @Override
    public void run(String... args) throws Exception {
        Comission comission = comissionRepository.findById(1L).orElse(new Comission());
        comission.setComissionFrom(BigDecimal.valueOf(0));
        comission.setComissionTo(BigDecimal.valueOf(0));
        comissionRepository.save(comission);

        SecretKey key = secretKeyRepository.findById(1L).orElse(new SecretKey());
        key.setKey("verysecretkey");
        secretKeyRepository.save(key);

        CurrencyCheck check1 = new CurrencyCheck();
        check1.setName("USD");
        check1.setAmount(BigDecimal.valueOf(300));
        currencyRepository.save(check1);

        CurrencyCheck check2 = new CurrencyCheck();
        check2.setName("RUB");
        check2.setAmount(BigDecimal.valueOf(5000));
        currencyRepository.save(check2);

        currencyRate.todayRates();
    
        

    }
}