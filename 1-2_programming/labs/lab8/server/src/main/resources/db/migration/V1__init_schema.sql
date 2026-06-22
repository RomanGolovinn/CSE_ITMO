CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS flats (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    x INT NOT NULL,
    y DOUBLE PRECISION NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT NOW(),
    area BIGINT NOT NULL CHECK (area > 0),
    number_of_rooms BIGINT NOT NULL CHECK (number_of_rooms > 0),
    furnish VARCHAR(50),
    view VARCHAR(50),
    transport VARCHAR(50),
    house_name VARCHAR(255),
    house_year BIGINT CHECK (house_year > 0),
    house_floors BIGINT CHECK (house_floors > 0),
    owner_id INT NOT NULL,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);