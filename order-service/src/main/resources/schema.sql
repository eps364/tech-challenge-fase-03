CREATE TABLE IF NOT EXISTS orders
(
    id            UUID           PRIMARY KEY,
    client_id     UUID           NOT NULL,
    restaurant_id UUID           NOT NULL,
    status        VARCHAR(50)    NOT NULL,
    total         NUMERIC(15, 2) NOT NULL,
    created_at    TIMESTAMPTZ    NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items
(
    order_id   UUID           NOT NULL REFERENCES orders (id),
    product_id UUID           NOT NULL,
    name       VARCHAR(255)   NOT NULL,
    quantity   INT            NOT NULL,
    price      NUMERIC(15, 2) NOT NULL,
    subtotal   NUMERIC(15, 2) NOT NULL
);