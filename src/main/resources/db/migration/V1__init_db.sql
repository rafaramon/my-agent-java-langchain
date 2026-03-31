-- V1__init_db.sql
-- Initial Flyway migration script
-- Create users table and initial admin user

CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(25) NOT NULL UNIQUE,
    api_key VARCHAR(255) NOT NULL UNIQUE
);

-- Insert initial admin user
-- The api_key is the SHA-256 hash of 'admin-key-12345'
INSERT INTO users (id, name, description, email, login, api_key)
VALUES (
    '550e8400-e29b-41d4-a716-446655440000', 
    'Administrator', 
    'Default administrative user', 
    'admin@myagent.com', 
    'admin', 
    'c6977161cf2a67ba98dd70602bdd01443a506fa34e7341c5b1b637b304205275'
);
