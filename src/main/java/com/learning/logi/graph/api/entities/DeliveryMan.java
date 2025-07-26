package com.learning.logi.graph.api.entities;

import com.learning.logi.graph.api.entities.enums.DeliveryManStatus;
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
@AllArgsConstructor
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
}
