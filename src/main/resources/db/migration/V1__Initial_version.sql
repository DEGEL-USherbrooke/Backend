CREATE TABLE users (
  id UUID NOT NULL PRIMARY KEY,
  created_at timestamp NOT NULL,
  updated_at timestamp NOT NULL,
  cip TEXT NOT NULL,
  token TEXT
);