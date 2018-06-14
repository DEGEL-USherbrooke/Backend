# Degel Backend

## Prepation
- Create `application-local.yml` with the following content:

```yaml
spring:
  profiles: local
  datasource:
    url: jdbc:postgresql://localhost:5432/degel
    username: degel
    password: password

app:
  location: http://localhost:8080
```

- (Only for docker) Create a `.env` file with the following content:

```
POSTGRES_USER=degel
POSTGRES_PASSWORD=password
```

## Running
- For local deployment, run `./deploy-local.sh`.
- For dev deployment, run `docker-compose -f docker-compose.dev.yml up -d`

## Adding authentification
By default, no OAuth client is defined in the `oauth_client_details` table, you need to add one.

- For normal users:

```
INSERT INTO oauth_client_details VALUES
  (<INSERT CLIENT ID HERE>,
   NULL,
   '{noop}',
   'user_read,user_write',
   'authorization_code,refresh_token',
   'http://localhost:8080/oauth/callback',
   NULL,
   18000,
   36000,
   NULL,
   'true');
```

- For trusted notification partners:

```
INSERT INTO oauth_client_details VALUES
  (<INSERT CLIENT ID HERE>,
   NULL,
   <INSERT CLIENT SECRET HERE (WITH CRYPTO METHOD)>,
   'push_notification',
   'client_credentials',
   NULL,
   'ROLE_TRUSTED_CLIENT',
   946080000,
   NULL,
   NULL,
   'true');
```
