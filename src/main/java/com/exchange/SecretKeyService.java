package com.exchange;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecretKeyService {

    private final SecretKeyRepository secretKeyRepository;

    @Autowired
    public SecretKeyService(SecretKeyRepository secretKeyRepository) {
        this.secretKeyRepository = secretKeyRepository;
    }

    public SecretKey updateKey(String keyValue) {
        SecretKey secretKey;
        if (secretKeyRepository.count() == 0) {
            secretKey = new SecretKey();
        } else {
            secretKey = secretKeyRepository.findAll().get(0);
        }
        secretKey.setKey(keyValue);
        return secretKeyRepository.save(secretKey);
    }

    public String getKey() {
        if (secretKeyRepository.count() > 0) {
            return secretKeyRepository.findAll().get(0).getKey();
        }
        return null;
    }
}
