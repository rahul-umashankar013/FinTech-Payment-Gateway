package com.example.cryptopay.controller;

import com.example.cryptopay.domain.Payment;
import com.example.cryptopay.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) { this.paymentService = paymentService; }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Payment req) throws Exception {
        Payment p = paymentService.receivePayment(req);
        return ResponseEntity.ok(p);
    }
}
