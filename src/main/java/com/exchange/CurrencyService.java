package com.exchange;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class CurrencyService {
    
    private final CurrencyRepository repository;

    @Autowired
    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    public List<CurrencyCheck> findAllAmount() {
        List<CurrencyCheck> checks = repository.findAll();
        return checks;
    }

    
    public String setAmount(CurrencyCheck check) {
        try {
            CurrencyCheck existingCheck = repository.findByName(check.getName());
            if (existingCheck == null) {
                CurrencyCheck newcheck = new CurrencyCheck(check.getName(), check.getAmount());
                repository.save(newcheck);
                return "Currency added successfully";

            }
            BigDecimal newAmount = check.getAmount();
            existingCheck.setAmount(newAmount);
            repository.save(existingCheck);

            return "Amount updated successfully";
        } catch (Exception e) {
            return "Failed: " + e.getMessage();
        }
    }
    
}

