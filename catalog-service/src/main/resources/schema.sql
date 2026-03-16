CREATE TABLE IF NOT EXISTS products (
    id       UUID    PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    price    NUMERIC(10, 2) NOT NULL,
    restaurant_id UUID,
    food_type VARCHAR(100)
);

ALTER TABLE products
    ADD COLUMN IF NOT EXISTS restaurant_id UUID;

ALTER TABLE products
    ADD COLUMN IF NOT EXISTS food_type VARCHAR(100);

UPDATE products
SET restaurant_id = 'a1b2c3d4-0001-0001-0001-000000000002'
WHERE restaurant_id IS NULL;

UPDATE products
SET food_type = 'Fast Food'
WHERE food_type IS NULL;

ALTER TABLE products
    ALTER COLUMN restaurant_id SET NOT NULL;

ALTER TABLE products
    ALTER COLUMN food_type SET NOT NULL;
