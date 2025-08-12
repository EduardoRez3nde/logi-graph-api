CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE tb_vehicle (
    id BIGSERIAL PRIMARY KEY,
    vehicle_license_plate VARCHAR(255) NOT NULL,
    vehicle_type VARCHAR(255),
    max_capacity_kg DOUBLE PRECISION NOT NULL
);

CREATE TABLE tb_delivery_man (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    delivery_man_status VARCHAR(255) NOT NULL,
    current_location GEOGRAPHY(POINT, 4326),
    vehicle_id BIGINT UNIQUE,
    created_on TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    
    CONSTRAINT fk_vehicle FOREIGN KEY (vehicle_id) REFERENCES tb_vehicle(id)
);

CREATE TABLE tb_order (
    id BIGSERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    order_status VARCHAR(255),
    collection_point GEOGRAPHY(POINT, 4326),
    delivered_point GEOGRAPHY(POINT, 4326),
    delivered_on TIMESTAMP(6) WITH TIME ZONE,
    delivery_man_id BIGINT,
    created_on TIMESTAMP(6) WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_delivery_man FOREIGN KEY (delivery_man_id) REFERENCES tb_delivery_man(id)
);
