package com.exchange;


import org.springframework.data.jpa.repository.JpaRepository;

public interface RateBaseRepository extends JpaRepository<RateBase, Long> {
    RateBase findByCcy(String ccy);
}
