# Lab 7 - Part 3 (Minimum)

## 1) Start Consul cluster
```bash
cd part3
docker compose up -d
```

If you already started Consul in Part 1 or Part 2 and containers are still running, skip this step.

## 2) Start backend services from Part 2
Open terminals and run:
```bash
cd part2/StockService1
mvn spring-boot:run
```

```bash
cd part2/StockService2
mvn spring-boot:run
```

```bash
cd part2/ProductService
mvn spring-boot:run
```

## 3) Start API Gateway
Open another terminal:
```bash
cd part3/ApiGateway
mvn spring-boot:run
```

## 4) Call services through gateway
Browser or curl:
```bash
curl http://localhost:8080/api/products/1
curl http://localhost:8080/api/stock/1
```

Both calls go through API gateway (`8080`), not directly to `8081/8082/8083`.

## 5) Verify in Consul UI
- http://localhost:8500
- http://localhost:8501
- http://localhost:8502

You should see:
- `api-gateway`
- `product-service`
- `stock-service`
