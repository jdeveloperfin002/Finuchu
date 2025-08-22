package com.demobank.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceResponse {
    private Double balance;
    private String accountNumber;
    private String fullName;
}