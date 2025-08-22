package com.demobank.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String toAccount;
    private Double amount;
    private String description;
    private String type; // DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT
}