package com.learning.logi.graph.api.domain.order.service;

import com.learning.logi.graph.api.domain.order.dto.OrderInsertDTO;
import com.learning.logi.graph.api.domain.order.dto.OrderResponseDTO;
import com.learning.logi.graph.api.domain.order.entities.Order;
import com.learning.logi.graph.api.domain.order.enums.OrderStatus;
import com.learning.logi.graph.api.domain.order.repository.OrderRepository;
import com.learning.logi.graph.api.presentation.exceptions.ResourceNotFound;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("Deve criar um pedido com sucesso quando os dados são válidos")
    void createOrderWithValidDataShouldReturnOrderWithStatusCreated() {

        Coordinate collectCoordinate = new Coordinate(-60.0, -3.0);
        Coordinate deliverCoordinate = new Coordinate(-60.1, -3.1);

        Point collectPoint = new GeometryFactory().createPoint(collectCoordinate);
        Point deliverPoint = new GeometryFactory().createPoint(deliverCoordinate);

        Order mockOrder = new Order("Test", collectPoint, deliverPoint);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        OrderInsertDTO dto = OrderInsertDTO.from("Test", -60.0, -3.0, -60.1, -3.1);
        OrderResponseDTO result = orderService.insert(dto);

        assertNotNull(result);
        assertEquals("Test", result.description());
        assertEquals(-60.0, result.collectLon());
        assertEquals(-3.0, result.collectLat());
        assertEquals(-60.1, result.deliveryLon());
        assertEquals(-3.1, result.deliveryLat());
    }

        @Test
        @DisplayName("Deve lançar exceção ao tentar criar pedido com descrição vazia")
        void createOrderWithEmptyDescriptionShouldThrowIllegalArgumentException() {

            OrderInsertDTO dto = OrderInsertDTO.from(null, -60.0, -3.0, -60.1, -3.1);
    
            Assertions.assertThatThrownBy(() -> orderService.insert(dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("order description cannot be empty");

            verify(orderRepository, never()).save(any(Order.class));
        }

        @Test
        @DisplayName("Deve buscar um pedido com sucesso quando o ID existe")
        void searchOrderByIdWhenIdExistsShouldReturnOrderResponseDTO() {

            Long orderId = 1L;

            Coordinate collectCoordinate = new Coordinate(-60.0, -3.0);
            Coordinate deliverCoordinate = new Coordinate(-60.1, -3.1);

            Point collectPoint = new GeometryFactory().createPoint(collectCoordinate);
            Point deliverPoint = new GeometryFactory().createPoint(deliverCoordinate);

            Order order = new Order(
                    "Test",
                    collectPoint,
                    deliverPoint
            );
            order.setId(orderId);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            OrderResponseDTO response = orderService.findById(orderId);

            Assertions.assertThat(response).isNotNull();
            Assertions.assertThat(response.id()).isEqualTo(orderId);
            Assertions.assertThat(response.description()).isEqualTo("Test");
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar um pedido com ID que não existe")
        void searchRequestByIdNoIdDoesNotExistShouldThrowResourceNotFoundException() {

            Long nonExistent = 99L;

            when(orderRepository.findById(nonExistent)).thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(() -> orderService.findById(nonExistent))
                    .isInstanceOf(ResourceNotFound.class)
                    .hasMessageContaining("resource not found with id " + nonExistent);
        }

        @Test
        @DisplayName("Deve retornar uma página de pedidos quando existem pedidos com o status solicitado")
        void listOrdersByStatusWhenOrdersExistShouldReturnDTOsPage() {

            Coordinate collectCoordinate = new Coordinate(-60.0, -3.0);
            Coordinate deliverCoordinate = new Coordinate(-60.1, -3.1);

            Point collectPoint = new GeometryFactory().createPoint(collectCoordinate);
            Point deliverPoint = new GeometryFactory().createPoint(deliverCoordinate);

            Pageable pageable = PageRequest.of(0, 10);
            Order order= new Order("Order 1", collectPoint, deliverPoint);
            order.setId(1L);
            List<Order> orderList = Collections.singletonList(order);
            Page<Order> orderPage = new PageImpl<>(orderList, pageable, 1);

            when(orderRepository.findByStatus(OrderStatus.CREATED.name(), pageable)).thenReturn(orderPage);

            Page<OrderResponseDTO> responsePage = orderService.findAllOrdersByStatus(OrderStatus.CREATED, pageable);

            assertThat(responsePage).isNotNull();
            assertThat(responsePage.getTotalElements()).isEqualTo(1);
            assertThat(responsePage.getContent().getFirst().description()).isEqualTo("Order 1");
        }
}

