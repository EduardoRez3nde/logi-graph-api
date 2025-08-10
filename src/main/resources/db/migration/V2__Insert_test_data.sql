-- Insere um veículo de teste com capacidade para 2 "unidades de demanda"
INSERT INTO tb_vehicle (id, vehicle_license_plate, vehicle_type, max_capacity_kg)
VALUES (1, 'XYZ-1234', 'MOTORCYCLE', 2.0) ON CONFLICT (id) DO NOTHING;

-- Insere um entregador de teste ONLINE, associado ao veículo 1
INSERT INTO tb_delivery_man (id, name, email, delivery_man_status, created_on, vehicle_id, current_location)
VALUES (
    1,
    'João Silva',
    'joao.silva@email.com',
    'AVAILABLE',
    NOW(), -- Define a data de criação como o momento atual
    1,
    ST_SetSRID(ST_MakePoint(-60.015, -3.115), 4326)
) ON CONFLICT (id) DO NOTHING;

-- Insere dois pedidos de teste para serem otimizados
INSERT INTO tb_order (id, description, order_status, created_on, collection_point, delivered_point)
VALUES (
    1,
    'Pedido de teste 1',
    'CREATED',
    NOW(),
    ST_SetSRID(ST_MakePoint(-60.013, -3.096), 4326),
    ST_SetSRID(ST_MakePoint(-60.016, -3.112), 4326)
) ON CONFLICT (id) DO NOTHING;

INSERT INTO tb_order (id, description, order_status, created_on, collection_point, delivered_point)
VALUES (
    2,
    'Pedido de teste 2',
    'CREATED',
    NOW(),
    ST_SetSRID(ST_MakePoint(-60.009, -3.097), 4326),
    ST_SetSRID(ST_MakePoint(-60.011, -3.114), 4326)
) ON CONFLICT (id) DO NOTHING;
