CREATE TABLE settings (
    id UUID NOT NULL PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    mobile_notifications boolean NOT NULL DEFAULT TRUE
)