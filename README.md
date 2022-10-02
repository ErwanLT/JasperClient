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
## Contributing
Please contribute using GitHub Flow. Create a branch, add commits, and open a pull request.

Please read CONTRIBUTING for details on our CODE OF CONDUCT, and the process for submitting pull requests to us.

## Copyright and License