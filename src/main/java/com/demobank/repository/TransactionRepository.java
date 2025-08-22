package com.demobank.repository;

import com.demobank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = ?1 OR t.toAccount = ?1 ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountNumber(String accountNumber);

    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = ?1 OR t.toAccount = ?1) AND t.transactionDate BETWEEN ?2 AND ?3 ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountNumberAndDateRange(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);
}