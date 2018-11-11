CREATE TABLE feeds (
  id UUID NOT NULL PRIMARY KEY,
  name TEXT NOT NULL,
  url TEXT NOT NULL,
  created_at timestamp NOT NULL,
  updated_at timestamp NOT NULL
);

CREATE TABLE feeds_subscriptions (
  user_id UUID NOT NULL REFERENCES users(id),
  feed_id UUID NOT NULL REFERENCES feeds(id),
  PRIMARY KEY(user_id, feed_id)
);