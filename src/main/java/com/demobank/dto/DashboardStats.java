package com.demobank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStats {
    private Double currentBalance;
    private Integer totalTransactions;
    private Double totalDeposits;
    private Double totalWithdrawals;
    private Integer pendingTransactions;
    private String accountNumber;
    private String fullName;
}