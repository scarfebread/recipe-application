CREATE SCHEMA the_cooking_pot;

CREATE TABLE the_cooking_pot.users (
  id SERIAL PRIMARY KEY,
  username VARCHAR,
  password VARCHAR,
  email VARCHAR,
  new_user BOOL
);

CREATE TABLE the_cooking_pot.password_reset_tokens (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES the_cooking_pot.users(id),
  token VARCHAR,
  expiry_date DATE
);

CREATE TABLE the_cooking_pot.recipes (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES the_cooking_pot.users(id),
  title VARCHAR NOT NULL,
  shared_by VARCHAR,
  rating INT,
  prep_time VARCHAR,
  cook_time VARCHAR,
  total_time VARCHAR,
  notes VARCHAR,
  serves INT,
  difficulty VARCHAR
);

CREATE TABLE the_cooking_pot.ingredients (
  id SERIAL PRIMARY KEY,
  description VARCHAR NOT NULL,
  metric VARCHAR NOT NULL,
  imperial VARCHAR NOT NULL,
  user_id INT REFERENCES the_cooking_pot.users(id)
);

CREATE TABLE the_cooking_pot.recipe_ingredients (
  id SERIAL PRIMARY KEY,
  recipe INT REFERENCES the_cooking_pot.recipes(id),
  ingredient INT REFERENCES the_cooking_pot.ingredients(id)
);

CREATE TABLE the_cooking_pot.steps (
  id SERIAL PRIMARY KEY,
  recipe INT REFERENCES the_cooking_pot.recipes(id),
  description VARCHAR NOT NULL
);

CREATE TABLE the_cooking_pot.recently_viewed (
  id SERIAL PRIMARY KEY,
  recipe INT REFERENCES the_cooking_pot.recipes(id),
  user_id INT REFERENCES the_cooking_pot.users(id)
);

CREATE TABLE the_cooking_pot.inventory (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES the_cooking_pot.users(id),
  ingredient_id INT REFERENCES the_cooking_pot.ingredients(id)
);

CREATE TABLE the_cooking_pot.shopping_list (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES the_cooking_pot.users(id),
  ingredient_id INT REFERENCES the_cooking_pot.ingredients(id)
);
