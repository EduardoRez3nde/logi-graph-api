package com.learning.logi.graph.api.domain.order.entities;

import com.learning.logi.graph.api.domain.AuditableEntity;
import com.learning.logi.graph.api.domain.delivery_man.entities.DeliveryMan;
import com.learning.logi.graph.api.domain.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.Instant;

@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tb_order")
public class Order extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point collectionPoint;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point deliveredPoint;

    @Column(name = "delivered_on")
    protected Instant deliveredOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deliveryman_id")
    private DeliveryMan deliveryMan;

    public Order() { }

    public Order(final String description, final Point collectPoint, final Point deliveredPoint) {
        this.description = description;
        this.collectionPoint = collectPoint;
        this.deliveredPoint = deliveredPoint;
        this.orderStatus = OrderStatus.CREATED;
    }

    public void assignDeliveryMan(final DeliveryMan deliveryMan) {

        if (this.orderStatus != OrderStatus.CREATED) {
            throw new  IllegalArgumentException("Order cannot be marked as delivered if not in transit.");
        }
        this.orderStatus = OrderStatus.ALLOCATED;
        this.deliveryMan = deliveryMan;
    }

    public void markAsDelivered() {

        if (this.orderStatus != OrderStatus.IN_TRANSIT) {
            throw new IllegalArgumentException("Order cannot be marked as delivered if not in transit.");
        }
        this.orderStatus = OrderStatus.DELIVERED;
        this.deliveredOn = Instant.now();
    }
}
