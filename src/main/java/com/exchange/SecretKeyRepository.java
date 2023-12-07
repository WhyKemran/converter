package com.exchange;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecretKeyRepository extends JpaRepository<SecretKey, Long> {
    SecretKey findByKey(String key);
}
