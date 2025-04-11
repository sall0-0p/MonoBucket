package com.lordbucket.bucketbank.repository;

import com.lordbucket.bucketbank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
