CREATE SCHEMA IF NOT EXISTS recipe;

CREATE TABLE IF NOT EXISTS recipe.users (
  id SERIAL,
  username VARCHAR,
  password VARCHAR,
  email VARCHAR
);

INSERT INTO recipe.users
(username, password, email)
VALUES
('jscarfe', '$2a$11$VpdlD/w553M1F2LC4l0XNu35jQrqlddetjjscF9Lm7gi9re1LOplq', 'james.scarfe@live.co.uk');