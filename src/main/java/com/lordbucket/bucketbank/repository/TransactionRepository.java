package com.lordbucket.bucketbank.repository;

import com.lordbucket.bucketbank.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
