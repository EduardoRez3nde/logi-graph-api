package com.learning.logi.graph.api.domain.vehicle.service;

import com.learning.logi.graph.api.domain.delivery_man.repository.DeliveryManRepository;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleInsertDTO;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleResponseDTO;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleUpdateDTO;
import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import com.learning.logi.graph.api.domain.vehicle.enums.VehicleType;
import com.learning.logi.graph.api.domain.vehicle.repository.VehicleRepository;
import com.learning.logi.graph.api.presentation.exceptions.LicensePlateAlreadyRegisteredException;
import com.learning.logi.graph.api.presentation.exceptions.VehicleNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTests {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DeliveryManRepository deliveryManRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    @DisplayName("Deve criar um veiculo com sucesso quando placa não existe")
    void shouldCreateVehicleSuccessfullyWhenPlateDoesNotExist() {

        final String vehiclePlate = "ABC-1233";
        final Long vehicleId = 1L;

        final VehicleInsertDTO vehicleInsert = new VehicleInsertDTO(vehiclePlate, VehicleType.CAR, 20.0);

        when(vehicleRepository.findByVehicleLicensePlate(vehiclePlate))
                .thenReturn(Optional.empty());

        final Vehicle vehicle = Vehicle.from(vehiclePlate, VehicleType.CAR, 20.0);
        vehicle.setId(vehicleId);

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenReturn(vehicle);

        final VehicleResponseDTO vehicleResponse = vehicleService.insert(vehicleInsert);

        assertThat(vehicleResponse).isNotNull();
        assertThat(vehicleResponse.id()).isEqualTo(vehicleId);
        assertThat(vehicleResponse.vehicleLicensePlate()).isEqualTo(vehiclePlate);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test                                                                                                                                                                                                              
    @DisplayName("Deve lançar uma exceção quando a placa já estiver cadastrada")
    void createVehicleWhenLicensePlateAlreadyExistsShouldThrowAnException() {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           

        final String vehiclePlate = "ABC-1233";

        final VehicleInsertDTO vehicleInsert = new VehicleInsertDTO(vehiclePlate, VehicleType.CAR, 20.0);

        when(vehicleRepository.findByVehicleLicensePlate(vehiclePlate))
                .thenReturn(Optional.of(new Vehicle()));

        assertThatThrownBy(() -> vehicleService.insert(vehicleInsert))
                .isInstanceOf(LicensePlateAlreadyRegisteredException.class)
                .hasMessageContaining("Plate already registered");

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar um veículo com sucesso quando o id fornecido existir")
    void searchVehicleByIdWhenIdExistsShouldReturnDTO() {

        final long vehicleId = 1L;
        final String vehiclePlate = "ABC-1233";
        final Vehicle vehicle = Vehicle.from(vehiclePlate, VehicleType.CAR, 20.0);
        vehicle.setId(vehicleId);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        final VehicleResponseDTO response = vehicleService.findById(vehicleId);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(vehicleId);
        assertThat(response.vehicleLicensePlate()).isEqualTo(vehiclePlate);

        verify(vehicleRepository, times(1)).findById(vehicleId);
    }

    @Test
    @DisplayName("Deve retornar um veiculo quando seu id existir")
    void shouldReturnVehicleWhenIdExists() {

        final long vehicleId = 1L;
        final String vehiclePlate = "ABC-1233";
        final VehicleType type = VehicleType.CAR;

        final Vehicle vehicle = Vehicle.from(vehiclePlate, type, 20.0);
        vehicle.setId(vehicleId);

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        final VehicleResponseDTO vehicleResponse = vehicleService.findById(vehicleId);

        Assertions.assertNotNull(vehicleResponse);
        Assertions.assertEquals(vehicleId, vehicleResponse.id());
        Assertions.assertEquals(vehiclePlate, vehicleResponse.vehicleLicensePlate());
        Assertions.assertEquals(type, vehicleResponse.vehicleType());
    }

    @Test
    @DisplayName("Deveria lançar uma exceção buscar um veiculo por id quando ele não existir")
    void shouldThrowExceptionWhenSearchingForIdWhenIdDoesNotExist() {

        final long id = 1L;

        when(vehicleRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.findById(id))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessageContaining("Vehicle with ID " + id + " not found");
    }

    @Test
    @DisplayName("Deveria retornar uma página com veículos quando existirem registros")
    void ShouldReturnPageWithVehiclesWhenRecordsExist() {

        final Vehicle vehicle1 = Vehicle.from("ABC-1234", VehicleType.CAR, 120.0);
        vehicle1.setId(1L);
        final Vehicle vehicle2 = Vehicle.from("XYZ-5678", VehicleType.MOTORCYCLE, 20.0);
        vehicle2.setId(2L);

        final List<Vehicle> vehicles = List.of(vehicle1, vehicle2);
        final PageRequest pageable = PageRequest.of(0, 10);

        final Page<Vehicle> vehiclesPage = new PageImpl<>(vehicles, pageable, vehicles.size());

        when(vehicleRepository.findAll(any(Pageable.class)))
                .thenReturn(vehiclesPage);

        final Page<VehicleResponseDTO> response = vehicleService.findAll(pageable);

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(2, response.getContent().size());
        Assertions.assertEquals("ABC-1234", response.getContent().get(0).vehicleLicensePlate());
        Assertions.assertEquals("XYZ-5678", response.getContent().get(1).vehicleLicensePlate());

        verify(vehicleRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deveria retornar página vazia quando não houver veículos")
    void ShouldReturnEmptyPageWhenThereAreNoVehicles() {

        final List<Vehicle> vehicles = List.of();
        final PageRequest pageable = PageRequest.of(0, 10);

        when(vehicleRepository.findAll(any(Pageable.class)))
                .thenReturn(Page.empty());

        final Page<VehicleResponseDTO> response = vehicleService.findAll(pageable);

        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("Deveria atualizar um veículo existente")
    void ShouldUpdateAnExistingVehicle() {

        final long id = 1L;
        final Vehicle vehicle = Vehicle.from("XYZ-5678", VehicleType.MOTORCYCLE, 20.0);
        vehicle.setId(id);

        final VehicleUpdateDTO updateDTO = VehicleUpdateDTO.from("ABC-1234", VehicleType.MOTORCYCLE, 20.0);
        final Vehicle updateVehicle = Vehicle.from(updateDTO.vehicleLicensePlate(), updateDTO.vehicleType(), updateDTO.maxCapacityKg());
        updateVehicle.setId(id);

        when(vehicleRepository.findById(id))
                .thenReturn(Optional.of(vehicle));

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenReturn(updateVehicle);

        final VehicleResponseDTO response = vehicleService.update(id, updateDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(updateDTO.vehicleLicensePlate(), response.vehicleLicensePlate());
        Assertions.assertEquals(updateDTO.vehicleType(), response.vehicleType());
        Assertions.assertEquals(updateDTO.maxCapacityKg(), response.maxCapacityKg());
        Assertions.assertEquals(id, response.id());

        verify(vehicleRepository).findById(id);
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Deveria lançar EntityNotFoundException quando o ID não existir")
    void ShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {

        final long idNoExists = 2L;
        final Vehicle vehicle = Vehicle.from("ABC-1234", VehicleType.CAR, 120.0);
        vehicle.setId(1L);

        final VehicleUpdateDTO updateDTO = VehicleUpdateDTO.from("ABC-1234", VehicleType.MOTORCYCLE, 20.0);

        when(vehicleRepository.findById(idNoExists))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            vehicleService.update(idNoExists, updateDTO);
        }, "Vehicle not found with id: " + idNoExists);
    }

    @Test
    @DisplayName("Deveria inserir um novo veículo com sucesso")
    void shouldSuccessfullyInsertNewVehicle() {

        final long id = 1L;
        final VehicleInsertDTO insertVehicle = VehicleInsertDTO.from("ABC-1234", VehicleType.CAR, 120.0);
        final Vehicle vehicle = Vehicle.from(insertVehicle.vehicleLicensePlate(), insertVehicle.vehicleType(), insertVehicle.maxCapacityKg());
        vehicle.setId(id);

        when(vehicleRepository.findByVehicleLicensePlate(insertVehicle.vehicleLicensePlate()))
                .thenReturn(Optional.empty());

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenReturn(vehicle);

        final VehicleResponseDTO response = vehicleService.insert(insertVehicle);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(insertVehicle.vehicleLicensePlate(), response.vehicleLicensePlate());
        Assertions.assertEquals(insertVehicle.vehicleType(), response.vehicleType());
        Assertions.assertEquals(insertVehicle.maxCapacityKg(), response.maxCapacityKg());
        Assertions.assertEquals(id, response.id());

        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Deveria lançar LicensePlateAlreadyRegisteredException quando a placa já existir")
    void shouldThrowLicensePlateAlreadyRegisteredExceptionWhenThePlateAlreadyExists() {

        final long id = 1L;
        final VehicleInsertDTO insertVehicle = VehicleInsertDTO.from("ABC-1234", VehicleType.MOTORCYCLE, 20.0);
        final Vehicle vehicle = Vehicle.from("ABC-1234", VehicleType.CAR, 120.0);
        vehicle.setId(id);

        when(vehicleRepository.findByVehicleLicensePlate(vehicle.getVehicleLicensePlate()))
                .thenReturn(Optional.of(vehicle));

        assertThatThrownBy(() -> vehicleService.insert(insertVehicle))
                .isInstanceOf(LicensePlateAlreadyRegisteredException.class)
                .hasMessageContaining("Plate already registered");
    }

    @Test
    @DisplayName("Deveria remover veículo existente e não associado a entregador")
    void shouldRemoveExistingVehicleNotAssociatedWithDeliveryPerson() {


    }

}

















