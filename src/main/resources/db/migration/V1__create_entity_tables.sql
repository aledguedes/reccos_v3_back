CREATE TABLE owners (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    role VARCHAR(50) NOT NULL DEFAULT 'OWNER',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE federations (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    street VARCHAR(255),
    number VARCHAR(255),
    complement VARCHAR(255),
    neighborhood VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip_code VARCHAR(255),
    cpf VARCHAR(20),
    full_name VARCHAR(255),
    phone_number VARCHAR(50),
    email_verified BOOLEAN,
    email_verification_code VARCHAR(255),
    email_verification_code_expiry BIGINT,
    federation_id UUID,
    CONSTRAINT fk_user_federation_ref FOREIGN KEY (federation_id) REFERENCES federations(id)
);

CREATE TABLE user_federations (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    federation_id UUID NOT NULL,
    associated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_federation_fed FOREIGN KEY (federation_id) REFERENCES federations(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_federation UNIQUE (user_id, federation_id)
);

CREATE TABLE verification_codes (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    code VARCHAR(6) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_email_code UNIQUE (email, code)
);

-- INSERT para OWNER inicial
INSERT INTO owners (id, email, password, username, email_verified, role, created_at)
VALUES (
    RANDOM_UUID(),
    'owner@admin.com',
    '$2a$12$tLU8f3BAtmYB40eCD3yiwufAWpZC.TB0OyQ/s/Gex09Hp40z9RPb6',
    'OWNER',
    TRUE,
    'OWNER',
    CURRENT_TIMESTAMP
);