CREATE TYPE EXCHANGE_REQUEST_STATUS AS ENUM ('PENDING', 'ACCEPTED', 'REJECTED', 'CANCELLED');
CREATE TYPE CREDIT_CARD_TYPE AS ENUM ('VISA', 'MASTERCARD');
CREATE TYPE FIELD_POSITION AS ENUM ('GOALKEEPER',
                                    'MIDFIELDER',
                                    'DEFENDER',
                                    'FORWARD');
CREATE TYPE EXCHANGE_OFFER_STATUS AS ENUM ('PENDING', 'ACCEPTED', 'REJECTED', 'COUNTEROFFER', 'CANCELLED');
CREATE CAST (VARCHAR AS FIELD_POSITION) WITH INOUT AS IMPLICIT;
CREATE CAST (VARCHAR AS EXCHANGE_REQUEST_STATUS) WITH INOUT AS IMPLICIT;
CREATE CAST (VARCHAR AS EXCHANGE_OFFER_STATUS) WITH INOUT AS IMPLICIT;
CREATE CAST (VARCHAR AS CREDIT_CARD_TYPE) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    birthday DATE NOT NULL,
    username VARCHAR(20) NOT NULL UNIQUE,
    auth0_sub VARCHAR(100) UNIQUE --Identificador de usuario de Auth0
);

CREATE TABLE IF NOT EXISTS card(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    player_position FIELD_POSITION NOT NULL,
    player_number SMALLINT NOT NULL,
    country VARCHAR(100) NOT NULL,
    photo_url VARCHAR(255)
);

--Consultar con Jonathan, aparentemente almacenar tarjetas de crédito no es buena práctica
CREATE TABLE IF NOT EXISTS credit_card(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    expiration_date DATE NOT NULL,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    cardholder_name VARCHAR(20) NOT NULL,
    bank VARCHAR(20) NOT NULL,
    card_type CREDIT_CARD_TYPE NOT NULL
);

CREATE TABLE IF NOT EXISTS purchase(
    id BIGSERIAL PRIMARY KEY,
    purchase_timestamp TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id),
    packets_purchased INT NOT NULL CHECK(packets_purchased > 0),
    base_amount NUMERIC GENERATED ALWAYS AS (packets_purchased*5) STORED, --Precio hipotetico de $5 por paquete
    credit_card_id BIGINT NOT NULL REFERENCES credit_card(id)
);

CREATE TABLE IF NOT EXISTS exchange_request(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    requested_card_id BIGINT NOT NULL REFERENCES card(id),
    status EXCHANGE_REQUEST_STATUS NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS exchange_offer(
    id BIGSERIAL PRIMARY KEY,
    bidder_id BIGINT NOT NULL REFERENCES users(id),
    exchange_request_id BIGINT NOT NULL REFERENCES exchange_request(id),
    offered_card_id BIGINT NOT NULL REFERENCES card(id),
    status EXCHANGE_OFFER_STATUS NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS exchange_counteroffer (
    id BIGSERIAL PRIMARY KEY,
    offered_card_id BIGINT NOT NULL REFERENCES card(id),
    status EXCHANGE_REQUEST_STATUS NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    exchange_request_id BIGINT NOT NULL REFERENCES exchange_request(id),
    exchange_offer_id BIGINT NOT NULL REFERENCES exchange_offer(id),
    UNIQUE(exchange_request_id, exchange_offer_id)
    );

CREATE TABLE IF NOT EXISTS ownership(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    card_id BIGINT NOT NULL REFERENCES card(id),
    number_of_cards_owned INT NOT NULL,
    UNIQUE(user_id, card_id)
);

CREATE INDEX IF NOT EXISTS idx_user_id_username ON users(id, username);
CREATE INDEX IF NOT EXISTS idx_ownership_user_id_card_id ON ownership(user_id, card_id);
CREATE INDEX IF NOT EXISTS idx_purchase_user_id_credit_card_id ON purchase(user_id,credit_card_id);
CREATE INDEX IF NOT EXISTS idx_payment_information_user_id ON credit_card(user_id);
CREATE INDEX IF NOT EXISTS idx_card_id_country ON card(id,country);