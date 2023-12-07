package com.exchange;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComissionService {

    private final ComissionRepository comissionRepository;

    @Autowired
    public ComissionService(ComissionRepository comissionRepository) {
        this.comissionRepository = comissionRepository;
    }

    public Comission createOrUpdateComission(BigDecimal from, BigDecimal to) {
        Comission comission = comissionRepository.findById(1L).orElse(new Comission());
        comission.setComissionFrom(from);
        comission.setComissionTo(to);
        return comissionRepository.save(comission);
    }

    public Comission getComission() {
        Comission comission = comissionRepository.findById(1L).orElse(null);
        if (comission != null) {
            return comission;
        }
        return null;
    }
 
}