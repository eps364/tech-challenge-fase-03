DROP TABLE IF EXISTS restaurant_owners;
DROP TABLE IF EXISTS restaurants;

CREATE TABLE IF NOT EXISTS restaurants (
    id          UUID         PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    active       BOOLEAN      NOT NULL DEFAULT true,
    street      VARCHAR(255),
    number      VARCHAR(50),
    district    VARCHAR(255),
    complement  VARCHAR(255),
    city        VARCHAR(255),
    state       VARCHAR(100),
    zip_code    VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS restaurant_owners (
    restaurant_id UUID NOT NULL REFERENCES restaurants(id),
    owner_id      UUID NOT NULL,
    PRIMARY KEY (restaurant_id, owner_id)
);
