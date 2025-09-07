package com.example.cryptopay.repo;

import com.example.cryptopay.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findFirst100ByProcessedFalseOrderByCreatedAtAsc();
    @Modifying
    @Query("update OutboxEvent o set o.processed = true, o.processedAt = current_timestamp where o.id = :id and o.processed = false")
    int markProcessed(@Param("id") Long id);
}
