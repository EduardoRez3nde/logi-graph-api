package com.learning.logi.graph.api.entities;

import com.learning.logi.graph.api.entities.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Entity
@Table(name = "tb_order")
public class Order extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point collectionPoint;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point deliveredPoint;

    @Column(name = "updated_on")
    protected Instant deliveredOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deliveryman_id", nullable = false)
    private DeliveryMan deliveryMan;

    public Order() { }

    @PreUpdate
    protected void onDelivered() {
        this.deliveredOn = Instant.now();
    }
}
