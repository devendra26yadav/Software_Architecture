# Lab 6 Part 3 - Webshop Microservices

This is a microservice split of the lab webshop into separate projects:

- `product-service`: product catalog component (`addProduct`, `getProduct`, `changePrice`)
- `shoppingcart-service`: shopping cart component (`addToShoppingCart`, `getShoppingCart`)
- `client-app`: REST client flow that calls both services

## Architecture

- `product-service` and `shoppingcart-service` are separate Spring Boot apps.
- Both services register in Consul service registry.
- `shoppingcart-service` calls `product-service` by service name (`product-service`) via Spring Cloud LoadBalancer + Consul discovery.
- Shopping carts keep product snapshots; on cart read, `shoppingcart-service` refreshes each item from `product-service` so price/name/description changes are propagated.

## Tech

- Java 21
- Spring Boot 3.3.6
- Spring Cloud 2023.0.3
- Spring Data MongoDB
- Consul service discovery

## Run

1. Start MongoDB + Consul:

```bash
docker compose up -d
```

2. Start product service:

```bash
mvn -pl product-service spring-boot:run
```

3. Start shopping cart service:

```bash
mvn -pl shoppingcart-service spring-boot:run
```

4. Run the client flow:

```bash
mvn -pl client-app spring-boot:run
```

### Local Discovery Note

For local host-run services with Consul in Docker, defaults are already set so that:
- service-to-service calls use `localhost` (host JVM friendly)
- Consul health checks use `host.docker.internal` (container to host)

If your environment differs, override:
- `CONSUL_SERVICE_HOST`
- `CONSUL_HEALTHCHECK_HOST`

The client prints:
- added product
- fetched product
- added to cart
- fetched cart
- changed price in product service
- fetched cart after price change (updated inside shopping cart service)

## Endpoints

### Product service (port 8901)
- `POST /api/products`
- `GET /api/products/{productNumber}`
- `PATCH /api/products/{productNumber}/price`
- `PUT /api/products/{productNumber}/price`

### Shopping cart service (port 8902)
- `POST /api/shopping-carts/{customerNumber}/items`
- `GET /api/shopping-carts/{customerNumber}`
