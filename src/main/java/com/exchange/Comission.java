package com.exchange;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Comission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal comissionFrom;
    private BigDecimal comissionTo;
    private String key;

    public Comission(){
    }

    public Comission(BigDecimal comissionFrom, BigDecimal comissionTo){
        this.comissionFrom = comissionFrom;
        this.comissionTo = comissionTo;
    }


    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public BigDecimal getComissionFrom() {
        return comissionFrom;
    }

    public void setComissionFrom(BigDecimal comissionFrom) {
        this.comissionFrom = comissionFrom;
    }

    public BigDecimal getComissionTo() {
        return comissionTo;
    }

    public void setComissionTo(BigDecimal comissionTo) {
        this.comissionTo = comissionTo;
    }
}