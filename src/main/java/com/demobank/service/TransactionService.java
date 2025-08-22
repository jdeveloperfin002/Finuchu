package com.demobank.service;

import com.demobank.dto.TransactionRequest;
import com.demobank.entity.Transaction;
import com.demobank.entity.User;
import com.demobank.enums.TransactionStatus;
import com.demobank.enums.TransactionType;
import com.demobank.repository.TransactionRepository;
import com.demobank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Transaction> getUserTransactions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionRepository.findByAccountNumber(user.getAccountNumber());
    }

    public Transaction transferFunds(TransactionRequest request, String username) {
        User fromUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User toUser = userRepository.findByAccountNumber(request.getToAccount())
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));

        if (fromUser.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        // Debit from sender
        fromUser.setBalance(fromUser.getBalance() - request.getAmount());
        fromUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(fromUser);

        // Credit to receiver
        toUser.setBalance(toUser.getBalance() + request.getAmount());
        toUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(toUser);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromUser.getAccountNumber());
        transaction.setToAccount(request.getToAccount());
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setDescription(request.getDescription());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReferenceNumber(UUID.randomUUID().toString());
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public Transaction deposit(TransactionRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBalance(user.getBalance() + request.getAmount());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setToAccount(user.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription(request.getDescription());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReferenceNumber(UUID.randomUUID().toString());
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public Transaction withdraw(TransactionRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(user.getBalance() - request.getAmount());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(user.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setDescription(request.getDescription());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReferenceNumber(UUID.randomUUID().toString());
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }
}