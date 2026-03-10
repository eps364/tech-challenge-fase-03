CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    client_id UUID NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    status VARCHAR(32) NOT NULL,
    attempts INT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (order_id)
);
