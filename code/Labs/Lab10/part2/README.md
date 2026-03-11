# Part 2

This folder contains the Lab 10 Part 2 implementation using in-memory users and HTTP Basic authentication.

## Users

The same in-memory users are configured in services `A`, `B`, and `C`:

- `customer` / `customer` with role `CUSTOMER`
- `employee` / `employee` with roles `CUSTOMER`, `EMPLOYEE`
- `manager` / `manager` with roles `CUSTOMER`, `EMPLOYEE`, `MANAGER`

## Services

- `ServiceA` on `http://localhost:8181`
  - `/productdata`: accessible by customer, employee, and manager
  - `/employee-contact-data`: accessible by employee and manager, then calls `ServiceB`
  - `/salary-data`: accessible by manager, then calls `ServiceC`
- `ServiceB` on `http://localhost:8182`
  - `/employee-contact-data`: accessible by employee and manager
- `ServiceC` on `http://localhost:8183`
  - `/salary-data`: accessible by manager

`ServiceA` forwards the caller's Basic auth header to the downstream service, so the downstream authorization is enforced with the same user identity.

## Run

Start these applications in separate terminals:

```bash
cd ServiceB && ./mvnw spring-boot:run
cd ServiceC && ./mvnw spring-boot:run
cd ServiceA && ./mvnw spring-boot:run
```

Then run the verification client:

```bash
cd Client && ./mvnw spring-boot:run
```

## Expected result

- All three users can call `/productdata`
- `customer` is forbidden from `/employee-contact-data`
- `employee` and `manager` can call `/employee-contact-data`
- Only `manager` can call `/salary-data`
