# Part 2: Financial Risk System

This folder is reserved for the Part 2 architecture solution.

## Required deliverables

1. Use case diagram
2. Activity diagram showing how to create a risk report
3. Context diagram
4. Container diagram
5. Component diagram
6. Sequence diagram showing how the components work together

## Suggested file organization

- `use-case-diagram.png`
- `activity-diagram.png`
- `context-diagram.png`
- `container-diagram.png`
- `component-diagram.png`
- `sequence-diagram.png`

## Scope summary from the lab

The risk system should:

1. Import trade data from TDS
2. Import counterparty data from RDS
3. Join and enrich the data
4. Calculate risk per counterparty
5. Generate an Excel-importable report
6. Deliver the report before 9am Singapore time
7. Allow authorized users to maintain risk parameters

## Suggested component breakdown

- Trade Import Component
- Counterparty Import Component
- Data Enrichment Component
- Risk Calculation Component
- Report Generation Component
- Report Distribution Component
- Parameter Management Component
- Audit Logging Component
- Monitoring/Alerting Component

## Suggested external actors/systems

- Business User
- Risk Admin User
- Trade Data System
- Reference Data System
- Central Monitoring Service
- Authentication/Authorization System
