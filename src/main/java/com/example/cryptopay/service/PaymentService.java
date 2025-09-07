package com.example.cryptopay.service;

import com.example.cryptopay.domain.OutboxEvent;
import com.example.cryptopay.domain.Payment;
import com.example.cryptopay.repo.OutboxRepository;
import com.example.cryptopay.repo.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final OutboxRepository outboxRepo;
    private final ObjectMapper mapper = new ObjectMapper();
    public PaymentService(PaymentRepository paymentRepo, OutboxRepository outboxRepo) {
        this.paymentRepo = paymentRepo;
        this.outboxRepo = outboxRepo;
    }
    @Transactional
    public Payment receivePayment(Payment payment) throws Exception {
        payment.setStatus("RECEIVED");
        Payment saved = paymentRepo.save(payment);
        OutboxEvent ev = new OutboxEvent();
        ev.setAggregateType("Payment");
        ev.setAggregateId(saved.getId().toString());
        ev.setEventType("PAYMENT_RECEIVED");
        ev.setEventId(UUID.randomUUID().toString());
        ev.setPayload(mapper.writeValueAsString(saved));
        outboxRepo.save(ev);
        return saved;
    }
}
