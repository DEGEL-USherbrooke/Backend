CREATE TABLE notification_token (
    id UUID NOT NULL PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    expo_token TEXT NOT NULL UNIQUE
)