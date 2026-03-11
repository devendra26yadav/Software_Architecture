# Lab 4 Part 1 Submission Content

## 1. Component Redesign (from Lab 2 webshop)

The webshop is redesigned into these components:

1. Product Component
2. Shopping Component
3. Order Component
4. Customer Component
5. Supplier Component
6. Admin Component
7. Notification Component

Detailed component/classes diagram source:
- `lab4-component-redesign.puml`

## 2. Layered Architecture + Class Diagram

The system is modeled using:

- Presentation layer (controllers)
- Application layer (application services)
- Domain layer (entities, value objects, domain services, domain events)
- Infrastructure layer (repositories, email service, payment gateway)

Detailed layered class diagram source:
- `lab2-layered-class-diagram.puml`

## 3. Domain Class Type Classification

Classification list source:
- `domain-classification.md`

Summary:

- Entities: `Product`, `Review`, `Supplier`, `Customer`, `CreditCard`, `ShoppingCart`, `CartItem`, `Order`, `OrderItem`
- Value Objects: `ProductNumber`, `CustomerNumber`, `OrderNumber`, `Address`, `Money`, `StockInfo`, `EmailAddress`, `PhoneNumber`
- Domain Services: `CheckoutDomainService`, `PaymentDomainService`
- Domain Events: `ProductPriceChanged`, `OrderPlaced`, `ProductAddedToCart`

## 4. Diagram Generation

If PlantUML is installed:

```bash
cd part1
plantuml lab2-layered-class-diagram.puml
plantuml lab4-component-redesign.puml
```

Use the generated outputs plus this document for the final Part 1 PDF.
