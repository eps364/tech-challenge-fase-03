INSERT INTO products (id, name, price) VALUES
    (1,  'X-Burguer',         25.90),
    (2,  'X-Salada',          28.90),
    (3,  'X-Bacon',           32.90),
    (4,  'Batata Frita P',    12.90),
    (5,  'Batata Frita G',    18.90),
    (6,  'Refrigerante Lata',  7.90),
    (7,  'Suco Natural',       9.90),
    (8,  'Sorvete',           14.90),
    (9,  'Onion Rings',       15.90),
    (10, 'Milk Shake',        19.90)
ON CONFLICT (id) DO NOTHING;

SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));
