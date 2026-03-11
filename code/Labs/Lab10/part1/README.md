# Part 1

This folder contains the lecture demo reorganized as the solution for Lab 10 Part 1.

## What is included

- `EmployeeService`: resource server with `/name`, `/phone`, `/salary`, and `/sensitive`
- `PrivateDataService`: downstream protected service with `/private`
- `ApiGateway`: optional gateway in front of the services
- `Client`: calls the services directly
- `ClientThatUsesGateway`: calls the same endpoints through the gateway
- `docker-compose.yml` and `keycloak/realm-export.json`: start Keycloak with the lab realm, client, users, and roles already configured

## Keycloak setup

From this folder:

```bash
docker compose up -d
```

That starts Keycloak on `http://localhost:8090` with:

- admin user: `admin` / `admin`
- realm: `myrealm`
- client id: `myclient`
- client secret: `0maxLeON2d9gwyF1ilv52YBxgB5AoXGL`
- users:
  - `john` / `john` with role `USER`
  - `frank` / `frank` with roles `USER` and `ADMIN`

## Run the services

Start these applications in separate terminals:

```bash
cd EmployeeService && ./mvnw spring-boot:run
cd PrivateDataService && ./mvnw spring-boot:run
```

Optional gateway:

```bash
cd ApiGateway && ./mvnw spring-boot:run
```

## Verify

Direct service client:

```bash
cd Client && ./mvnw spring-boot:run
```

Gateway client:

```bash
cd ClientThatUsesGateway && ./mvnw spring-boot:run
```

Expected behavior:

- `/name` is public
- `/phone` is allowed for `john` and `frank`
- `/salary` is allowed only for `frank`
- `/sensitive` propagates `frank`'s token from `EmployeeService` to `PrivateDataService`
