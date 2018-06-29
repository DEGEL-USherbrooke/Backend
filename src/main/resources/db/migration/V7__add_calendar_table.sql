CREATE TABLE calendar (
    id UUID NOT NULL PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    key TEXT NOT NULL,
    last_fetch timestamp,
    calendar TEXT
)