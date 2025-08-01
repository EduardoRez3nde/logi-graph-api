package com.learning.logi.graph.api.domain.vehicle.service;

import com.learning.logi.graph.api.domain.delivery_man.repository.DeliveryManRepository;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleInsertDTO;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleResponseDTO;
import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import com.learning.logi.graph.api.domain.vehicle.enums.VehicleType;
import com.learning.logi.graph.api.domain.vehicle.repository.VehicleRepository;
import com.learning.logi.graph.api.presentation.exceptions.LicensePlateAlreadyRegisteredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DeliveryManRepository deliveryManRepository;

    @InjectMocks
    private VehicleService vehicleSevice;

    @Test
    @DisplayName("Deve criar um veículo com sucesso quando os dados forem válidos")
    void createVehicleWithValidDataMustSaveANDReturnDTO() {

        String vehiclePlate = "ABC-1233";
        Long vehicleId = 1L;

        VehicleInsertDTO vehicleInsert = new VehicleInsertDTO(vehiclePlate, VehicleType.CAR, 20.0);

        when(vehicleRepository.findByPlateVehicle(vehiclePlate)).thenReturn(Optional.empty());

        Vehicle vehicle = Vehicle.from(vehiclePlate, VehicleType.CAR, 20.0);
        vehicle.setId(vehicleId);

        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        VehicleResponseDTO vehicleResponse = vehicleSevice.insert(vehicleInsert);

        assertThat(vehicleResponse).isNotNull();
        assertThat(vehicleResponse.id()).isEqualTo(vehicleId);
        assertThat(vehicleResponse.vehicleLicensePlate()).isEqualTo(vehiclePlate);

        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando a placa já estiver cadastrada")
    void createVehicleWhenLicensePlateAlreadyExistsShouldThrowAnException() {

        String vehiclePlate = "ABC-1233";

        VehicleInsertDTO vehicleInsert = new VehicleInsertDTO(vehiclePlate, VehicleType.CAR, 20.0);

        when(vehicleRepository.findByPlateVehicle(vehiclePlate))
                .thenReturn(Optional.of(new Vehicle()));

        assertThatThrownBy(() -> vehicleSevice.insert(vehicleInsert))
                .isInstanceOf(LicensePlateAlreadyRegisteredException.class)
                .hasMessageContaining("Plate already registered");

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar um veículo com sucesso quando o id fornecido existir")
    void searchVehicleByIdWhenIdExistsShouldReturnDTO() {

        long vehicleId = 1L;
        String vehiclePlate = "ABC-1233";
        Vehicle vehicle = Vehicle.from(vehiclePlate, VehicleType.CAR, 20.0);
        vehicle.setId(vehicleId);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        VehicleResponseDTO response = vehicleSevice.findById(vehicleId);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(vehicleId);
        assertThat(response.vehicleLicensePlate()).isEqualTo(vehiclePlate);

        verify(vehicleRepository, times(1)).findById(vehicleId);
    }
}
