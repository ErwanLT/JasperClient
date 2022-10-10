# Jasper Client
## build informations
[![Build Status](https://app.travis-ci.com/ErwanLT/JasperClient.svg?branch=dev)](https://app.travis-ci.com/ErwanLT/JasperClient)
[![codecov](https://codecov.io/gh/ErwanLT/JasperClient/branch/dev/graph/badge.svg?token=gF99xjq9DF)](https://codecov.io/gh/ErwanLT/JasperClient)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/bee32ae2e1ef470cbc76f4761320e166)](https://www.codacy.com/gh/ErwanLT/JasperClient/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ErwanLT/JasperClient&amp;utm_campaign=Badge_Grade)
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
MIT License

Copyright (c) 2022 Erwan Le Tutour

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
