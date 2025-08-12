UPDATE tb_order SET delivery_man_id = 1 WHERE id IN (1, 2);

UPDATE tb_delivery_man SET delivery_man_status = 'IN_DELIVERY' WHERE id = 1;