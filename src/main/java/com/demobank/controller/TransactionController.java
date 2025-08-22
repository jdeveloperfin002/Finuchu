package com.demobank.controller;

import com.demobank.dto.TransactionRequest;
import com.demobank.entity.Transaction;
import com.demobank.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction", description = "Transaction operations")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getUserTransactions(Principal principal) {
        List<Transaction> transactions = transactionService.getUserTransactions(principal.getName());
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferFunds(@RequestBody TransactionRequest request, Principal principal) {
        try {
            Transaction transaction = transactionService.transferFunds(request, principal.getName());
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request, Principal principal) {
        try {
            Transaction transaction = transactionService.deposit(request, principal.getName());
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest request, Principal principal) {
        try {
            Transaction transaction = transactionService.withdraw(request, principal.getName());
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}