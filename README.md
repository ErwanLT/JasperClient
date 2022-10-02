# Jasper Client
## build informations

## Generate PDF using Jasper client
```mermaid
  flowchart TB
    A[Asking for PDF] --> B(Execute it on server)
    B --> C(Check Status)
    C --> D{is it ready ?}
    D -->|yes| E[Get it]
    D -->|no| C
```