INSERT INTO products (id, name, price, restaurant_id, food_type) VALUES
    (1,  'X-Burguer',         25.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'MAIN'),
    (2,  'X-Salada',          28.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'MAIN'),
    (3,  'X-Bacon',           32.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'MAIN'),
    (4,  'Batata Frita P',    12.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'SIDE'),
    (5,  'Batata Frita G',    18.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'SIDE'),
    (6,  'Refrigerante Lata',  7.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'DRINK'),
    (7,  'Suco Natural',       9.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'DRINK'),
    (8,  'Sorvete',           14.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'DESSERT'),
    (9,  'Onion Rings',       15.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'SIDE'),
    (10, 'Milk Shake',        19.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'DRINK')
ON CONFLICT (id) DO NOTHING;

SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));
