package com.lordbucket.bucketbank.repository;

import com.lordbucket.bucketbank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByCardNumber(String cardNumber);
}
