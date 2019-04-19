CREATE SCHEMA IF NOT EXISTS recipe;

CREATE TABLE IF NOT EXISTS recipe.users (
  id SERIAL PRIMARY KEY,
  username VARCHAR,
  password VARCHAR,
  email VARCHAR
);

CREATE TABLE IF NOT EXISTS recipe.password_reset_tokens (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES recipe.users(id),
  token VARCHAR,
  expirydate DATE
);