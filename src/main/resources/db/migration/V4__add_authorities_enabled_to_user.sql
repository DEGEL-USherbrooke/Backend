ALTER TABLE users
    ADD COLUMN enabled boolean NOT NULL;
ALTER TABLE users
    DROP COLUMN token;

CREATE TABLE authorities (
    user_id UUID NOT NULL REFERENCES users(id),
    authority VARCHAR(256) NOT NULL);
CREATE UNIQUE INDEX ix_auth_username ON authorities (user_id,authority);