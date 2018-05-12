# Degel Backend

## Prepation
- Create `application-local.yml` with the following content:
```
spring:
  profiles: local
  datasource:
    url: jdbc:postgresql://localhost:5432/degel
    username: degel
    password: password

app:
  envName: local
```
- (Only for docker) Create a `.env` file with the following content:
```
POSTGRES_USER=degel
POSTGRES_PASSWORD=password
```