package com.learning.logi.graph.api.domain.optimization.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OptaVehicle implements OptaStandstill {

    private Long id;
    private Location startingPoint;
    private Integer capacity;

    private List<OptaCustomer> customers;

    public OptaVehicle() { }

    @Override
    public Location getLocation() {
        return startingPoint;
    }
}
