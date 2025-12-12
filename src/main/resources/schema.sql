CREATE TABLE IF NOT EXISTS users (
  id serial PRIMARY KEY,
  username varchar(100) UNIQUE NOT NULL,
  password varchar(255) NOT NULL,
  role varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
  id serial PRIMARY KEY,
  token varchar(512) UNIQUE NOT NULL,
  expiry_date timestamp NOT NULL,
  user_id integer REFERENCES users(id)
);
