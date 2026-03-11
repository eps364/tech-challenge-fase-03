CREATE TABLE IF NOT EXISTS clients (
    id UUID PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL,
    address_id UUID,
    street VARCHAR(255),
    number VARCHAR(32),
    neighborhood VARCHAR(255),
    complement VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(64),
    zip_code VARCHAR(32)
);
