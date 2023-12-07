package com.exchange;

import java.math.BigDecimal;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RateBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ccy;
    private BigDecimal rate;

    public RateBase(){
        
    }
    public RateBase(String ccy, BigDecimal rate){
        this.ccy = ccy;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public String getCcy() {
        return ccy;
    }

    public BigDecimal getRate(){
        return rate;
    }



}
