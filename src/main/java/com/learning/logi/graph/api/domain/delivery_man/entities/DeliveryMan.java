package com.learning.logi.graph.api.domain.delivery_man.entities;

import com.learning.logi.graph.api.domain.AuditableEntity;
import com.learning.logi.graph.api.domain.delivery_man.enums.DeliveryManStatus;
import com.learning.logi.graph.api.domain.order.entities.Order;
import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tb_delivery_man")
public class DeliveryMan extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private DeliveryManStatus deliveryManStatus;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point currentLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToMany(mappedBy = "deliveryMan", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Order> orders = new ArrayList<>();

    public DeliveryMan() { }

    private DeliveryMan(
            final  String name,
            final String email,
            final DeliveryManStatus deliveryManStatus,
            final Vehicle vehicle
    ) {
        this.name = name;
        this.email = email;
        this.deliveryManStatus = deliveryManStatus;
        this.vehicle = vehicle;
    }

    public static DeliveryMan from(
            final  String name,
            final String email,
            final DeliveryManStatus deliveryManStatus,
            final Vehicle vehicle
    ) {
        return new DeliveryMan(name, email, deliveryManStatus, vehicle);
    }
}
