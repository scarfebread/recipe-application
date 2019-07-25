DROP SCHEMA IF EXISTS recipe CASCADE;

CREATE SCHEMA recipe;

CREATE TABLE recipe.users (
  id SERIAL PRIMARY KEY,
  username VARCHAR,
  password VARCHAR,
  email VARCHAR,
  new_user BOOL
);

CREATE TABLE recipe.password_reset_tokens (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES recipe.users(id),
  token VARCHAR,
  expiry_date DATE
);

CREATE TABLE recipe.recipes (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES recipe.users(id),
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

CREATE TABLE recipe.ingredients (
  id SERIAL PRIMARY KEY,
  description VARCHAR NOT NULL,
  metric VARCHAR NOT NULL,
  imperial VARCHAR NOT NULL,
  user_id INT REFERENCES recipe.users(id)
);

CREATE TABLE recipe.recipe_ingredients (
  id SERIAL PRIMARY KEY,
  recipe INT REFERENCES recipe.recipes(id),
  ingredient INT REFERENCES recipe.ingredients(id)
);

CREATE TABLE recipe.steps (
  id SERIAL PRIMARY KEY,
  recipe INT REFERENCES recipe.recipes(id),
  description VARCHAR NOT NULL,
  step INT NOT NULL
);

CREATE TABLE recipe.recently_viewed (
  id SERIAL PRIMARY KEY,
  recipe INT REFERENCES recipe.recipes(id),
  user_id INT REFERENCES recipe.users(id)
);

CREATE TABLE recipe.inventory (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES recipe.users(id),
  ingredient_id INT REFERENCES recipe.ingredients(id)
);

CREATE TABLE recipe.shopping_list (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES recipe.users(id),
  ingredient_id INT REFERENCES recipe.ingredients(id),
  inventory_id INT REFERENCES recipe.inventory(id)
);
