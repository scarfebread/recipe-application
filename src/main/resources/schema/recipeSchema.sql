DROP SCHEMA IF EXISTS recipe CASCADE;

CREATE SCHEMA recipe;

CREATE TABLE recipe.users (
  id SERIAL PRIMARY KEY,
  username VARCHAR,
  password VARCHAR,
  email VARCHAR
);

CREATE TABLE recipe.password_reset_tokens (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES recipe.users(id),
  token VARCHAR,
  expiryDate DATE
);

CREATE TABLE recipe.recipes (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES recipe.users(id),
  title VARCHAR NOT NULL,
  shared_by VARCHAR
);

CREATE TABLE recipe.ingredients (
  id SERIAL PRIMARY KEY,
  recipe INT REFERENCES recipe.recipes(id),
  name VARCHAR NOT NULL
);

CREATE TABLE recipe.steps (
  id SERIAL PRIMARY KEY,
  recipe INT REFERENCES recipe.recipes(id),
  name VARCHAR NOT NULL,
  step INT NOT NULL
);

