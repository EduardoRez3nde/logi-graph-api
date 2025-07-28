package com.learning.logi.graph.api.entities;

import jakarta.persistence.*;
import java.time.Instant;

@MappedSuperclass
public abstract class AuditableEntity {

    @Column(name = "created_on", nullable = false, updatable = false)
    protected Instant createdOn;

    @PrePersist
    protected void onCreate() {
        this.createdOn = Instant.now();
    }

    public Instant getCreatedOn() {
        return createdOn;
    }
}

