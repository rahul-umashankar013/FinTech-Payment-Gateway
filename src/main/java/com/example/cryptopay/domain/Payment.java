package com.example.cryptopay.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String merchantId;
    private String customerId;
    private String cryptoCurrency;
    private BigDecimal cryptoAmount;
    private BigDecimal fiatAmount;
    private String fiatCurrency;
    private String status;
    private Instant createdAt = Instant.now();
    // getters and setters
}
