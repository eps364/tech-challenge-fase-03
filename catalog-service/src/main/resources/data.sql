INSERT INTO products (id, name, price, restaurant_id, food_type) VALUES
    (1,  'X-Burguer',         25.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'Fast Food'),
    (2,  'X-Salada',          28.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'Fast Food'),
    (3,  'X-Bacon',           32.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'Fast Food'),
    (4,  'Batata Frita P',    12.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'Side'),
    (5,  'Batata Frita G',    18.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'Side'),
    (6,  'Refrigerante Lata',  7.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'Drink'),
    (7,  'Suco Natural',       9.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'Drink'),
    (8,  'Sorvete',           14.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'Dessert'),
    (9,  'Onion Rings',       15.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'Side'),
    (10, 'Milk Shake',        19.90, 'a1b2c3d4-0001-0001-0001-000000000002', 'Drink')
ON CONFLICT (id) DO NOTHING;

SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));
