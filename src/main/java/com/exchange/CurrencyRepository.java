package com.exchange;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<CurrencyCheck, Long> {
    CurrencyCheck findByName(String name);
}