ALTER TABLE oauth_access_token
  ADD CONSTRAINT oauth_access_token_pk
    PRIMARY KEY (authentication_id);