package com.demobank.service;

import com.demobank.dto.DashboardStats;
import com.demobank.entity.Transaction;
import com.demobank.entity.User;
import com.demobank.enums.TransactionStatus;
import com.demobank.enums.TransactionType;
import com.demobank.repository.TransactionRepository;
import com.demobank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public DashboardStats getDashboardStats(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionRepository.findByAccountNumber(user.getAccountNumber());

        double totalDeposits = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEPOSIT ||
                        (t.getType() == TransactionType.TRANSFER &&
                                t.getToAccount().equals(user.getAccountNumber())))
                .filter(t -> t.getStatus() == TransactionStatus.COMPLETED)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalWithdrawals = transactions.stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAWAL ||
                        (t.getType() == TransactionType.TRANSFER &&
                                t.getFromAccount().equals(user.getAccountNumber())))
                .filter(t -> t.getStatus() == TransactionStatus.COMPLETED)
                .mapToDouble(Transaction::getAmount)
                .sum();

        long pendingTransactions = transactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.PENDING)
                .count();

        return DashboardStats.builder()
                .currentBalance(user.getBalance())
                .totalTransactions(transactions.size())
                .totalDeposits(totalDeposits)
                .totalWithdrawals(totalWithdrawals)
                .pendingTransactions((int) pendingTransactions)
                .accountNumber(user.getAccountNumber())
                .fullName(user.getFullName())
                .build();
    }
}