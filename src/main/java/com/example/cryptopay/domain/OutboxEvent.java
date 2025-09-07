package com.example.cryptopay.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "outbox_events", indexes = @Index(columnList = "processed"))
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String aggregateType;
    private String aggregateId;
    @Lob
    private String payload;
    private String eventType;
    private String eventId;
    private boolean processed = false;
    private Instant createdAt = Instant.now();
    private Instant processedAt;
    // getters/setters
}
