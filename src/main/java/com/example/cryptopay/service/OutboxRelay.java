package com.example.cryptopay.service;

import com.example.cryptopay.domain.OutboxEvent;
import com.example.cryptopay.kafka.OutboxProducer;
import com.example.cryptopay.repo.OutboxRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.util.List;

@Component
public class OutboxRelay {
    private final OutboxRepository outboxRepo;
    private final OutboxProducer producer;
    private final RedisTemplate<String,Object> redis;
    private static final String LOCK_KEY = "outbox:relay:lock";
    public OutboxRelay(OutboxRepository outboxRepo, OutboxProducer producer, RedisTemplate<String,Object> redis) {
        this.outboxRepo = outboxRepo;
        this.producer = producer;
        this.redis = redis;
    }
    @Scheduled(fixedDelay = 5000)
    public void relay() {
        Boolean acquired = redis.opsForValue().setIfAbsent(LOCK_KEY, "1", Duration.ofSeconds(10));
        if (acquired == null || !acquired) return;
        try {
            List<OutboxEvent> batch = outboxRepo.findFirst100ByProcessedFalseOrderByCreatedAtAsc();
            for (OutboxEvent ev : batch) {
                String idKey = "outbox:event:" + ev.getEventId();
                Boolean seen = redis.opsForValue().setIfAbsent(idKey, "1", Duration.ofMinutes(10));
                if (seen != null && !seen) continue;
                try {
                    producer.publish("payments.events", ev.getEventId(), ev.getPayload());
                    markProcessed(ev.getId());
                    redis.opsForValue().set(idKey, "1", Duration.ofMinutes(10));
                } catch (Exception ex) { }
            }
        } finally {
            redis.delete(LOCK_KEY);
        }
    }
    @Transactional
    public void markProcessed(Long id) { outboxRepo.markProcessed(id); }
}
